package com.xjx.workbench.service.impl;

import com.xjx.commons.constants.Constants;
import com.xjx.commons.utils.DateUtils;
import com.xjx.commons.utils.UUIDUtils;
import com.xjx.settings.domain.User;
import com.xjx.workbench.dao.*;
import com.xjx.workbench.domain.*;
import com.xjx.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ClueServiceImpl implements ClueService {
    @Autowired
    private ClueMapper clueMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ContactsMapper contactsMapper;
    @Autowired
    private ClueRemarkMapper clueRemarkMapper;
    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;
    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;
    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private TransactionRemarkMapper transactionRemarkMapper;

    @Override
    public int saveCreateClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }

    @Override
    public List<Clue> selectActivityByConditionForPage(Map map) {
        return clueMapper.selectActivityByConditionForPage(map);
    }

    @Override
    public int selectCountByConditionForPage(Map map) {
        return clueMapper.selectCountByConditionForPage(map);
    }

    @Override
    public Clue queryClueForDetailById(String id) {
        return clueMapper.selectClueForDetailById(id);
    }

    @Override
    public Clue selectClueById(String id) {
        return clueMapper.selectClueById(id);
    }

    @Override
    public int saveEditClue(Clue clue) {
        return clueMapper.updateClue(clue);
    }

    @Override
    public int deleteClueById(String[] ids) {
        return clueMapper.deleteClueById(ids);
    }

    @Override
    public void saveConvertClue(Map map) {
        String clueId = (String) map.get("clueId");
        Clue clue = clueMapper.selectClueById(clueId);
        User user=(User) map.get(Constants.SESSION_USER);
        String isCreateTran = (String) map.get("isCreateTran");

        Customer customer = new Customer();
        customer.setId(UUIDUtils.getUUID());
        customer.setAddress(clue.getAddress());
        customer.setContactSummary(clue.getContactSummary());
        customer.setCreateBy(user.getId());
        customer.setCreateTime(DateUtils.formateDateTime(new Date()));
        customer.setDescription(clue.getDescription());
        customer.setName(clue.getCompany());
        customer.setNextContactTime(clue.getNextContactTime());
        customer.setOwner(user.getId());
        customer.setPhone(clue.getPhone());
        customer.setWebsite(clue.getWebsite());
        customerMapper.insertCustomer(customer);

        Contacts contacts = new Contacts();
        contacts.setAddress(clue.getAddress());
        contacts.setAppellation(clue.getAppellation());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setCreateBy(user.getId());
        contacts.setCreateTime(DateUtils.formateDateTime(new Date()));
        contacts.setCustomerId(customer.getId());
        contacts.setDescription(clue.getDescription());
        contacts.setEmail(clue.getEmail());
        contacts.setFullname(clue.getFullname());
        contacts.setId(UUIDUtils.getUUID());
        contacts.setJob(clue.getJob());
        contacts.setMphone(clue.getMphone());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setOwner(user.getId());
        contacts.setSource(clue.getSource());
        contactsMapper.insertContacts(contacts);

        //根据clueId查询该线索下所有的备注
        List<ClueRemark> crList = clueRemarkMapper.selectClueRemarkByClueId(clueId);
        //如果该线索下有备注，把该线索下所有的备注转换到客户备注表中一份
        if(crList !=null && crList.size() > 0) {
            //遍历crList，封装客户备注
            CustomerRemark customerRemark = null;
            ContactsRemark contactsRemark = null;
            List<CustomerRemark> curList = new ArrayList<>();
            List<ContactsRemark> corList = new ArrayList<>();
            for (ClueRemark clueRemark : crList) {
                customerRemark = new CustomerRemark();
                customerRemark.setCreateBy(clueRemark.getCreateBy());
                customerRemark.setCreateTime(clueRemark.getCreateTime());
                customerRemark.setCustomerId(customer.getId());
                customerRemark.setEditBy(clueRemark.getEditBy());
                customerRemark.setEditFlag(clueRemark.getEditFlag());
                customerRemark.setEditTime(clueRemark.getEditTime());
                customerRemark.setId(UUIDUtils.getUUID());
                customerRemark.setNoteContent(clueRemark.getNoteContent());
                curList.add(customerRemark);

                contactsRemark = new ContactsRemark();
                contactsRemark.setContactsId(clueRemark.getId());
                contactsRemark.setCreateBy(clueRemark.getCreateBy());
                contactsRemark.setCreateTime(clueRemark.getCreateTime());
                contactsRemark.setEditBy(clueRemark.getEditBy());
                contactsRemark.setEditFlag(clueRemark.getEditFlag());
                contactsRemark.setEditTime(clueRemark.getEditTime());
                contactsRemark.setId(UUIDUtils.getUUID());
                contactsRemark.setNoteContent(clueRemark.getNoteContent());
                corList.add(contactsRemark);
            }
            customerRemarkMapper.insertCustomerRemarkByList(curList);
            contactsRemarkMapper.insertContactsRemarkByList(corList);
        }
        //根据clueId查询该线索和市场活动的关联关系
        List<ClueActivityRelation> carList = clueActivityRelationMapper.selectClueActivityRelationByClueId(clueId);
        //把该线索和市场活动的关联关系转换到联系人和市场活动的关联关系表中
        if(carList != null && carList.size() > 0){
            ContactsActivityRelation coar = null;
            List<ContactsActivityRelation> coarList = new ArrayList<>();
            for(ClueActivityRelation car:carList){
                coar = new ContactsActivityRelation();
                coar.setActivityId(car.getActivityId());
                coar.setContactsId(contacts.getId());
                coar.setId(UUIDUtils.getUUID());
                coarList.add(coar);
            }
            contactsActivityRelationMapper.insertContactsActivityRelationByList(coarList);
        }
        //如果需要创建交易，则往交易表中添加一条记录,还需要把该线索下的备注转换到交易备注表中一份
        if("true".equals(isCreateTran)){
            Transaction transaction = new Transaction();
            transaction.setActivityId((String) map.get("activityId"));
            transaction.setContactsId(contacts.getId());
            transaction.setCreateBy(user.getId());
            transaction.setCreateTime(DateUtils.formateDateTime(new Date()));
            transaction.setCustomerId(customer.getId());
            transaction.setExpectedDate((String) map.get("expectedDate"));
            transaction.setId(UUIDUtils.getUUID());
            transaction.setMoney((String) map.get("money"));
            transaction.setName((String) map.get("name"));
            transaction.setOwner(user.getId());
            transaction.setStage((String) map.get("stage"));
            transactionMapper.insertTran(transaction);

            if(crList != null && crList.size() > 0){
                TransactionRemark transactionRemark = null;
                List<TransactionRemark> trList=new ArrayList<>();
                for(ClueRemark cr : crList){
                    transactionRemark = new TransactionRemark();
                    transactionRemark.setCreateBy(cr.getCreateBy());
                    transactionRemark.setCreateTime(cr.getCreateTime());
                    transactionRemark.setEditBy(cr.getEditBy());
                    transactionRemark.setEditFlag(cr.getEditFlag());
                    transactionRemark.setEditTime(cr.getEditTime());
                    transactionRemark.setId(UUIDUtils.getUUID());
                    transactionRemark.setNoteContent(cr.getNoteContent());
                    transactionRemark.setTranId(transaction.getId());
                    trList.add(transactionRemark);
                }
                transactionRemarkMapper.insertTranRemarkByList(trList);
            }
        }
        //删除该线索下所有的备注
        clueRemarkMapper.deleteClueRemarkByClueId(clueId);
        //删除该线索和市场活动的关联关系
        clueActivityRelationMapper.deleteClueActivityRelationByClueId(clueId);
        String[] ids = {clueId};
        //删除线索
        clueMapper.deleteClueById(ids);
    }
}
