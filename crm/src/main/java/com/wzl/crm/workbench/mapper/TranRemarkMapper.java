package com.wzl.crm.workbench.mapper;

import com.wzl.crm.workbench.domain.TranRemark;

import java.util.List;

public interface TranRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_remark
     *
     * @mbggenerated Sat May 06 11:12:34 CST 2023
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_remark
     *
     * @mbggenerated Sat May 06 11:12:34 CST 2023
     */
    int insert(TranRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_remark
     *
     * @mbggenerated Sat May 06 11:12:34 CST 2023
     */
    int insertSelective(TranRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_remark
     *
     * @mbggenerated Sat May 06 11:12:34 CST 2023
     */
    TranRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_remark
     *
     * @mbggenerated Sat May 06 11:12:34 CST 2023
     */
    int updateByPrimaryKeySelective(TranRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_remark
     *
     * @mbggenerated Sat May 06 11:12:34 CST 2023
     */
    int updateByPrimaryKey(TranRemark record);

    /**
     * 批量增加交易备注
     */
    int insertTranRemarkByList(List<TranRemark> tranRemarkList);

    /**
     * 根据交易id查询交易评论
     */
    List<TranRemark> selectTranHistoryForTranid(String tranId);

    /**
     * 创建交易备注
     */
    int insertTranRemark(TranRemark tranRemark);

    /**
     * 删除交易备注
     */
    int deleteTranRemarkById(String id);

    /**
     * 修改交易备注
     */
    int  updateTranRemarkById(TranRemark tranRemark);
}