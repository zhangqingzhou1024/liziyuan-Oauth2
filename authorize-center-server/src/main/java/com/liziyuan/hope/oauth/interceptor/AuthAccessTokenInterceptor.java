package com.liziyuan.hope.oauth.interceptor;

import com.liziyuan.hope.oauth.common.enums.ErrorCodeEnum;
import com.liziyuan.hope.oauth.common.utils.DateUtils;
import com.liziyuan.hope.oauth.common.utils.JsonUtils;
import com.liziyuan.hope.oauth.common.utils.SystemErrorUtils;
import com.liziyuan.hope.oauth.das.model.AuthAccessToken;
import com.liziyuan.hope.oauth.service.AuthorizationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于校验Access Token是否为空以及Access Token是否已经失效
 * /api/**
 *
 * @author zqz
 * @date 2022/6/22
 * @since 1.0.0
 */
public class AuthAccessTokenInterceptor extends HandlerInterceptorAdapter {
    @Resource(name = "authorizationServiceImpl")
    private AuthorizationService authorizationService;

    /**
     * 检查Access Token是否已经失效
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = request.getParameter("access_token");

        if (StringUtils.isNoneBlank(accessToken)) {
            //查询数据库中的Access Token
            AuthAccessToken authAccessToken = authorizationService.selectByAccessToken(accessToken);

            if (authAccessToken != null) {
                Long savedExpiresAt = authAccessToken.getExpiresIn();
                //过期日期
                LocalDateTime expiresDateTime = DateUtils.ofEpochSecond(savedExpiresAt, null);
                //当前日期
                LocalDateTime nowDateTime = DateUtils.now();

                //如果Access Token已经失效，则返回错误提示
                return expiresDateTime.isAfter(nowDateTime) || SystemErrorUtils.generateErrorResponse(response, ErrorCodeEnum.EXPIRED_TOKEN);
            } else {
                return SystemErrorUtils.generateErrorResponse(response, ErrorCodeEnum.INVALID_GRANT);
            }
        } else {
            return SystemErrorUtils.generateErrorResponse(response, ErrorCodeEnum.INVALID_REQUEST);
        }
    }

}
