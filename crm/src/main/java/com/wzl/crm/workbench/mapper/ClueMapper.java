package com.wzl.crm.workbench.mapper;

import com.wzl.crm.workbench.domain.Clue;
import com.wzl.crm.workbench.domain.FunnelVO;

import java.util.List;
import java.util.Map;

public interface ClueMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Thu Mar 17 15:29:56 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Thu Mar 17 15:29:56 CST 2022
     */
    int insert(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Thu Mar 17 15:29:56 CST 2022
     */
    int insertSelective(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Thu Mar 17 15:29:56 CST 2022
     */
    Clue selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Thu Mar 17 15:29:56 CST 2022
     */
    int updateByPrimaryKeySelective(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Thu Mar 17 15:29:56 CST 2022
     */
    int updateByPrimaryKey(Clue record);

    /**
     * 新增线索
     * @param clue 新增的线索
     * @return 新增条数
     */
    int insertClue(Clue clue);

    /**
     * 根据条件分页查询线索列表
     * @param map 查询条件
     * @return 查询到的线索
     */
    List<Clue> selectClueByConditionForPage(Map<String, Object> map);

    /**
     * 根据条件查询线索总条数
     * @param map 查询条件
     * @return 线索总条数
     */
    int selectCountOfClueByCondition(Map<String, Object> map);

    /**
     * 通过id删除选则的线索
     * @param clueIds 线索id数组
     * @return 删除的条数
     */
    int deleteClueByIds(String[] clueIds);

    /**
     * 通过id查询线索
     * @param id 线索id
     * @return 对应id的线索
     */
    Clue selectClueById(String id);

    /**
     * 更新对应线索的数据
     * @param clue 更新的线索
     * @return 更新条数
     */
    int updateClue(Clue clue);

    /**
     * 通过id查询线索详情
     * @param id 线索id
     * @return 对应id的线索
     */
    Clue selectClueForDetailById(String id);

    /**
     * 线索图表
     */
    List<Integer> selectCountOfClueGroupByClueStage();
    List<String>selectClueStageOfClueGroupByClueStage();

}