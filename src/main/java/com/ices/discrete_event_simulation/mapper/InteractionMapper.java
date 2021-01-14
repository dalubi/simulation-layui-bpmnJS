package com.ices.discrete_event_simulation.mapper;

import com.ices.discrete_event_simulation.entity.Interaction;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zth
 * @since 2020-11-05
 */
public interface InteractionMapper extends BaseMapper<Interaction> {
    //BPMN中抽取到一个交互类，取到他的名字，放入数据库
    @Insert("insert into interaction (interactionName) values (#{interactionName})")
    void insertByName(String interactionName);


    @Select("select interactionId from interaction where interactionName = #{interactionName}")
    int getInteractionIdByName(String interactionName);

    @Select("select interactionName from interaction where interactionId = #{interactionId}")
    String getInteractionNameById(int interactionId);

    //更新这个交互类有什么参数
    @Update("update interaction set interaction.containParameterIds = #{containParameterIds}" +
            "where interactionId = #{interactionId}")
    void giveParamsToInteractionById(@Param("interactionId") int interactionId,
                                     @Param("containParameterIds") String containParameterIds);

    @Select("select * from interaction")
    @ResultType(value = Interaction.class)
    List<Interaction> getAllInteractions();
}
