package com.wl.mapper;

import com.wl.entity.Menu;
import com.wl.entity.Role;
import com.wl.entity.UserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 24509
* @description 针对表【sys_user_role(用户-角色 关联表)】的数据库操作Mapper
* @createDate 2022-09-25 17:07:15
* @Entity com.wl.entity.UserRole
*/
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    List<Menu> queryMenusByUserId(@Param("userId") Integer userId);

    List<Role> queryRolesNameByUserId(@Param("userId") Integer userId);
}




