package com.wl.custom;

import com.wl.entity.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;


public class CustomUser extends User {

    private com.wl.entity.User sysUser;

    public CustomUser(com.wl.entity.User sysUser, Collection<? extends GrantedAuthority> authorities) {
        super(sysUser.getUsername(), sysUser.getPassword(), authorities);
        this.sysUser = sysUser;
    }

    public com.wl.entity.User getSysUser() {
        return sysUser;
    }

    public void setSysUser(com.wl.entity.User sysUser) {
        this.sysUser = sysUser;
    }


}
