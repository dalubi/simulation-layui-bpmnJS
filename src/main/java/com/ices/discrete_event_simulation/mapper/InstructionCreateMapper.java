package com.ices.discrete_event_simulation.mapper;

import com.ices.discrete_event_simulation.entity.InstructionCreate;
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
public interface InstructionCreateMapper extends BaseMapper<InstructionCreate> {

    @Select("select max(id) from instruction_create")
    int maxId();

    @Select("select objectId from federate_Object where objectName = #{federateObject}")
    int findObjectIdByName(@Param("federateObject") String federateObject);

    @Insert("insert into instruction_create(id,typeInformation,actualInformation,outName) " +
            "values(#{id},#{typeInformation},#{actualInformation},#{outName})")
    int insert(InstructionCreate create);

    @Insert("insert into instruction_create(id,outName) " +
            "values(#{id},#{outName})")
    void insertNullObject(InstructionCreate create);
}
