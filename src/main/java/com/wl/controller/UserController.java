package com.wl.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wl.common.Constants;
import com.wl.common.Result;
import com.wl.controller.dto.UserDTO;
import com.wl.entity.Menu;
import com.wl.entity.Role;
import com.wl.entity.User;
import com.wl.mapper.UserRoleMapper;
import com.wl.service.MenuService;
import com.wl.service.UserService;
import com.wl.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private MenuService menuService;


    @GetMapping("/page")
    public IPage<User> findPage(Integer pageNo,
                                Integer pageSize,
                                String username,
                                String email,
                                String address,
                                HttpServletRequest request){
       Page<User> page = new Page<>(pageNo,pageSize);
       QueryWrapper<User> queryWrapper = new QueryWrapper<>();
       queryWrapper.like(StringUtils.isNotBlank(username),"username",username)
               .like(StringUtils.isNotBlank(email),"email",email)
               .like(StringUtils.isNotBlank(address),"address",address)
               .orderByDesc("id");
//        String userId = TokenUtils.getTokenUserId(request.getHeader("token"));
//        User user = userService.getById(Integer.parseInt(userId));
//       log.info(user.toString());

        //这里是后面补的 本来是应该用一句sql写的 搞复杂了
        Page<User> userPage = userService.page(page, queryWrapper);
        List<User> users = userPage.getRecords();
        for (User user : users) {
            List<Role> roles = userRoleMapper.queryRolesNameByUserId(user.getId());
            user.setRoleList(roles);
        }
        return userPage;
    }

    @PostMapping("/user")
    public boolean saveUser(@RequestBody User user){
        return userService.saveOrUpdate(user);
    }

    @DeleteMapping("/user/{id}")
    public boolean deleteUser(@PathVariable("id") Integer id){
        return userService.removeById(id);
    }

    @DeleteMapping("/user/batch")
    public boolean deleteUserBatch(@RequestBody List<Integer> userIds){
        return userService.removeBatchByIds(userIds);
    }

    @PostMapping("/user/export")
    public void exportExcel(HttpServletResponse response,@RequestBody List<Integer> userIds){
        List<User> userList = userService.listByIds(userIds);
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String filename = URLEncoder.encode("用户表"+UUID.randomUUID(),"UTF-8");
//            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");// 暴露Content-Disposition使其可以被访问
            response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xlsx");
            EasyExcel.write(response.getOutputStream(),User.class)
                    .sheet("sheet1")
                    .doWrite(userList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/user/import")
    public void impExcel(MultipartFile file) throws IOException {
           userService.impExcel(file);
    }

    //使用了springSecurity该接口中的登录逻辑就没用了
    @Deprecated
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO){
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if(StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)){
            return userService.login(userDTO);
        }
        return Result.error(Constants.CODE_400,"请求参数错误");
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO){
        String username = userDTO.getUsername();
        if(StringUtils.isNotBlank(username)){
            User user = userService.register(userDTO);
            return Result.success(user);
        }
        return Result.error(Constants.CODE_400,"请求参数错误");
    }

    //注册逻辑有问题 将就写个这个方法
    @PostMapping("/checkRepeat")
    public Result checkRepeat(@RequestBody UserDTO userDTO){
        String username = userDTO.getUsername();
        if(StringUtils.isNotBlank(username)){
            boolean isExsit = userService.getUserInfoByName(userDTO);
            return Result.success(isExsit);
        }
        return Result.error(Constants.CODE_400,"请求参数错误");
    }

    @GetMapping("/user/{username}")
    public Result getUserByUsername(@PathVariable("username") String username){
        if(StringUtils.isNotBlank(username)){
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username",username);
            User user = userService.getOne(queryWrapper);
            return Result.success(user);
        }
        return Result.error(Constants.CODE_400,"请求参数错误");
    }

    @GetMapping("/user/info")
    public Result getUserInfoByUserId(HttpServletRequest request){
        String token = request.getHeader("token");
        Integer userId = TokenUtils.getUserId(token);
        List<Menu> menuTree = menuService.getMenuTree(userId);
        List<Menu> buttons = menuService.getButtonsByUserId(userId);
        HashMap<String, Object> result = new HashMap<>();
        result.put("menuTree",menuTree);
        result.put("buttons",buttons);
        return Result.success(result);
    }

}
