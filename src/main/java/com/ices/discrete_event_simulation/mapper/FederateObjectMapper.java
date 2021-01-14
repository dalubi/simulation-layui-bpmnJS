package com.ices.discrete_event_simulation.mapper;

import com.ices.discrete_event_simulation.dto.federateObjectDTO;
import com.ices.discrete_event_simulation.entity.FederateObject;
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
public interface FederateObjectMapper extends BaseMapper<FederateObject> {

    @Select("select max(objectId) from federate_object")
    int maxId();

    @Select("select objectId,objectName from federate_object")
    @ResultType(value= federateObjectDTO.class)
    List<federateObjectDTO> queryAll();

    @Select("select objectName from federate_object")
    List<String> queryAllName();

    @Select("select * from federate_object where objectId=#{instanceId}")
    FederateObject queryInstanceById(int instanceId);

    @Select("select objectId from federate_object where objectName=#{objectName}")
    int queryObjectByName(String objectName);
}
