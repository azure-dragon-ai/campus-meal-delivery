package com.campus.starter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 校园配餐系统启动类
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.campus"})
@MapperScan("com.campus.database.mapper")
public class CampusMealDeliveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampusMealDeliveryApplication.class, args);
    }
}
