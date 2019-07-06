package com.draymond;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//扫描mybatis mapper包路径
@MapperScan(basePackages = {"com.draymond.mapper"})
//ComponentScan注解的默认扫描范围是启动程序DraymondWechatApplication.java所在目录及其下的所有子包。

public class DraymondWechatApplication {

    public static void main(String[] args) {
        SpringApplication.run(DraymondWechatApplication.class, args);
    }

}
