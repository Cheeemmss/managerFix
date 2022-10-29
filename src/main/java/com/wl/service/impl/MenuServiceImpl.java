package com.wl.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wl.entity.Menu;
import com.wl.service.MenuService;
import com.wl.mapper.MenuMapper;
import com.wl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.pattern.PathPattern;

import java.util.ArrayList;
import java.util.List;

/**
* @author 24509
* @description 针对表【sys_menu】的数据库操作Service实现
* @createDate 2022-09-19 15:45:18
*/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements MenuService{

    @Autowired
    private UserService userService;

    @Autowired
    private MenuMapper menuMapper;

    /**
     * 根据用户Id获得其被分配的菜单的 菜单树
     * @param userId
     * @return
     */
    @Override
    public List<Menu> getMenuTree(Integer userId) {

        List<Menu> menuList = null;
        //查询当前角色的所有菜单(只查菜单)
        if(userId != null){
           menuList =  userService.getMenusByUserId(userId);
        }else {
            //在超级管理员分配菜单请求该接口时走这个分支(不但会返回菜单，还会返回按钮)
            menuList = this.list();
        }

        //存放有树形结构的List
        List<Menu> menuTreeList = new ArrayList<>();
        //查询所有一级菜单
        for (Menu menu : menuList) {
            if(menu.getPid() == null){
                menuTreeList.add(menu);
            }
        }
        //为每个一级菜单查找子菜单
        for (Menu menu : menuTreeList) {
            List<Menu> childrenMenu = getChildMenu(menuList, menu.getId());
            menu.setChildrenMenu(childrenMenu);
        }
        return menuTreeList;
    }

    /**
     *
     * 查找某个菜单的子菜单
     * @param menuList  全部菜单项
     * @param pid       要查找想要查其子节点信息的 节点ID
     * @return          某个菜单的全部子菜单
     */
    public List<Menu> getChildMenu(List<Menu> menuList,Integer pid){
        //构造传入的父节点的子菜单集合
        ArrayList<Menu> childList = new ArrayList<>();
        //将该父节点的全部子菜单添加到子菜单集合中
        for (Menu menu : menuList) {
            if(pid.equals(menu.getPid())){
                childList.add(menu);
            }
        }
        //如果当前传入的父节点已经没有子节点了 --> 就直接返回
        if(!CollectionUtil.isEmpty(childList)){
            //若当前传入的父节点还有子节点 --> 则遍历所有子节点 继续递归查找子节点的子节点...
            for (Menu menu : childList) {
                List<Menu> childMenu = getChildMenu(menuList, menu.getId());
                menu.setChildrenMenu(childMenu);
            }
        }

        return childList;
    }


    //
    public List<Menu> getButtonsByUserId(Integer userId){
        return menuMapper.queryButtonsByUserId(userId);
    }

}




