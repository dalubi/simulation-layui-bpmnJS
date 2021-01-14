package com.ices.discrete_event_simulation.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ices.discrete_event_simulation.util.AjaxResponse;
import com.ices.discrete_event_simulation.util.GlobalConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("loginSuccessHandler")
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    //把对象转化成JSON类型的解析器
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        logger.info("登录成功1");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        logger.info("登录成功2");
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        //httpServletResponse.getWriter().write("登陆成功");
        httpServletResponse.getWriter().write(
                objectMapper.writeValueAsString(
                        AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                                GlobalConfig.ResponseCode.SUCCESS.getDesc(),
                                authentication.getName())
                ));
    }
}
