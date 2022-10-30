package com.wl.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wl.Exception.ServiceException;
import com.wl.common.Constants;
import com.wl.common.Result;
import com.wl.controller.dto.UserDTO;
import com.wl.entity.Menu;
import com.wl.entity.User;
import com.wl.listener.ExcelListener;
import com.wl.mapper.UserMapper;
import com.wl.mapper.UserRoleMapper;
import com.wl.service.MenuService;
import com.wl.service.UserService;
import com.wl.utils.TokenUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.wl.common.Constants.CODE_501;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    @Lazy
    private MenuService menuService;

    //加密盐
    private final String SALT = ")*3aWleQ";

    @Override
    public void impExcel(MultipartFile file) throws IOException {
        InputStream is = file.getInputStream();
        EasyExcel.read(is,User.class,new ExcelListener(this)).sheet().doRead();
    }

    @Override
    public Result login(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //首先根据用户名判断是否存在该用户
        queryWrapper.eq("username",userDTO.getUsername());
        User user = getOne(queryWrapper);
        if(user == null){
            return Result.error(CODE_501,"用户不存在,请先注册");
        }

        //将密码进行加密与之前注册时候加密存入数据库中的密码进行对比
        userDTO.setPassword(DigestUtils.md5Hex(userDTO.getPassword()+SALT));
        QueryWrapper<User> checkUserWrapper = new QueryWrapper<>();
        checkUserWrapper.eq("username",userDTO.getUsername())
                .eq("password",userDTO.getPassword());

        User loginUser = getOne(checkUserWrapper);
        if(loginUser == null){
            throw new ServiceException(Constants.CODE_450,"账号或密码不正确");
        }

        //登录成功过后要返回token
        UserDTO userVo = new UserDTO();
        BeanUtils.copyProperties(loginUser,userVo);
        String token = TokenUtils.createToken(loginUser.getId(),loginUser.getUsername());
        userVo.setToken(token);
        //还要返回菜单树信息以及按钮信息
        userVo.setMenuList(menuService.getMenuTree(user.getId()));
        userVo.setButtonList(menuService.getButtonsByUserId(user.getId()));
        return Result.success(userVo);

    }

    @Override
    public User register(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",userDTO.getUsername());
        User user = null;
        String password = DigestUtils.md5Hex(userDTO.getPassword()+SALT);
        userDTO.setPassword(password);

        user = getOne(queryWrapper);

        if(user != null){
            throw new ServiceException(Constants.CODE_450,"该用户已存在");
        }
        user = new User();
        save(user);
        BeanUtils.copyProperties(userDTO,user);

        return user;
    }

    @Override
    public boolean getUserInfoByName(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",userDTO.getUsername());
        User user = getOne(queryWrapper);
        return user != null;
    }

    public User getUserInfoByName(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        User user = getOne(queryWrapper);
        return user;
    }

    /**
     * 根据 userId 来获取他所有的菜单
     * @param userId
     * @return
     */
    public List<Menu> getMenusByUserId(Integer userId){
        return userRoleMapper.queryMenusByUserId(userId);
    }
}
