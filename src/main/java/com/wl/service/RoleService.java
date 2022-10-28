package com.wl.service;

import com.wl.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 24509
* @description 针对表【sys_role】的数据库操作Service
* @createDate 2022-09-17 19:06:58
*/
public interface RoleService extends IService<Role> {

    void saveRoleMenu(Integer roleId, List<Integer> menuIds);

    void saveUserRole(Integer userId,List<Integer> roleIds);
}
