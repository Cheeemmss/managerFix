package com.wl.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.org.apache.regexp.internal.RE;
import com.wl.common.Result;
import com.wl.entity.Role;
import com.wl.entity.RoleMenu;
import com.wl.entity.User;
import com.wl.entity.UserRole;
import com.wl.mapper.RoleMenuMapper;
import com.wl.mapper.UserRoleMapper;
import com.wl.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;


    @GetMapping("/page")
    public Result findPage(Integer pageNo,
                           Integer pageSize,
                           String name,
                           HttpServletRequest request){
        Page<Role> page = new Page<>(pageNo,pageSize);
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name),"name",name);

        Page<Role> rolePage = roleService.page(page, queryWrapper);
        return Result.success(rolePage);
    }

    @PreAuthorize("hasAuthority('btn.role.edit')")
    @PostMapping("/save")
    public Result saveUser(@RequestBody Role role){
        boolean saveOrUpdate = roleService.saveOrUpdate(role);
        return Result.success(saveOrUpdate);
    }

    @PreAuthorize("hasAuthority('btn.role.delete')")
    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable("id") Integer id){
        boolean remove = roleService.removeById(id);
        return Result.success(remove);
    }

    @DeleteMapping("/batch")
    public Result deleteUserBatch(@RequestBody List<Integer> roleIds){
        boolean batch = roleService.removeBatchByIds(roleIds);
        return Result.success(batch);
    }

    @PostMapping("/allocationMenu/{roleId}")
    public Result saveRoleMenu(@PathVariable("roleId") Integer roleId,@RequestBody List<Integer> menuIds){
          roleService.saveRoleMenu(roleId,menuIds);
          return Result.success();
    }

    /**
     * 根据 roleId 获取该角色被分配的 menu
     * @param roleId
     * @return
     */
    @GetMapping("/roleMenu/{roleId}")
    public Result getRoleMenu(@PathVariable("roleId") Integer roleId){
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId,roleId);
        List<RoleMenu> roleMenuList = roleMenuMapper.selectList(queryWrapper);
        return Result.success(roleMenuList);
    }

    /**
     * 根据 userId 获取该角色被分批额的role
     * @param userId
     * @return
     */
    @GetMapping("{userId}")
    public Result getRolesByUserId(@PathVariable("userId") Integer userId){
        List<UserRole> userRoles = userRoleMapper.selectList(new QueryWrapper<UserRole>().eq("user_id", userId));
        List<Integer> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        return Result.success(roleIds);
    }

    @PostMapping("/allocateRole/{userId}")
    public Result saveUserRole(@PathVariable("userId") Integer userId,@RequestBody List<Integer> roleIds){
        roleService.saveUserRole(userId,roleIds);
        return Result.success();
    }

}
