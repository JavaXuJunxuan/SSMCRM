package com.xjx.settings.service;

import com.xjx.settings.domain.DictionaryValue;

import java.util.List;

public interface DictionaryValueService {
    List<DictionaryValue> selectDictionaryValueByTypeCode(String typeCode);
}
