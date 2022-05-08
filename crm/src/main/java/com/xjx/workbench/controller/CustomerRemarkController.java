package com.xjx.workbench.controller;

import com.xjx.commons.constants.Constants;
import com.xjx.commons.domain.ReturnObject;
import com.xjx.commons.utils.DateUtils;
import com.xjx.commons.utils.UUIDUtils;
import com.xjx.settings.domain.User;
import com.xjx.workbench.domain.CustomerRemark;
import com.xjx.workbench.service.CustomerRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class CustomerRemarkController {
    @Autowired
    private CustomerRemarkService customerRemarkService;

    @RequestMapping("workbench/customerRemark/editRemark.do")
    @ResponseBody
    public Object editRemark(CustomerRemark customerRemark, HttpSession httpSession){
        ReturnObject returnObject = new ReturnObject();
        User user = (User) httpSession.getAttribute(Constants.SESSION_USER);
        customerRemark.setEditBy(user.getId());
        customerRemark.setEditFlag(Constants.REMARK_EDIT_FLAG_YES_EDITED);
        customerRemark.setEditTime(DateUtils.formateDateTime(new Date()));
        try {
            int count = customerRemarkService.updateRemark(customerRemark);
            if (count > 0){
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRtn(customerRemark);
            }else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙，请稍后重试");
            }
        }catch (Exception e){
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后重试");
        }
        return returnObject;
    }
    @RequestMapping("workbench/customerRemark/deleteRemark.do")
    @ResponseBody
    public Object deleteRemark(String id){
        ReturnObject returnObject = new ReturnObject();
        try {
            int count = customerRemarkService.deleteRemark(id);
            if (count > 0){
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙，请稍后重试");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后重试");
        }
        return returnObject;
    }
    @RequestMapping("/workbench/customerRemark/saveCreateCustomerRemark.do")
    @ResponseBody
    public Object saveCreateCustomerRemark(CustomerRemark customerRemark,HttpSession session){
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        customerRemark.setId(UUIDUtils.getUUID());
        customerRemark.setCreateBy(user.getId());
        customerRemark.setCreateTime(DateUtils.formateDateTime(new Date()));
        customerRemark.setEditFlag(Constants.REMARK_EDIT_FLAG_NO_EDITED);
        ReturnObject returnObject = new ReturnObject();
        try {
            int count = customerRemarkService.saveCreateCustomerRemark(customerRemark);
            if (count > 0){
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRtn(customerRemark);
            }else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙，请稍后重试");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后重试");
        }
        return returnObject;
    }

}
