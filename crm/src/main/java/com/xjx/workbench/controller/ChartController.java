package com.xjx.workbench.controller;

import com.xjx.workbench.domain.FunnelVO;
import com.xjx.workbench.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ChartController {
    @Autowired
    private TransactionService transcationService;

    @RequestMapping("/workbench/chart/transaction/index.do")
    public String index(){
        //跳转页面
        return "workbench/chart/transaction/index";
    }

    @RequestMapping("/workbench/chart/transaction/queryCountOfTranGroupByStage.do")
    @ResponseBody
    public Object queryCountOfTranGroupByStage(){
        //调用service层方法，查询数据
        List<FunnelVO> funnelVOList = transcationService.queryCountOfTranGroupByStage();
        //根据查询结果，返回响应信息
        return funnelVOList;
    }
}