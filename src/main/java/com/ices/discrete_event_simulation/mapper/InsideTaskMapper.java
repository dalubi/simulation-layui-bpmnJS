package com.ices.discrete_event_simulation.mapper;

import com.ices.discrete_event_simulation.entity.InsideTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface InsideTaskMapper extends BaseMapper<InsideTask> {

    @Update("update inside_task set inside_task.iscomplete=#{iscomplete}"+
            "where inside_task.insidetaskId=#{insidetaskId}")
    void updateinsidetask(InsideTask insidetask);

    @Select("select * from inside_task")
    List<InsideTask> queryAll();

    @Update("update inside_task set inside_task.iscomplete=#{iscomplete}"+
            "where inside_task.insidetaskId=#{insidetaskId}")
    void update(@Param("insidetaskId") String insidetaskId,
                @Param("iscomplete") String iscomplete);

    @Select("select iscomplete from inside_task where insidetaskId=#{insidetaskId}")
    String checkCompleteById(@Param("insidetaskId") String insidetaskId);

}
