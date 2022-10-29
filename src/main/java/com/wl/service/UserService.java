package com.wl.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wl.common.Result;
import com.wl.controller.dto.UserDTO;
import com.wl.entity.Menu;
import com.wl.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService extends IService<User> {

    void impExcel(MultipartFile file) throws IOException;

    Result login(UserDTO userDTO);

    User register(UserDTO userDTO);

    boolean getUserInfoByName(UserDTO userDTO);

    User getUserInfoByName(String userName);

    public List<Menu> getMenusByUserId(Integer userId);
}
