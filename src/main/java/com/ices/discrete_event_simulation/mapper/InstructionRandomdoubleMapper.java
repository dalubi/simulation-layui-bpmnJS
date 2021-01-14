package com.ices.discrete_event_simulation.mapper;

import com.ices.discrete_event_simulation.entity.InstructionRandomdouble;
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
public interface InstructionRandomdoubleMapper extends BaseMapper<InstructionRandomdouble> {
    @Select("select max(id) from instruction_randomdouble")
    int maxId();
}
