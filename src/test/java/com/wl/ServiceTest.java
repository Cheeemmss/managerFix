package com.wl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.excel.EasyExcel;
import com.wl.entity.Menu;
import com.wl.entity.User;
import com.wl.listener.ExcelListener;
import com.wl.service.MenuService;
import com.wl.service.RoleService;
import com.wl.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.SpringBootTest;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
public class ServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    @Test
    public void test1(){
        long count = userService.count();
        System.out.println(count);
    }

//    @Test
//    public void test2(){
//        List<User> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            User user = new User();
//            user.setName("tom"+i);
//            user.setAge(20+i);
//            list.add(user);
//        }
//        userService.saveBatch(list);
//    }
//
//    @Test
//    public void test3(){
//
//    }

    @Test
    public void test3(){
        User user = new User();
        user.setUsername("张四");
        user.setEmail("111111@qq.com");
        userService.saveOrUpdate(user);
    }

    @Test
    public void testExcel(){
        String filename = "I:\\用户2.xlsx";

        List<User> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            User user = new User();
//            user.setUsername("tom"+i);
//            user.setEmail("1234@qq.com"+i);
//            list.add(user);
//        }
        List<User> userList = userService.list();

        EasyExcel.write(filename,User.class).sheet("用户表1").doWrite(userList);
    }

    @Test
    public void testExcel2() throws FileNotFoundException {
        String filename = "I:\\用户.xlsx";
        File file = new File(filename);
        FileInputStream is = new FileInputStream(file);
        EasyExcel.read(is,User.class,new ExcelListener(userService)).sheet().doRead();
    }

    @Test
    public void test4(){
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.DAY_OF_YEAR);
    }

    @Test
    public void test5() throws FileNotFoundException {
        File file = new File("I:\\files2\\test.txt");
        String md5 = SecureUtil.md5(new FileInputStream(file));
        System.out.println(md5);
    }

//    @Test
//    public void test6(){
//        List<Menu> menuTree = menuService.getMenuTree();
//        System.out.println(menuTree);
//    }

    @Test
    public void test7(){
        List<Menu> list = menuService.list();
        List<Menu> collect = list.stream().filter(m -> m.getPid() == null).map(
                (m) -> {
                    m.setChildrenMenu(getChildren(m, list));
                    return m;
                }
        ).collect(Collectors.toList());
    }

    private List<Menu> getChildren(Menu root, List<Menu> all) {
        List<Menu> children = all.stream().filter(m -> {
            return Objects.equals(m.getPid(), root.getId());
        }).map(
                //这里由于最下面一层子节点过滤出来的List为空, 所以不会再执行下面的map 也就不会再递归调用getChildren方法 直接将最下面一层子节点返回, 递归开始回溯
                (m) -> {
                    m.setChildrenMenu(getChildren(m, all));
                    return m;
                }
        ).collect(Collectors.toList());
        return children;
    }

    @Test
    public void streamTest(){
        Integer[] integers = {1,2,3,4,5};
        List<Integer> list = Arrays.asList(integers);
        //只要调用stream()方法的对象不是null就行 就算是[]空集合 他 []-->map()-->filter()-->[] 他 map() filter()都不会经过
        List<Integer> list1 = list.stream().filter(num -> num > 5).map(num -> {
            System.out.println("执行了");
            return num;
        }).collect(Collectors.toList());

        System.out.println(list1);

    }

    @Test
    public void test8(){
        List<Menu> list = userService.getMenusByUserId(2);
        list.forEach(System.out::println);
    }

    @Test
    public void test9(){
        List<Menu> menuTree = menuService.getMenuTree(2);
        menuTree.forEach(System.out::println);
    }

//    @Test
//    public void redisTest(){
//        Jedis jedis = new Jedis("192.168.246.128",6379);
//        jedis.auth("WL20010825");
////        System.out.println(jedis.ping());
//          //string
////        jedis.set("name","tom");
////        String name = jedis.get("name");
////        System.out.println(name);
//          jedis.mset("k1","v1","k2","v2");
//        List<String> list = jedis.mget("k1", "k2", "name");
//        list.forEach(System.out::println);
////        Set<String> keys = jedis.keys("*");
////        for (String key : keys) {
////            System.out.println(key);
////        }
//
//
//    }
//
//    @Test
//    public void redisTest2(){
//        Jedis jedis = new Jedis("192.168.246.128",6379);
//        jedis.auth("WL20010825");
//        //list
//        jedis.lpush("key1","lucy","jack","jerry");
//        List<String> list = jedis.lrange("key1", 0, -1);
//        list.forEach(System.out::println);
//        jedis.sadd("key2","lucy","mary");
//        //set
//        Set<String> set = jedis.smembers("key2");
//        set.forEach(System.out::println);
//        //hash
//        jedis.hset("users","name","30");
//        String hget = jedis.hget("users", "name");
//        System.out.println(hget);
//        jedis.zadd("china",100d,"shanghai");
//        Set<String> set1 = jedis.zrange("china", 0, -1);
//        System.out.println(set1);
//
//    }
//
//    //生成验证码
//    public String getCode(){
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < 6; i++) {
//            int num = new Random().nextInt(10);
//            sb.append(num);
//        }
//        return sb.toString();
//    }
//    //判断是否发送超过三次
//    public void verifyCode(String phone){
//        Jedis jedis = new Jedis("192.168.246.128",6379);
//        jedis.auth("WL20010825");
//
//        //结合phoneNum生成唯一的key
//        String countKey = "countKey" + phone ; //用于记录发了几次的key
//        String codeKey = "codeKey" + phone;
//
//        String count = jedis.get(countKey);
//        if(count == null){
//            jedis.setex(countKey,60*60*24,"1");
//        }else if(Integer.parseInt(count) <=2 ){
//            jedis.incr(countKey);
//        }else if (Integer.parseInt(count) > 2){
//            System.out.println("今日你已发送过三次");
//            jedis.close();
//            return;
//        }
//
//        jedis.setex(codeKey,120,getCode());
//        jedis.close();
//
//    }
//
//    //根据手机号码和验证码去Redis中找之前存的code(通过 codeKey+phone 的key去找),并与用户发送过来的code进行比较
//    public void getRedisCode(String phone,String code){
//        Jedis jedis = new Jedis("192.168.246.128",6379);
//        jedis.auth("WL20010825");
//
//        String codeKey = "codeKey" + phone;
//        String redisCode = jedis.get(codeKey);
//        if(redisCode.equals(code)){
//            System.out.println("成功");
//        }else {
//            System.out.println("失败");
//        }
//    }
//
//    @Test
//    public void redisTest3(){
//
//        //模拟发送 并设置到Redis中去
//        verifyCode("13880798126");
//        //模拟进行用户输入验证码的校验
//        getRedisCode("13880798126","159550");
//    }

    @Test
    public void test10(){
        String s = DigestUtils.md5Hex("root" + ")*3aWleQ");
        System.out.println(s);
    }


}
