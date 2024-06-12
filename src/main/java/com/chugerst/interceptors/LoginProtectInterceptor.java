package com.chugerst.interceptors;

import com.chugerst.util.JwtHelper;
import com.chugerst.util.Result;
import com.chugerst.util.ResultCodeEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component//放入ioc容器，因为要JwtHelper注入到里头，必须在ioc容器里

//true放行。falue拦截
public class LoginProtectInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtHelper jwtHelper;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token= request.getHeader("token");
        boolean expiration = jwtHelper.isExpiration(token);
        if(!expiration){
            return true;
        }

        Result result =Result.build(null, ResultCodeEnum.NOTLOGIN);
        //这里不是controller层没法通过ResponseBody注解返回json
        //转换成json字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(result);
        response.getWriter().print(json);

        return false;
    }
}
