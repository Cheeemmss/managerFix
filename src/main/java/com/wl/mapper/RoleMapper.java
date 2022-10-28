package com.wl.mapper;

import com.wl.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 24509
* @description 针对表【sys_role】的数据库操作Mapper
* @createDate 2022-09-17 19:06:58
* @Entity com.wl.entity.Role
*/
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

}




