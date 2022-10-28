package com.wl.mapper;

import com.wl.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author 24509
* @description 针对表【sys_menu】的数据库操作Mapper
* @createDate 2022-09-19 15:45:18
* @Entity com.wl.entity.Menu
*/
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> queryButtonsByUserId(Integer userId);
}




