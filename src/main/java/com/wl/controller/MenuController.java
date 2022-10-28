package com.wl.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wl.common.Result;
import com.wl.entity.Dict;
import com.wl.entity.Menu;
import com.wl.entity.Role;
import com.wl.mapper.DictMapper;
import com.wl.service.MenuService;;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/menu")
@Slf4j
public class MenuController {
    
    @Autowired
    private MenuService menuService;

    @Autowired
    private DictMapper dictMapper;

    @GetMapping("/page")
    public Result findPage(Integer pageNo,
                           Integer pageSize,
                           String name,
                           HttpServletRequest request){
        Page<Menu> page = new Page<>(pageNo,pageSize);
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name),"name",name)
                .orderByDesc("id");

        Page<Menu> rolePage = menuService.page(page, queryWrapper);
        return Result.success(rolePage);
    }

    @PostMapping("/save")
    public Result saveUser(@RequestBody Menu menu){
        boolean saveOrUpdate = menuService.saveOrUpdate(menu);
        return Result.success(saveOrUpdate);
    }

    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable("id") Integer id){
        boolean remove = menuService.removeById(id);
        return Result.success(remove);
    }

    @DeleteMapping("/batch")
    public Result deleteUserBatch(@RequestBody List<Integer> menuIds){
        boolean batch = menuService.removeBatchByIds(menuIds);
        return Result.success(batch);
    }

    @GetMapping("/menuTree")
    public Result getMenuTree(@RequestParam(value = "userId",required = false)Integer userId){
//       log.info("userId:{}",userId);
       List<Menu> menuList  =  menuService.getMenuTree(userId);
       return Result.success(menuList);
    }

    @GetMapping("/icons")
    public Result getIcons(){
           QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
           queryWrapper.eq("type","icon");
           List<Dict> icons = dictMapper.selectList(queryWrapper);
           return Result.success(icons);
    }
}
