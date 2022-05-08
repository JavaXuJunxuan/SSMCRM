package com.xjx.workbench.service.impl;

import com.xjx.commons.constants.Constants;
import com.xjx.commons.utils.DateUtils;
import com.xjx.commons.utils.UUIDUtils;
import com.xjx.settings.domain.User;
import com.xjx.workbench.dao.ContactsMapper;
import com.xjx.workbench.dao.CustomerMapper;
import com.xjx.workbench.domain.Contacts;
import com.xjx.workbench.domain.Customer;
import com.xjx.workbench.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ContactServiceImpl implements ContactService{
    @Autowired
    private ContactsMapper contactsMapper;
    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public void saveCreateContact(Contacts contacts, User user) {
//        得到联系人的客户名称，这个属性在web页面传过来的是名称，但是表中是客户的ID所以需要进行转换
        String name = contacts.getCustomerId();
        Customer customer = null;
//        如果客户名称不为空就要进行判断
        if (name != ""){
//            去客户表中查找是否有该客户名称的客户
            customer = customerMapper.selectCustomerByName(name);
//            没有就要创建
            if (customer == null){
                customer = new Customer();
                customer.setOwner(user.getId());
                customer.setName(name);
                customer.setId(UUIDUtils.getUUID());
                customer.setCreateTime(DateUtils.formateDateTime(new Date()));
                customer.setCreateBy(user.getId());
                customerMapper.insertCustomer(customer);
                contacts.setCustomerId(customer.getId());
            }
//            有就把这个客户的id取出来
            contacts.setCustomerId(customer.getId());
        }
//        如果为空就直接进行保存操作即可
        contactsMapper.insertContacts(contacts);
    }

    @Override
    public List<Contacts> queryContactsByConditionForPage(Map<String, Object> map) {
        return contactsMapper.queryContactsByConditionForPage(map);
    }

    @Override
    public int selectCountByConditionForPage(Map<String, Object> map) {
        return contactsMapper.selectCountByConditionForPage(map);
    }

    @Override
    public List<Contacts> queryAllContacts(String id) {
        return contactsMapper.queryAllContacts(id);
    }

    @Override
    public int deleteContact(String id) {
        return contactsMapper.deleteContact(id);
    }
}
