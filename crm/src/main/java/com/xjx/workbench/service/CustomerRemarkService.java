package com.xjx.workbench.service;

import com.xjx.workbench.domain.CustomerRemark;

import java.util.List;

public interface CustomerRemarkService {
    List<CustomerRemark> selectCustomerRemarkById(String id);

    int updateRemark(CustomerRemark customerRemark);

    int deleteRemark(String id);

    int saveCreateCustomerRemark(CustomerRemark customerRemark);
}
