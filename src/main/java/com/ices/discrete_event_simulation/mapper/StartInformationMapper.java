package com.ices.discrete_event_simulation.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ices.discrete_event_simulation.entity.StartInformation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zth
 * @since 2020-11-05
 */
public interface StartInformationMapper extends BaseMapper<StartInformation> {
    @Insert("insert into start_information(federateId,isFirst,firstTask,initialInstanceTask,updateInstanceTask) " +
            "values(#{federateId},#{isFirst},#{firstTask},#{initialInstanceTask},#{updateInstanceTask})")
    int insert(StartInformation startinformation);

    @Insert("insert into start_information(federateId,isFirst,firstTask) " +
            "values(#{federateId},#{isFirst},#{firstTask})")
    void insert2(StartInformation startinformation);

    @Select("select federateId from start_information where isFirst=-1")
    List<Integer> queryExternalId();

    @Select("select listIds from start_information where federateId=#{federateId}")
    String queryFederateListById(int federateId);

    @Update("update start_information set start_information.listIds = #{listIds}" +
            " where federateId = #{federateId}")
    void updateFederateListByFederateId(@Param("federateId") int federateId,
                                        @Param("listIds") String listIds);


    @Select("select variableIds from start_information where federateId=#{federateId}")
    String queryFederateVariableById(int federateId);

    @Update("update start_information set start_information.variableIds = #{variableIds}" +
            " where federateId = #{federateId}")
    void updateFederateVariableByFederateId(@Param("federateId") int federateId,
                                            @Param("variableIds") String variableIds);


    @Select("select objects from start_information where federateId=#{federateId}")
    String queryFederateObjectsById(int federateId);

    @Select("select listIds from start_information where federateId=#{federateId}")
    String queryFederateListIdsById(int federateId);

    @Update("update start_information set start_information.objects = #{objects}" +
            " where federateId = #{federateId}")
    void updateFederateObjectByFederateId(@Param("federateId") int federateId,
                                          @Param("objects") String objects);

    @Update("update start_information set start_information.updateInstanceTask = #{updateInstanceTask}" +
            " where federateId = #{federateId}")
    void updateUpdateInstanceByFederateId(@Param("federateId") int federateId,
                                          @Param("updateInstanceTask") int updateInstanceTask);


    @Update("update start_information set start_information.initialInstanceTask = #{initialInstanceTask}" +
            " where federateId = #{federateId}")
    void updateInitialInstanceTaskByFederateId(@Param("federateId") int federateId,
                                               @Param("initialInstanceTask") int initialInstanceTask);

    @Update("update start_information set start_information.ownEventTask = #{ownEventTask}" +
            " where federateId = #{federateId}")
    void updateOwnEventTaskByFederateId(@Param("federateId") int federateId,
                                        @Param("ownEventTask") int ownEventTask);
}
