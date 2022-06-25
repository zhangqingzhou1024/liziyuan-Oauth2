package com.liziyuan.hope.oauth.controller;

import com.liziyuan.hope.oauth.common.constans.SessionConstants;
import com.liziyuan.hope.oauth.das.model.User;
import com.liziyuan.hope.oauth.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户相关controller
 *
 * @author zqz
 * @date 2022/6/3
 * @since 1.0.0
 */
@Controller
public class UserController {

    @Resource(name = "userServiceImpl")
    private UserService userService;

    /**
     * 注册接口
     *
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author zqz
     * @date 2022/6/3 11:13
     * @since 1.0.0
     */
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> register(@RequestBody(required = true) User user) {
        Map<String, Object> result = new HashMap<>(2);

        boolean registerRet = userService.register(user);
        if (registerRet) {
            result.put("code", "200");
            result.put("msg", "注册成功");
        } else {
            result.put("code", "500");
            result.put("msg", "注册失败");
        }

        return result;
    }

    /**
     * 登录页面
     *
     * @return org.springframework.web.servlet.ModelAndView
     * @author zqz
     * @date 2022/6/3 11:13
     * @since 1.0.0
     */
    @RequestMapping("/login")
    public ModelAndView loginPage(HttpServletRequest request) {
        String redirectUrl = request.getParameter("redirectUri");
        if (StringUtils.isNoneBlank(redirectUrl)) {
            HttpSession session = request.getSession();
            //将回调地址添加到session中
            session.setAttribute(SessionConstants.SESSION_LOGIN_REDIRECT_URL, redirectUrl);
        }

        return new ModelAndView("login");
    }

    /**
     * 登录验证
     *
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author zqz
     * @date 2022/6/3 11:13
     * @since 1.0.0
     */
    @PostMapping("/check")
    @ResponseBody
    public Map<String, Object> check(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>(2);

        //用户名
        String username = request.getParameter("username");
        //密码
        String password = request.getParameter("password");

        if (StringUtils.isNoneBlank(username) && StringUtils.isNoneBlank(password)) {
            //1. 登录验证
            Map<String, Object> checkMap = userService.checkLogin(username, password);
            Boolean loginResult = (Boolean) checkMap.get("result");
            User correctUser = (User) checkMap.get("user");

            //登录验证通过
            if (loginResult != null && loginResult) {
                //2. session中添加用户信息
                HttpSession session = request.getSession();
                session.setAttribute(SessionConstants.SESSION_USER, correctUser);

                //3. 返回给页面的数据
                result.put("code", 200);
                //登录成功之后的回调地址
                String redirectUrl = (String) session.getAttribute(SessionConstants.SESSION_LOGIN_REDIRECT_URL);
                session.removeAttribute(SessionConstants.SESSION_LOGIN_REDIRECT_URL);

                if (StringUtils.isNoneBlank(redirectUrl)) {
                    result.put("redirect_uri", redirectUrl);
                }
            } else {
                if (correctUser.getStatus() == 0) {
                    result.put("msg", "该用户还未开通！");
                } else if (correctUser.getStatus() == 3) {
                    result.put("msg", "该用户已被管理员禁用！");
                } else {
                    result.put("msg", "用户名或密码错误！");
                }
            }
        } else {
            result.put("msg", "请求参数不能为空！");
        }

        return result;
    }

    /**
     * 注销
     *
     * @return org.springframework.web.servlet.ModelAndView
     * @author zqz
     * @date 2022/6/3 11:47
     * @since 1.0.0
     */
    @RequestMapping("/logout")
    public ModelAndView logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(SessionConstants.SESSION_USER);

        return new ModelAndView("redirect:/login");
    }

    /**
     * 用户首页
     *
     * @return java.lang.String
     * @author zqz
     * @date 2022/6/3 11:13
     * @since 1.0.0
     */
    @RequestMapping("/user/userIndex")
    public String userIndex() {
        return "userIndex";
    }
}
