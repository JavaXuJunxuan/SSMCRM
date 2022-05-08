package com.xjx.settings.service.impl;

import com.xjx.settings.dao.DictionaryValueMapper;
import com.xjx.settings.domain.DictionaryValue;
import com.xjx.settings.service.DictionaryValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictionaryValueServiceImpl implements DictionaryValueService {
    @Autowired
    private DictionaryValueMapper dictionaryValueMapper;

    @Override
    public List<DictionaryValue> selectDictionaryValueByTypeCode(String typeCode) {
        return dictionaryValueMapper.selectDicValueByTypeCode(typeCode);
    }
}
