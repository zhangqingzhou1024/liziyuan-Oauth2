package com.liziyuan.hope.oauth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ServletComponentScan
@EnableAsync
@MapperScan("com.liziyuan.hope")
public class AuthorizeCenterServerApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(AuthorizeCenterServerApplication.class);
        application.run(args);
        System.out.println(">>>>>>>>>>>>> AuthorizeCenterServerApplication启动成功 端口-》【7000】>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

    }
}
