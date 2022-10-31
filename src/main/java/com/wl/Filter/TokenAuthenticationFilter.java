package com.wl.Filter;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wl.common.Result;
import com.wl.utils.ResponseUtil;
import com.wl.utils.TokenUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.wl.common.Constants.CODE_401;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private RedisTemplate<String,Object> redisTemplate;

    public TokenAuthenticationFilter() {

    }

    public TokenAuthenticationFilter(RedisTemplate<String,Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        //如果是登录接口，直接放行
        if("/login".equals(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }
        if("/file".equals(request.getRequestURI()) || "/file/avatar".equals(request.getRequestURI())){
            chain.doFilter(request,response);
            return;
        }

        //每次请求都会去获取用户信息和权限信息(根据token从redis中取)
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if(null != authentication) {
            //将用户信息和权限信息保存到ContextHolder中 权限信息可在接口中做权限判断
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //登录过后,这里放行会去执行目标接口方法
            chain.doFilter(request, response);
        } else {
            ResponseUtil.out(response, Result.error(CODE_401,"无token或token已过期"));
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // token置于header里
        String token = request.getHeader("token");
        if (!StringUtils.isEmpty(token)) {
            String username = TokenUtils.getUsername(token);
            //这里还要查到权限用于后端的权限控制
            if(StringUtils.isNotBlank(username)){
                Object authoritiesObj = redisTemplate.opsForValue().get(username);
                List<String> authoritiesList = JSONUtil.toList(JSONUtil.toJsonStr(authoritiesObj), String.class);
                List<SimpleGrantedAuthority> authorities = authoritiesList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                if (!StringUtils.isEmpty(username)) {
                    return new UsernamePasswordAuthenticationToken(username, null, authorities);
                }
            }
        }
        return null;
    }
}