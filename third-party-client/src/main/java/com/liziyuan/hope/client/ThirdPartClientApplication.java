package com.liziyuan.hope.client;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class ThirdPartClientApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ThirdPartClientApplication.class);
        application.setBannerMode(Banner.Mode.CONSOLE);
        application.run(args);
        System.out.println(">>>>>>>>>>>>> ThirdPartClientApplication 启动成功 端口-》【7080】>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

    }
}
