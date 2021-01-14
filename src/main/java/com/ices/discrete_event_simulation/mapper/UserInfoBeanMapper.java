package com.ices.discrete_event_simulation.mapper;

import com.ices.discrete_event_simulation.entity.UserInfoBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserInfoBeanMapper {

    @Select("select * from user where username = #{username}")
    UserInfoBean selectByUsername(@Param("username") String username);

}
