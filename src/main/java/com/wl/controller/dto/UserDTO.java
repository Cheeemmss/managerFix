package com.wl.controller.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wl.entity.Menu;
import com.wl.entity.Role;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private String username;
    private String password;
    private String nickname;
    private String avatarUrl;
    private String token;

    private List<Menu> menuList;
    private List<Menu> buttonList;
}
