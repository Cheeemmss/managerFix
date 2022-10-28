package com.wl.service;

import com.wl.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 24509
* @description 针对表【sys_menu】的数据库操作Service
* @createDate 2022-09-19 15:45:18
*/
public interface MenuService extends IService<Menu> {

    List<Menu> getMenuTree(Integer userId);

    List<Menu> getButtonsByUserId(Integer userId);
}
