package com.xjx.workbench.service.impl;

import com.xjx.workbench.dao.TransactionHistoryMapper;
import com.xjx.workbench.domain.TransactionHistory;
import com.xjx.workbench.service.TransactionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionHistoryServiceImpl implements TransactionHistoryService {
    @Autowired
    private TransactionHistoryMapper transactionHistoryMapper;
    @Override
    public List<TransactionHistory> selectDictionaryValueByTypeCode(String id) {
        return transactionHistoryMapper.selectTranHistoryForDetailByTranId(id);
    }
}
