package com.ices.discrete_event_simulation.mapper;

import com.ices.discrete_event_simulation.entity.HelpVariableDescription;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zth
 * @since 2020-11-05
 */
public interface HelpVariableDescriptionMapper extends BaseMapper<HelpVariableDescription> {
    @Insert("insert into help_variable_description(parameterName,parameterType,variableDescription,federateId)"
            +"values(#{parameterName},#{parameterType},#{variableDescription},#{federateId})")
    int insert(HelpVariableDescription helpVariable);

    @Select("select * from help_variable_description")
    List<HelpVariableDescription> queryAll();

    @Select("select * from help_variable_description where federateId=#{federateId}")
    List<HelpVariableDescription> queryByFederateId(Integer federateId);
}
