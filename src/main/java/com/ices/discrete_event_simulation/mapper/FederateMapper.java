package com.ices.discrete_event_simulation.mapper;

import com.ices.discrete_event_simulation.dto.federateDTO;
import com.ices.discrete_event_simulation.entity.Federate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultType;
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
public interface FederateMapper extends BaseMapper<Federate> {

    @Select("select federateId from federate where federateName = #{federateName}")
    int findFederateIdByName(String federateName);

    @Select("select federateName from federate where federateId = #{federateId}")
    String findFederateNameById(Integer federateId);

    @Insert("insert into federate(federateName,publishInteractionIds,subscribeInteractionIds) " +
            "values(#{federateName},#{publishInteractionIds},#{subscribeInteractionIds})")
    int insert(Federate federate);

    @Select("select federateId,federateName from federate")
    @ResultType(value= federateDTO.class)
    List<federateDTO> allFederateInfo();

}
