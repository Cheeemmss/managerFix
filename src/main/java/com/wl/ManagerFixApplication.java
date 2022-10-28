package com.wl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@MapperScan("com.wl.mapper")
public class ManagerFixApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagerFixApplication.class, args);
    }

}
