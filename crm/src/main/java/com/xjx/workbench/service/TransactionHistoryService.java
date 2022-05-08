package com.xjx.workbench.service;

import com.xjx.workbench.domain.TransactionHistory;

import java.util.List;

public interface TransactionHistoryService {
    List<TransactionHistory> selectDictionaryValueByTypeCode(String id);
}
