package com.liziyuan.hope.client;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class ThirdPartClientApp7080 {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ThirdPartClientApp7080.class);
        application.setBannerMode(Banner.Mode.CONSOLE);
        application.run(args);
        System.out.println(">>>>>>>>>>>>> ThirdPartClientApplication 启动成功 端口-》【7080】>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

    }
}
