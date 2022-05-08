package com.xjx.workbench.controller;

import com.xjx.commons.constants.Constants;
import com.xjx.commons.domain.ReturnObject;
import com.xjx.settings.domain.DictionaryValue;
import com.xjx.settings.domain.User;
import com.xjx.settings.service.DictionaryValueService;
import com.xjx.settings.service.UserService;
import com.xjx.workbench.domain.Transaction;
import com.xjx.workbench.domain.TransactionHistory;
import com.xjx.workbench.domain.TransactionRemark;
import com.xjx.workbench.service.CustomerService;
import com.xjx.workbench.service.TransactionHistoryService;
import com.xjx.workbench.service.TransactionRemarkService;
import com.xjx.workbench.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
public class TransactionController {
    @Autowired
    private DictionaryValueService dictionaryValueService;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private TransactionService transcationService;
    @Autowired
    private TransactionRemarkService transactionRemarkService;
    @Autowired
    private TransactionHistoryService transactionHistoryService;



    @RequestMapping("/workbench/transaction/index.do")
    public String index(HttpServletRequest request){
        //调用service层方法，查询动态数据
        List<DictionaryValue> stageList = dictionaryValueService.selectDictionaryValueByTypeCode("stage");
        List<DictionaryValue> transactionTypeList = dictionaryValueService.selectDictionaryValueByTypeCode("transactionType");
        List<DictionaryValue> sourceList = dictionaryValueService.selectDictionaryValueByTypeCode("source");
        //把数据保存到request
        request.setAttribute("stageList",stageList);
        request.setAttribute("transactionTypeList",transactionTypeList);
        request.setAttribute("sourceList",sourceList);
        //请求转发
        return "/workbench/transaction/index";
    }
    @RequestMapping("/workbench/transaction/saveTransaction.do")
    public String saveTransaction(HttpServletRequest request) {
        //调用service层方法，查询动态数据
        List<User> userList = userService.queryAllUsers();
        List<DictionaryValue> stageList = dictionaryValueService.selectDictionaryValueByTypeCode("stage");
        List<DictionaryValue> transactionTypeList = dictionaryValueService.selectDictionaryValueByTypeCode("transactionType");
        List<DictionaryValue> sourceList = dictionaryValueService.selectDictionaryValueByTypeCode("source");
        //把数据保存到request中
        request.setAttribute("userList", userList);
        request.setAttribute("stageList", stageList);
        request.setAttribute("transactionTypeList", transactionTypeList);
        request.setAttribute("sourceList", sourceList);
        //请求转发
        return "workbench/transaction/save";
    }
    @RequestMapping("/workbench/transaction/getPossibilityByStage.do")
    @ResponseBody
    public Object getPossibilityByStage(String stageValue){
        System.out.println(stageValue);
        //解析properties配置文件，根据阶段获取可能性
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility = bundle.getString(stageValue);
        //返回响应信息
        return possibility;
    }
    @RequestMapping("/workbench/transaction/queryCustomerNameByName.do")
    @ResponseBody
    public Object queryCustomerNameByName(String customerName){
        //调用service层方法，查询所有客户名称
        List<String> customerNameList = customerService.queryCustomerNameByName(customerName);
        //根据查询结果，返回响应信息
        return customerNameList;//['xxxx','xxxxx',......]
    }

    @RequestMapping("/workbench/transaction/saveCreateTran.do")
    @ResponseBody
    public Object saveCreateTran(@RequestParam Map<String,Object> map, HttpSession session){
        //封装参数
        map.put(Constants.SESSION_USER,session.getAttribute(Constants.SESSION_USER));
        ReturnObject returnObject = new ReturnObject();
        try {
            //调用service层方法，保存创建的交易
            transcationService.saveCreateTran(map);
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试....");
        }
        return returnObject;
    }
    @RequestMapping("/workbench/transaction/detail.do")
    public String detail(String id,HttpServletRequest request){
        //调用service层方法，查询数据
        Transaction transaction = transcationService.queryTranForDetailById(id);
        List<TransactionRemark> remarkList = transactionRemarkService.queryTranRemarkForDetailByTranId(id);
        List<TransactionHistory> historyList = transactionHistoryService.selectDictionaryValueByTypeCode(id);

        //根据tran所处阶段名称查询可能性
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility = bundle.getString(transaction.getStage());
        transaction.setPossibility(possibility);
        //把数据保存到request中
        request.setAttribute("tran",transaction);
        request.setAttribute("remarkList",remarkList);
        request.setAttribute("historyList",historyList);
        //request.setAttribute("possibility",possibility);
        //调用service方法，查询交易所有的阶段
        List<DictionaryValue> stageList = dictionaryValueService.selectDictionaryValueByTypeCode("stage");
        request.setAttribute("stageList",stageList);
        //请求转发
        return "workbench/transaction/detail";
    }
    @RequestMapping("/workbench/transaction/deleteTransaction.do")
    @ResponseBody
    public Object deleteTransaction(String id){
        System.out.println(id);
        ReturnObject returnObject = new ReturnObject();
        try {
            int count = transcationService.deleteTransaction(id);
            if (count > 0) {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统繁忙，请稍后重试");
            }
        }catch (Exception e){
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后重试");
        }
        return returnObject;
    }
}

