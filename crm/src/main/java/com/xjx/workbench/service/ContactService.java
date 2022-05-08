package com.xjx.workbench.service;

import com.xjx.settings.domain.User;
import com.xjx.workbench.domain.Contacts;

import java.util.List;
import java.util.Map;

public interface ContactService {
    void saveCreateContact(Contacts contacts, User user);

    List<Contacts> queryContactsByConditionForPage(Map<String, Object> map);

    int selectCountByConditionForPage(Map<String, Object> map);

    List<Contacts> queryAllContacts(String id);

    int deleteContact(String id);
}
