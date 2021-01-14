package com.ices.discrete_event_simulation.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ices.discrete_event_simulation.entity.InstructionListsize;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zth
 * @since 2020-11-05
 */
public interface InstructionListsizeMapper extends BaseMapper<InstructionListsize> {
    @Select("select max(id) from instructioncom.ices.discrete_event_simulationlistsize")
    int maxId();
}
