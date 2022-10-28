package com.wl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wl.entity.Menu;
import com.wl.entity.Role;
import com.wl.entity.RoleMenu;
import com.wl.entity.UserRole;
import com.wl.mapper.RoleMenuMapper;
import com.wl.mapper.UserRoleMapper;
import com.wl.service.RoleService;
import com.wl.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author 24509
* @description 针对表【sys_role】的数据库操作Service实现
* @createDate 2022-09-17 19:06:58
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{


    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;


    /**
     * 绑定角色与菜单之间的关系
     * @param roleId
     * @param menuIds
     */
    @Override
    @Transactional
    public void saveRoleMenu(Integer roleId, List<Integer> menuIds) {
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId,roleId);
        //先删除所有的role-menu绑定关系 再重新绑定
        roleMenuMapper.delete(queryWrapper);

        for (Integer menuId : menuIds) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuMapper.insert(roleMenu);
        }
    }

    /**
     * 绑定user与角色之间的关系
     * @param userId
     * @param roleIds
     */
    @Override
    @Transactional
    public void saveUserRole(Integer userId, List<Integer> roleIds) {
        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id",userId);
        userRoleMapper.delete(userRoleQueryWrapper);

        for (Integer roleId : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRoleMapper.insert(userRole);
        }
    }
}




