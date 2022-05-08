package com.xjx.workbench.service;

import com.xjx.workbench.controller.TransactionRemarkController;
import com.xjx.workbench.domain.TransactionRemark;

import java.util.List;

public interface TransactionRemarkService {
    List<TransactionRemark> queryTranRemarkForDetailByTranId(String id);

    int updateRemark(TransactionRemarkController transactionRemark);
}
