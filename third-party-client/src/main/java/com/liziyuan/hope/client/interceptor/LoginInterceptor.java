package com.liziyuan.hope.client.interceptor;

import com.liziyuan.hope.client.common.SpringContextUtils;
import com.liziyuan.hope.client.common.constans.ClientSessionConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 定义一些页面需要做登录检查
 * /user/**
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
        String accessToken = (String) session.getAttribute(ClientSessionConstants.SESSION_ACCESS_TOKEN);

        if (StringUtils.isNoneBlank(accessToken)) {
            return true;
        } else {
            //如果token不存在，则跳转等登录页面
            response.sendRedirect(request.getContextPath() + "/login?redirectUrl=" + SpringContextUtils.getRequestUrl(request));

            return false;
        }
    }
}
