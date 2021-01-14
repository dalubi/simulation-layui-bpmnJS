package com.ices.discrete_event_simulation.mapper;

import com.ices.discrete_event_simulation.entity.InstructionCreatestringbuilder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zth
 * @since 2020-11-05
 */
public interface InstructionCreatestringbuilderMapper extends BaseMapper<InstructionCreatestringbuilder> {
    @Select("select max(id) from instruction_createstringbuilder")
    int maxId();
}
