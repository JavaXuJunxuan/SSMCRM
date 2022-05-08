package com.xjx.workbench.dao;

import com.xjx.workbench.domain.FunnelVO;
import com.xjx.workbench.domain.TransactionHistory;

import java.util.List;

public interface TransactionHistoryMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_history
     *
     * @mbggenerated Tue Apr 26 18:12:20 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_history
     *
     * @mbggenerated Tue Apr 26 18:12:20 CST 2022
     */
    int insert(TransactionHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_history
     *
     * @mbggenerated Tue Apr 26 18:12:20 CST 2022
     */
    int insertSelective(TransactionHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_history
     *
     * @mbggenerated Tue Apr 26 18:12:20 CST 2022
     */
    TransactionHistory selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_history
     *
     * @mbggenerated Tue Apr 26 18:12:20 CST 2022
     */
    int updateByPrimaryKeySelective(TransactionHistory record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_history
     *
     * @mbggenerated Tue Apr 26 18:12:20 CST 2022
     */
    int updateByPrimaryKey(TransactionHistory record);

    int insertTranHistory(TransactionHistory transactionHistory);

    List<TransactionHistory> selectTranHistoryForDetailByTranId(String id);
}