package com.wl.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wl.entity.MyFile;
import com.wl.entity.User;
import com.wl.mapper.FileMapper;
import com.wl.service.UserService;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
public class MyFileController {

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private UserService userService;

    @PostMapping("/file/upload")
    public String upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();

        File fileUploadPathDir = new File(fileUploadPath);
        if(!fileUploadPathDir.exists()){
            fileUploadPathDir.mkdirs();
        }

        String filename = UUID.randomUUID() + StrUtil.DOT + type;
        File uploadFile = new File(fileUploadPath + filename);

        String md5 = SecureUtil.md5(file.getInputStream());
        //从数据库里面查(根据md5 若是相同的文件,获取的md5是相同的) 是否是重复的文件 如果是相同的文件就不再往硬盘上存了 只是记录一下这次上传的文件的名称 存一条数据 (数据库中多条数据指向同一硬盘上文件)
        MyFile theFile = null;
        List<MyFile> urlList = fileMapper.selectList(new QueryWrapper<MyFile>().eq("md5", md5));
        if(urlList.size() != 0){
            theFile = urlList.get(0);
        }

        String url = null;
        if(theFile != null){
            url = theFile.getUrl();
        }else {
            url = "http://localhost:8080/file/" + filename;
            file.transferTo(uploadFile);
        }

        MyFile myFile = new MyFile();
        myFile.setName(originalFilename);
        myFile.setType(type);
        myFile.setSize(size/1024);
        myFile.setUrl(url);
        myFile.setMd5(md5);
        fileMapper.insert(myFile);

        return url;
    }

    //做头像上传的 虽然有点奇怪
    @PostMapping("/file/avatar")
    public String avatarUpload(MultipartFile file,  String username) throws IOException {
        File avatarDir = new File(fileUploadPath);
        if(!avatarDir.exists()){
            avatarDir.mkdirs();
        }
        String fileName = UUID.randomUUID() + StrUtil.DOT + FileUtil.extName(file.getOriginalFilename());
        file.transferTo(new File(fileUploadPath + fileName));
        String url = "http://localhost:8080/file/" + fileName;
        userService.update(new UpdateWrapper<User>().eq("username",username).set("avatar_url",url));
        return url;
    }

//    http://localhost:8080/file/{filename}
    @GetMapping("/file/{fileName}")
    public void download(@PathVariable("fileName") String fileName, HttpServletResponse response) throws IOException {
        File uploadFile = new File(fileUploadPath + fileName);
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition","attachment;filename=" + URLEncoder.encode(fileName,"UTF-8"));
        response.setContentType("application/octet-stream");

        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();

    }
}
