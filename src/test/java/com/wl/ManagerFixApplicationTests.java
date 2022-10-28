package com.wl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.wl.entity.User;
import com.wl.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class ManagerFixApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testList(){
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }

    @Test
    public void testInsert(){
//        User user = new User();
//        user.setName("张三");
//        user.setAge(23);
//        user.setEmail("111111@qq.com");
//        int insert = userMapper.insert(user);
//        System.out.println(user.getUid());
    }

    @Test
    public void test3(){
        int i = userMapper.deleteById(1562054540394156034L);
        System.out.println(i);
        Map<String,Object> map = new HashMap<>();
        map.put("name","张三");
        map.put("age",23);
        userMapper.deleteByMap(map);
        List<Long> list = Arrays.asList(1L, 2L, 3L);
        userMapper.deleteBatchIds(list);
    }

    @Test
    public void test4(){
//        User user = new User();
//        user.setUid(4L);
//        user.setName("李四");
//        userMapper.updateById(user);
    }

    @Test
    public void test5(){
        List<Long> list = Arrays.asList(1L, 2L, 3L);
        List<User> users = userMapper.selectBatchIds(list);
        users.forEach(System.out::println);
//          Map<String,Object> map = new HashMap<>();
//          map.put("name","Jack");
//          map.put("age",20);
//          List<User> users = userMapper.selectByMap(map);
//          users.forEach(System.out::println);
//          userMapper.selectList(new QueryWrapper<>());
//        Map<String, Object> map = userMapper.selectMapById(1L);
//        System.out.println(map);
    }

    @Test
    public void test6(){
        QueryWrapper<User> queryWrapper =  new QueryWrapper<>();
        queryWrapper.like("name","a")
                .between("age",20,30)
                .isNotNull("email");
        List<User> users = userMapper.selectList(queryWrapper);
    }

    @Test
    public void test7(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("age")
                     .orderByAsc("uid");
        List<User> userList = userMapper.selectList(queryWrapper);
    }

    @Test
    public void test8(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("email");
        int delete = userMapper.delete(queryWrapper);
    }

    @Test
    public void test9(){
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.gt("age",20)
//                .like("name","a")
//                .or()
//                .isNull("email");
//        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        User user = new User();
//        user.setName("小明");
//        user.setEmail("1231231@qq.com");
//        userMapper.update(user,queryWrapper);
    }

    @Test
    public void test10(){
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.like("name","a")
//                .and(w -> w.gt("age",20).or().isNull("email"));
//        User user = new User();
//        user.setName("小红");
//        user.setEmail("1231231@qq.com");
//        userMapper.update(user,queryWrapper);
    }

    @Test
    public void test11(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("name","age","email");
        List<Map<String, Object>> maps = userMapper.selectMaps(queryWrapper);
    }

    @Test
    public void test12(){
        User user = new User();
        user.setUsername("张三");
        user.setPassword("zhangsan");
        userMapper.insert(user);
    }


}
