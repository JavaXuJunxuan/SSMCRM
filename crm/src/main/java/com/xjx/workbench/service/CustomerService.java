package com.xjx.workbench.service;

import com.xjx.workbench.domain.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerService {
    public List<String> queryCustomerNameByName(String name);
    public int saveCreateCustomer(Customer customer);
    List<Customer> selectCustomersByConditionForPage(Map<String, Object> map);
    int selectCountByConditionForPage(Map<String, Object> map);
    Customer queryCustomerById(String id);
    int updateCustomer(Customer customer);
    int deleteCustomerByIds(String[] id);
    Customer queryCustomerForDetailById(String id);
    int selectCustomersByName(String name);
}
