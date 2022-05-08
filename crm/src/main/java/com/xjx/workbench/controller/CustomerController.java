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
import com.xjx.workbench.domain.Customer;
import com.xjx.workbench.domain.CustomerRemark;
import com.xjx.workbench.domain.Transaction;
import com.xjx.workbench.service.ContactService;
import com.xjx.workbench.service.CustomerRemarkService;
import com.xjx.workbench.service.CustomerService;
import com.xjx.workbench.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class CustomerController {
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRemarkService customerRemarkService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private DictionaryValueService dictionaryValueService;

    @RequestMapping("/workbench/customer/index.do")
    public String index(){
        return "workbench/customer/index";
    }

    @RequestMapping("/workbench/customer/queryAllUsers.do")
    @ResponseBody
    public Object queryAllUsers(){
        List<User> userList = userService.queryAllUsers();
        return userList;
    }

    @RequestMapping("/workbench/customer/saveCreateCustomer.do")
    @ResponseBody
    public Object saveCreateCustomer(Customer customer, HttpSession session){
        ReturnObject returnObject = new ReturnObject();
        User user = (User)session.getAttribute(Constants.SESSION_USER);
        customer.setCreateBy(user.getId());
        customer.setCreateTime(DateUtils.formateDateTime(new Date()));
        customer.setId(UUIDUtils.getUUID());
        String name = customer.getName();
        try {
            int number = customerService.selectCustomersByName(name);
            if (number != 0){
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("客户名称重复，请修改客户名称");
                return returnObject;
            }
            int count = customerService.saveCreateCustomer(customer);
            if (count > 0) {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
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

    @RequestMapping("/workbench/customer/queryCustomersByConditionForPage.do")
    @ResponseBody
    public Object queryCustomersByConditionForPage(String name, String owner, String phone, String website,
                                                   int pageNo, int pageSize){
        Map<String,Object> map = new HashMap<>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("phone", phone);
        map.put("website", website);
        map.put("beginNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);
        List<Customer> customerList = customerService.selectCustomersByConditionForPage(map);
        int count = customerService.selectCountByConditionForPage(map);
        Map result = new HashMap();
        result.put("customerList", customerList);
        result.put("totalRows", count);
        return result;
    }

    @RequestMapping("/workbench/customer/queryAllUsersAndCustomerById.do")
    @ResponseBody
    public Object queryAllUsersAndCustomerById(String id){
        System.out.println("----------------------"+id);
        Map<String,Object> map = new HashMap<>();
        List<User> userList = userService.queryAllUsers();
        Customer customer = customerService.queryCustomerById(id);
        map.put("userList",userList);
        map.put("customer",customer);
        return map;
    }

    @RequestMapping("/workbench/customer/saveEditCustomer.do")
    @ResponseBody
    public Object saveEditCustomer(Customer customer,HttpSession session){
        ReturnObject returnObject = new ReturnObject();
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        customer.setEditBy(user.getId());
        customer.setEditTime(DateUtils.formateDateTime(new Date()));
        try {
            int count = customerService.updateCustomer(customer);
            if (count > 0) {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙，请稍后重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后重试");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/customer/deleteCustomerByIds.do")
    @ResponseBody
    public Object deleteCustomerByIds(String[] id){
        System.out.println(id);
        ReturnObject returnObject = new ReturnObject();
        try {
            int count = customerService.deleteCustomerByIds(id);
            if (count > 0) {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙，请稍后重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后重试");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/customer/detail.do")
    public String detail(String id, HttpServletRequest request){
        List<User> userList  = userService.queryAllUsers();
        List<DictionaryValue> sourceList  = dictionaryValueService.selectDictionaryValueByTypeCode("source");
        List<DictionaryValue> appellationList  = dictionaryValueService.selectDictionaryValueByTypeCode("appellation");
        Customer customer = customerService.queryCustomerForDetailById(id);
        List<CustomerRemark> remarkList = customerRemarkService.selectCustomerRemarkById(id);
        List<Transaction> transactionList = transactionService.queryAllTransaction(id);
        ResourceBundle resourceBundle = ResourceBundle.getBundle("possibility");
        for (Transaction transaction : transactionList){
            transaction.setPossibility(resourceBundle.getString(transaction.getStage()));
        }
        List<Contacts> contactsList = contactService.queryAllContacts(id);
        request.setAttribute("userList",userList);
        request.setAttribute("sourceList",sourceList);
        request.setAttribute("appellationList",appellationList);
        request.setAttribute("customer",customer);
        request.setAttribute("remarkList",remarkList);
        request.setAttribute("transactionList",transactionList);
        request.setAttribute("contactsList",contactsList);
        return "workbench/customer/detail";
    }

    /*@RequestMapping("/workbench/customer/.do")
    @ResponseBody
    public Object (){

    }*/
}
