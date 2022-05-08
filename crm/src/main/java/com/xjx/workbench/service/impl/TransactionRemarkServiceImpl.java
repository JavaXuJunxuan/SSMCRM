package com.xjx.workbench.service.impl;

import com.xjx.workbench.controller.TransactionRemarkController;
import com.xjx.workbench.dao.TransactionRemarkMapper;
import com.xjx.workbench.domain.TransactionRemark;
import com.xjx.workbench.service.TransactionRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionRemarkServiceImpl implements TransactionRemarkService {
    @Autowired
    private TransactionRemarkMapper transactionRemarkMapper;

    @Override
    public List<TransactionRemark> queryTranRemarkForDetailByTranId(String id) {
        return transactionRemarkMapper.queryTranRemarkForDetailByTranId(id);
    }

    @Override
    public int updateRemark(TransactionRemarkController transactionRemark) {
        return 0;
    }
}
