package com.xjx.workbench.controller;

import com.xjx.commons.constants.Constants;
import com.xjx.commons.domain.ReturnObject;
import com.xjx.workbench.service.TransactionRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TransactionRemarkController {
    @Autowired
    private TransactionRemarkService transactionRemarkService;

    /*@RequestMapping("workbench/transactionRemark/editRemark.do")
    @ResponseBody
    public Object editRemark(TransactionRemarkController transactionRemark){
        ReturnObject returnObject = new ReturnObject();
        try {
            int count = transactionRemarkService.updateRemark(transactionRemark);
            if (count > 0) {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);

            }
        }catch (Exception e){

        }
    }*/
}
