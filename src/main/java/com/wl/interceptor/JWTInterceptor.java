package com.wl.interceptor;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wl.Exception.ServiceException;
import com.wl.common.Constants;
import com.wl.entity.User;
import com.wl.service.UserService;
import com.wl.utils.TokenUtils;
import org.apache.poi.xwpf.usermodel.TOC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class JWTInterceptor implements HandlerInterceptor {

//    @Autowired
//    private UserService userService;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//
//        //若有请求不是映射到方法则直接通过
//        if(!(handler instanceof HandlerMethod)){
//            return true;
//        }
//        //看下有没有token
//        String token = request.getHeader("token");
//        if(StringUtils.isBlank(token)){
//            throw new ServiceException(Constants.CODE_401,"无token,请重新登录");
//        }
//
//        //token中不存在ID信息的情况
//        String userId = null;
//        try {
//            userId = JWT.decode(token).getAudience().get(0);
//        }catch (JWTDecodeException E){
//            throw new ServiceException(Constants.CODE_401,"token验证失败,请重新登录");
//        }
//
//        User user = userService.getById(Integer.parseInt(userId));
//        if(user == null){
//            throw new ServiceException(Constants.CODE_401,"用户名不存在,请重新登录");
//        }
//
//        try {
//            TokenUtils.verify(token);  //验证token是否过期 算法是否一致...
//        }catch (JWTVerificationException e){
//            throw new ServiceException(Constants.CODE_401,"token验证失败,请重新登录");
//        }
//        return true;
//    }
}

