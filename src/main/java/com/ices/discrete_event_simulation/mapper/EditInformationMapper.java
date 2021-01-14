package com.ices.discrete_event_simulation.mapper;

import com.ices.discrete_event_simulation.entity.EditInformation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zth
 * @since 2020-11-05
 */
public interface EditInformationMapper extends BaseMapper<EditInformation> {
    @Select("select federateId from federate where federateName = #{federateName}")
    Integer findFederateIdByFederateName(@Param("federateName") String federateName);

    @Select("select interactionId from interaction where interactionName = #{interactionName}")
    Integer findInteractionIdByInteractionName(@Param("interactionName") String interactionName);

    @Insert("insert into edit_information(fromFederateId,fromInteractionId,taskId) " +
            "values(#{fromFederateId},#{fromInteractionId},#{taskId})")
    int insert(EditInformation editInformation);

}
