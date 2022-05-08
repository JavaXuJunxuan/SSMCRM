package com.xjx.workbench.service.impl;

import com.xjx.workbench.dao.CustomerMapper;
import com.xjx.workbench.domain.Customer;
import com.xjx.workbench.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public List<String> queryCustomerNameByName(String name) {
        return customerMapper.selectCustomerNameByName(name);
    }

    @Override
    public int saveCreateCustomer(Customer customer) {
        return customerMapper.saveCreateCustomer(customer);
    }

    @Override
    public List<Customer> selectCustomersByConditionForPage(Map<String, Object> map) {
        return customerMapper.selectCustomersByConditionForPage(map);
    }

    @Override
    public int selectCountByConditionForPage(Map<String, Object> map) {
        return customerMapper.selectCountByConditionForPage(map);
    }

    @Override
    public Customer queryCustomerById(String id) {
        return customerMapper.queryCustomerById(id);
    }

    @Override
    public int updateCustomer(Customer customer) {
        return customerMapper.updateCustomer(customer);
    }

    @Override
    public int deleteCustomerByIds(String[] id) {
        return customerMapper.deleteCustomerByIds(id);
    }

    @Override
    public Customer queryCustomerForDetailById(String id) {
        return customerMapper.queryCustomerForDetailById(id);
    }

    @Override
    public int selectCustomersByName(String name) {
        return customerMapper.selectCustomersByName(name);
    }
}
