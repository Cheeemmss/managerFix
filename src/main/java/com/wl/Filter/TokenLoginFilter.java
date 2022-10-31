package com.wl.Filter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wl.common.Result;
import com.wl.controller.dto.UserDTO;
import com.wl.custom.CustomUser;
import com.wl.entity.Menu;
import com.wl.entity.User;
import com.wl.service.MenuService;
import com.wl.service.UserService;
import com.wl.utils.ResponseUtil;
import com.wl.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.wl.common.Constants.CODE_500;

@Slf4j
public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

   private final RedisTemplate<String,Object> redisTemplate;

   private final MenuService menuService;

    public TokenLoginFilter(AuthenticationManager authenticationManager, RedisTemplate<String,Object> redisTemplate,MenuService menuService) {
        this.setAuthenticationManager(authenticationManager);
        this.setPostOnly(false);
        //指定登录接口及提交方式，可以指定任意路径
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login","POST"));
        this.redisTemplate = redisTemplate;
        this.menuService = menuService;
    }

    /**
     * 登录认证
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UserDTO userDTO = new ObjectMapper().readValue(request.getInputStream(), UserDTO.class);
            Authentication authenticationToken = new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword());
            return this.getAuthenticationManager().authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 认证成功 (登录成功)
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //principal用户信息,没有认证之前为用户名,认证后一般为用户对象
        CustomUser customUser = (CustomUser)authResult.getPrincipal();
        String token = TokenUtils.createToken(customUser.getSysUser().getId(),customUser.getSysUser().getUsername());
        //redis保存权限数据 (用于权限控制)
        Collection<GrantedAuthority> authorities = customUser.getAuthorities();
        List<String> authoritiesList = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        redisTemplate.opsForValue().set(customUser.getUsername(), authoritiesList,2L, TimeUnit.HOURS);

        // (由于前端没有改,登录进去就需要菜单和权限数据返回2,所以这里返回的是UserDTO)

        List<Menu> buttons = menuService.getButtonsByUserId(customUser.getSysUser().getId());
        List<Menu> menuTree = menuService.getMenuTree(customUser.getSysUser().getId());
        UserDTO userDTO = BeanUtil.copyProperties(customUser.getSysUser(), UserDTO.class);
        log.info(userDTO.toString());

        userDTO.setButtonList(buttons);
        userDTO.setMenuList(menuTree);
        userDTO.setToken(token);
        ResponseUtil.out(response, Result.success(userDTO));
    }

    /**
     * 认证失败
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        if(e.getCause() instanceof RuntimeException) {
            ResponseUtil.out(response, Result.error(CODE_500,e.getMessage()));
        } else {
            ResponseUtil.out(response, Result.error(CODE_500,"系统错误"));
        }
    }
}
