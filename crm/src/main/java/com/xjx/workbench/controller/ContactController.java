package com.xjx.workbench.controller;

import com.xjx.commons.constants.Constants;
import com.xjx.commons.domain.ReturnObject;
import com.xjx.commons.utils.DateUtils;
import com.xjx.commons.utils.UUIDUtils;
import com.xjx.settings.domain.DictionaryValue;
import com.xjx.settings.domain.User;
import com.xjx.settings.service.DictionaryValueService;
import com.xjx.settings.service.UserService;
import com.xjx.workbench.domain.Contacts;
import com.xjx.workbench.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ContactController {
    @Autowired
    private UserService userService;
    @Autowired
    private DictionaryValueService dictionaryValueService;
    @Autowired
    private ContactService contactService;

    @RequestMapping("/workbench/contact/index.do")
    public String index(HttpServletRequest request){
        List<User> userList = userService.queryAllUsers();
        List<DictionaryValue> appellationList = dictionaryValueService.selectDictionaryValueByTypeCode("appellation");
        List<DictionaryValue> sourceList = dictionaryValueService.selectDictionaryValueByTypeCode("source");
        request.setAttribute("userList",userList);
        request.setAttribute("appellationList",appellationList);
        request.setAttribute("sourceList",sourceList);
        return "workbench/contact/index";
    }
    @RequestMapping("/workbench/contact/saveCreateContact.do")
    @ResponseBody
    public Object saveCreateCustomer(Contacts contacts, HttpSession session){
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        contacts.setId(UUIDUtils.getUUID());
        contacts.setCreateBy(user.getId());
        contacts.setCreateTime(DateUtils.formateDateTime(new Date()));
        ReturnObject returnObject = new ReturnObject();
        try {
            contactService.saveCreateContact(contacts,user);
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRtn(contacts);
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后再试...");
        }
        return returnObject;
    }
    @RequestMapping("/workbench/contact/queryContactsByConditionForPage.do")
    @ResponseBody
    public Object queryContactsByConditionForPage(String owner,String fullname,String customerId,String source,
                                                  String birthday,int pageNo,int pageSize){
        Map<String,Object> map = new HashMap<>();
        map.put("owner",owner);
        map.put("fullname",fullname);
        map.put("customerId",customerId);
        System.out.println(customerId+"11111111111111111111111111111111111111111111111111111");
        map.put("source",source);
        map.put("birthday",birthday);
        map.put("beginNo", (pageNo - 1) * pageSize);
        map.put("pageSize",pageSize);
        List<Contacts> contactsList = contactService.queryContactsByConditionForPage(map);
        int count = contactService.selectCountByConditionForPage(map);
        Map<String,Object> result = new HashMap<>();
        result.put("contactsList", contactsList);
        result.put("totalRows", count);
        return result;
    }
    @RequestMapping("/workbench/contact/deleteContact.do")
    @ResponseBody
    public Object deleteContact(String id){
        ReturnObject returnObject = new ReturnObject();
        try {
            int count = contactService.deleteContact(id);
            if (count > 0){
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后再试...");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后再试...");
        }
        return returnObject;
    }
    /*@RequestMapping("/workbench/contact/.do")
    @ResponseBody
    public Object (){

    }*/
}
