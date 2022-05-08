package com.xjx.settings.dao;

import com.xjx.settings.domain.DictionaryValue;

import java.util.List;

public interface DictionaryValueMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_value
     *
     * @mbggenerated Tue Apr 19 14:34:36 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_value
     *
     * @mbggenerated Tue Apr 19 14:34:36 CST 2022
     */
    int insert(DictionaryValue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_value
     *
     * @mbggenerated Tue Apr 19 14:34:36 CST 2022
     */
    int insertSelective(DictionaryValue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_value
     *
     * @mbggenerated Tue Apr 19 14:34:36 CST 2022
     */
    DictionaryValue selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_value
     *
     * @mbggenerated Tue Apr 19 14:34:36 CST 2022
     */
    int updateByPrimaryKeySelective(DictionaryValue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_value
     *
     * @mbggenerated Tue Apr 19 14:34:36 CST 2022
     */
    int updateByPrimaryKey(DictionaryValue record);

    List<DictionaryValue> selectDicValueByTypeCode(String typeCode);
}