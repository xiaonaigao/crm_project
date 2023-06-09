package com.wzl.crm.workbench.mapper;

import com.wzl.crm.workbench.domain.FunnelVO;
import com.wzl.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Sat May 06 11:11:30 CST 2023
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Sat May 06 11:11:30 CST 2023
     */
    int insert(Tran record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Sat May 06 11:11:30 CST 2023
     */
    int insertSelective(Tran record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Sat May 06 11:11:30 CST 2023
     */
    Tran selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Sat May 06 11:11:30 CST 2023
     */
    int updateByPrimaryKeySelective(Tran record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran
     *
     * @mbggenerated Sat May 06 11:11:30 CST 2023
     */
    int updateByPrimaryKey(Tran record);

    /**
     * 增加交易
     */
    int insertTran(Tran tran);
    /**
     * 主页-查询交易
     */
    List<Tran> selectAllTranDetail(Map<String,Object> map);
    /**
     * 交易的数量
     */
    int selectAllTranCount(Map<String,Object> map);
    /**
     * 插入交易
     */
    int insertCreateTran(Tran tran);
    /**
     * 根据ids删除交易
     */
    int deleteCreateTran(String[] tranIds);

    /**
     * 根据id查询交易
     */
    Tran selectTranDetailFortranId(String tranId);

    /**
     * 根据id更新
     */
    int updateTranDetailFortranId(Tran tran);
    /**
     * 根据id查询详细信息
     */
    Tran selectTranFortranId(String tranId);

    /**
     * 交易图表信息
     * @return
     */
	List<FunnelVO> selectCountOfTranGroupByStage();
}