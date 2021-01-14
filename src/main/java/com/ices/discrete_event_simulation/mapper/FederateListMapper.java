package com.ices.discrete_event_simulation.mapper;

import com.ices.discrete_event_simulation.entity.FederateList;
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
public interface FederateListMapper extends BaseMapper<FederateList> {
    @Insert("insert into federate_list(objectName,listName)"
            +"values(#{objectName},#{listName})")
    int insert(FederateList federatelist);

    @Select("select max(listId) from federate_list")
    int maxId();

    @Select("select listName from federate_list")
    List<String> findAllListName() ;
}
