package com.liziyuan.hope.client.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用户相关controller
 *
 * @author zqz
 * @date 2022/6/9
 * @since 1.0.0
 */
@Controller
@RequestMapping("/user")
public class ClientUserController {

    /**
     * 用户首页
     *
     * @return java.lang.String
     * @author zqz
     * @date 2022/6/9 17:10
     * @since 1.0.0
     */
    @RequestMapping("/userIndex")
    public String userIndex() {
        return "userIndex";
    }

    /**
     * 一个测试的受保护的页面
     *
     * @return java.lang.String
     * @author zqz
     * @date 2022/6/9 17:10
     * @since 1.0.0
     */
    @RequestMapping("/protected")
    public String protectedPage() {
        return "protected";
    }
}
