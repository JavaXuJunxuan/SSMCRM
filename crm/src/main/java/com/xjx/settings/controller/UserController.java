package com.xjx.settings.controller;

import com.xjx.commons.constants.Constants;
import com.xjx.commons.domain.ReturnObject;
import com.xjx.commons.utils.DateUtils;
import com.xjx.settings.domain.User;
import com.xjx.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserService userService;


    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin() {
        return "settings/qx/user/login";
    }

    @RequestMapping("/settings/qx/user/login.do")
    @ResponseBody
    public Object queryUserByLoginActAndPwd(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Map<String, Object> map = new HashMap();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);
        map.put("isRemPwd", isRemPwd);
        User user = userService.queryUserByLoginActAndPwd(map);
        ReturnObject returnObject = new ReturnObject();
        if (user == null) {
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("账号或者密码错误");
        }else {
            if (DateUtils.formateDateTime(new Date()).compareTo(user.getExpireTime()) > 0){
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("登陆失败，账号已经过期");
            }else if (!user.getAllowIps().contains(httpServletRequest.getRemoteAddr())){
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("登陆失败，账号的IP地址受限");
            }else if ("0".equals(user.getLockState())){
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("登陆失败，账号已经被锁定");
            }else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                httpServletRequest.getSession().setAttribute(Constants.SESSION_USER,user);
                if ("true".equals(isRemPwd)){
                    Cookie cookie1 = new Cookie("loginAct",user.getLoginAct());
                    cookie1.setMaxAge(10*24*60*60);
                    httpServletResponse.addCookie(cookie1);
                    Cookie cookie2 = new Cookie("loginPwd",user.getLoginPwd());
                    cookie2.setMaxAge(10*24*60*60);
                    httpServletResponse.addCookie(cookie2);
                }else {
                    Cookie cookie1 = new Cookie("loginAct","1");
                    cookie1.setMaxAge(0);
                    httpServletResponse.addCookie(cookie1);
                    Cookie cookie2 = new Cookie("loginPwd","1");
                    cookie2.setMaxAge(0);
                    httpServletResponse.addCookie(cookie2);
                }
            }
        }
        return returnObject;
    }

    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response, HttpSession session){
        Cookie cookie1 = new Cookie("loginAct","1");
        cookie1.setMaxAge(0);
        response.addCookie(cookie1);
        Cookie cookie2 = new Cookie("loginPwd","1");
        cookie2.setMaxAge(0);
        response.addCookie(cookie2);
        session.invalidate();
        return "redirect:/";
    }
}
