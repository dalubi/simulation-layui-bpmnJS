package com.ices.discrete_event_simulation.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ices.discrete_event_simulation.entity.Task;
import org.apache.ibatis.annotations.*;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zth
 * @since 2020-11-05
 */
public interface TaskMapper extends BaseMapper<Task> {
    @Insert("insert into task(taskId) values(#{taskId})")
    void insertTaskId(String taskId);

    @Insert("insert into task(InstructionSequence,InstructionIds,nextTaskId) " +
            "values(#{InstructionSequence},#{InstructionIds},#{nextTaskId})")
    void insertTask(Task task);

    //每个任务用来构造指令序列用的
    @Select("select instructionSequence from task where taskId=#{taskId}")
    String findInstructionSequenceById(String taskId);

    @Select("select instructionIds from task where taskId=#{taskId}")
    String findInstructionIdsById(String taskId);

    //更新指令序列
    @Update("update task set task.InstructionSequence = #{InstructionSequence} ," +
            "task.InstructionIds = #{InstructionIds} where taskId = #{taskId}")
    void updateSequenceAndinstructionIdsById(@Param("InstructionSequence") String InstructionSequance,
                                             @Param("InstructionIds") String InstructionIds,
                                             @Param("taskId") String taskId);


    //根据一个taskId，得到这个指令的任务序列
    @Select("select * from task where taskId = #{taskId}")
    @ResultType(value = Task.class)
    Task findTaskInfoById(String taskId);

    @Select("select max(taskId) from task")
    int maxId();

    //根据Id更新指令ID
    @Update("update task set task.InstructionIds = #{InstructionIds} where taskId = #{taskId}")
    void updateInstructionIdsById(@Param("InstructionIds") String InstructionIds, @Param("taskId") int taskId);
}
