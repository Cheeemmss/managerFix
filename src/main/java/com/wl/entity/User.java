package com.wl.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@TableName(value = "sys_user")
public class User {

    @TableId(type = IdType.AUTO)
    @ExcelProperty(index = 0)
    private Integer id;

    @ExcelProperty(value = "用户名",index = 1)
    private String username;

    @JsonIgnore
    @ExcelIgnore
    private String password;

    @ExcelProperty(value = "昵称",index = 2)
    private String nickname;

    @ExcelProperty(value = "电子邮箱",index = 3)
    private String email;

    @ExcelProperty(value = "电话号码",index = 4)
    private String phone;

    @ExcelProperty(value = "地址",index = 5)
    private String address;

    private String avatarUrl;

    /**
     * 该用户所能查看的菜单
     */
    @TableField(exist = false)
    private List<Menu> menuList;

    /**
     * 用户权限 可以返回给前端展示
     */
    @TableField(exist = false)
    private List<Role> roleList;

}
