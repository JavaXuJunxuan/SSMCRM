package com.xjx.workbench.service;

import com.xjx.workbench.domain.FunnelVO;
import com.xjx.workbench.domain.Transaction;

import java.util.List;
import java.util.Map;

public interface TransactionService {
    public void saveCreateTran(Map<String, Object> map);
    public Transaction queryTranForDetailById(String id);
    public List<FunnelVO> queryCountOfTranGroupByStage();
    public List<Transaction> queryAllTransaction(String id);
    public int deleteTransaction(String id);

}
