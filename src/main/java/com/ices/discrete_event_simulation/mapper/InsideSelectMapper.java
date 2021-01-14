package com.ices.discrete_event_simulation.mapper;

import com.ices.discrete_event_simulation.entity.InsideSelect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ices.discrete_event_simulation.entity.InstructionSelect;
import org.apache.ibatis.annotations.Insert;
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
public interface InsideSelectMapper extends BaseMapper<InsideSelect> {
    @Insert("insert into inside_select(id,bran1Info,bran2Info,iscomplete)"
            +"values(#{id},#{bran1Info},#{bran2Info},#{iscomplete})")
    int insert(InsideSelect inside);

    @Update("update  inside_select set insideselect.iscomplete=#{iscomplete} "+
            "where insideselect.id=#{id}")
    void updateInsideSelect(InsideSelect insideSelect);

    @Update("update  instruction_select set instructionselect.branch1Id=#{branch1id},instructionselect.branch2Id=#{branch2id}"+
            " where instructionselect.selectId=#{selectId}")
    void updateInstructionSelect(InstructionSelect instructionselect);


    @Select("select * from inside_select")
    List<InsideSelect> queryAll();

    @Update("update  start_information set start_information.firstTask=#{id} "+
            "where isFirst=1")
    void updateInitialTask(int id);
}
