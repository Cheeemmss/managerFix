package com.wl.custom;

import com.wl.Exception.ServiceException;
import com.wl.common.Constants;
import com.wl.entity.Menu;
import com.wl.entity.User;
import com.wl.service.MenuService;
import com.wl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.wl.common.Constants.CODE_501;

@Component
public class UserDetailsServiceImpl implements UserDetailsService{

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getUserInfoByName(username);
        if(user == null){
            throw new ServiceException(CODE_501,"用户不存在,请先注册");
        }
        List<Menu> buttons = menuService.getButtonsByUserId(user.getId());
        List<String> buttonList = buttons.stream().map(btn -> btn.getPermission().trim()).collect(Collectors.toList());
        List<SimpleGrantedAuthority> permissionList = new ArrayList<>();
        for (String button : buttonList) {
            permissionList.add(new SimpleGrantedAuthority(button));
        }
        return new CustomUser(user,permissionList);
    }
}
