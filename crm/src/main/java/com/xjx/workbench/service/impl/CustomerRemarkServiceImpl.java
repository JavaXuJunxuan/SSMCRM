package com.xjx.workbench.service.impl;

import com.xjx.workbench.dao.CustomerRemarkMapper;
import com.xjx.workbench.domain.CustomerRemark;
import com.xjx.workbench.service.CustomerRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerRemarkServiceImpl implements CustomerRemarkService {
    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;
    @Override
    public List<CustomerRemark> selectCustomerRemarkById(String id) {
        return customerRemarkMapper.selectCustomerRemarkById(id);
    }

    @Override
    public int updateRemark(CustomerRemark customerRemark) {
        return customerRemarkMapper.updateRemark(customerRemark);
    }

    @Override
    public int deleteRemark(String id) {
        return customerRemarkMapper.deleteRemark(id);
    }

    @Override
    public int saveCreateCustomerRemark(CustomerRemark customerRemark) {
        return customerRemarkMapper.saveCreateCustomerRemark(customerRemark);
    }
}
