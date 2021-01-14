package com.ices.discrete_event_simulation.security;

import com.ices.discrete_event_simulation.mapper.UserInfoBeanMapper;

import com.ices.discrete_event_simulation.pojo.UserInfoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserInfoBeanMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        String passWord = passwordEncoder().encode("111");//密码必须是秘文，而不应该是明文
//
//        //启动了自动运行，判断用户名和密码，设置用户的角色
//        return new User(username,
//                passWord,
//                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ACTIVITI_USER"));
        UserInfoBean userInfoBean = mapper.selectByUsername(username);
        if(userInfoBean==null){
            throw new UsernameNotFoundException("数据库中无此用户");
        }
        return userInfoBean;
    }

    @Bean
    //@Bean：返回的是一个生产好的，代替AutoWird的实例对象
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
