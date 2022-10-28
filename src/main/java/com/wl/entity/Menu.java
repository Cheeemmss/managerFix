package com.wl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 
 * @TableName sys_menu
 */
@TableName(value ="sys_menu")
@Data
public class Menu implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 路径
     */
    private String path;

    /**
     * 图标
     */
    private String icon;

    /**
     * 描述
     */
    private String description;

    /**
     * 父菜单id
     */
    private Integer pid;

    /**
     * 路由对应的vue页面
     */
    private String pagePath;

    /**
     * 菜单类型 1.普通菜单 2.菜单对应页面下的某个按钮
     */
    private Integer menuType;

    /**
     * 按钮权限(ex:用户管理添加按钮 -> btn.user.add)
     */
    private String permission;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private List<Menu> childrenMenu;


}