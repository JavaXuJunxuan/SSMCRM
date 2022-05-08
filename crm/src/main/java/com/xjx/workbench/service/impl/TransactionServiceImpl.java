package com.xjx.workbench.service.impl;

import com.xjx.commons.constants.Constants;
import com.xjx.commons.utils.DateUtils;
import com.xjx.commons.utils.UUIDUtils;
import com.xjx.settings.domain.User;
import com.xjx.workbench.dao.CustomerMapper;
import com.xjx.workbench.dao.TransactionHistoryMapper;
import com.xjx.workbench.dao.TransactionMapper;
import com.xjx.workbench.domain.Customer;
import com.xjx.workbench.domain.FunnelVO;
import com.xjx.workbench.domain.Transaction;
import com.xjx.workbench.domain.TransactionHistory;
import com.xjx.workbench.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private TransactionHistoryMapper transactionHistoryMapper;

    @Override
    public void saveCreateTran(Map<String, Object> map) {
        String customerName = (String) map.get("customerName");
        User user=(User) map.get(Constants.SESSION_USER);
        //根据name精确查询客户
        Customer customer = customerMapper.selectCustomerByName(customerName);
        //如果客户不存在，则新建客户
        if(customer == null){
            customer = new Customer();
            customer.setOwner(user.getId());
            customer.setName(customerName);
            customer.setId(UUIDUtils.getUUID());
            customer.setCreateTime(DateUtils.formateDateTime(new Date()));
            customer.setCreateBy(user.getId());
            customerMapper.insertCustomer(customer);
        }
        //保存创建的交易
        Transaction transaction = new Transaction();
        transaction.setStage((String) map.get("stage"));
        transaction.setOwner((String) map.get("owner"));
        transaction.setNextContactTime((String) map.get("nextContactTime"));
        transaction.setName((String) map.get("name"));
        transaction.setMoney((String) map.get("money"));
        transaction.setId(UUIDUtils.getUUID());
        transaction.setExpectedDate((String) map.get("expectedDate"));
        transaction.setCustomerId(customer.getId());
        transaction.setCreateTime(DateUtils.formateDateTime(new Date()));
        transaction.setCreateBy(user.getId());
        transaction.setContactSummary((String) map.get("contactSummary"));
        transaction.setContactsId((String) map.get("contactsId"));
        transaction.setActivityId((String) map.get("activityId"));
        transaction.setDescription((String) map.get("description"));
        transaction.setSource((String) map.get("source"));
        transaction.setType((String) map.get("type"));
        transactionMapper.insertTran(transaction);
        //保存交易历史
        TransactionHistory transactionHistory=new TransactionHistory();
        transactionHistory.setCreateBy(user.getId());
        transactionHistory.setCreateTime(DateUtils.formateDateTime(new Date()));
        transactionHistory.setExpectedDate(transaction.getExpectedDate());
        transactionHistory.setId(UUIDUtils.getUUID());
        transactionHistory.setMoney(transaction.getMoney());
        transactionHistory.setStage(transaction.getStage());
        transactionHistory.setTranId(transaction.getId());
        transactionHistoryMapper.insertTranHistory(transactionHistory);
    }

    @Override
    public Transaction queryTranForDetailById(String id) {
        return transactionMapper.selectTranForDetailById(id);
    }

    @Override
    public List<FunnelVO> queryCountOfTranGroupByStage() {
        return transactionMapper.selectCountOfTranGroupByStage();
    }

    @Override
    public List<Transaction> queryAllTransaction(String id) {
        return transactionMapper.queryAllTransaction(id);
    }

    @Override
    public int deleteTransaction(String id) {
        return transactionMapper.deleteTransaction(id);
    }

    /*@Override
    public List<Transaction> queryAllTransaction() {
        *//*return transactionMapper.queryAllTransaction();*//*
    }*/
}
