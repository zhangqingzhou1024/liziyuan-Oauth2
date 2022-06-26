package com.liziyuan.hope.oauth.interceptor;

import com.liziyuan.hope.oauth.common.SpringContextUtils;
import com.liziyuan.hope.oauth.common.constans.SessionConstants;
import com.liziyuan.hope.oauth.das.model.User;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 定义一些页面需要做登录检查
 * "/user/**", "/oauth2.0/authorizePage", "/oauth2.0/authorize", "/sso/token"
 *
 * @author zqz
 * @date 2022/6/26
 * @since 1.0.0
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    /**
     * 检查是否已经登录
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        //获取session中存储的token
        User user = (User) session.getAttribute(SessionConstants.SESSION_USER);

        if (user != null) {
            return true;
        } else {
            //如果token不存在，则跳转到登录页面
            response.sendRedirect(request.getContextPath() + "/login?redirectUri=" + SpringContextUtils.getRequestUrl(request));

            return false;
        }
    }
}
