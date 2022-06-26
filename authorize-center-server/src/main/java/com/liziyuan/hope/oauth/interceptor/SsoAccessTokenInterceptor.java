package com.liziyuan.hope.oauth.interceptor;

import com.liziyuan.hope.oauth.common.enums.ErrorCodeEnum;
import com.liziyuan.hope.oauth.common.utils.DateUtils;
import com.liziyuan.hope.oauth.common.utils.SystemErrorUtils;
import com.liziyuan.hope.oauth.das.model.SsoAccessToken;
import com.liziyuan.hope.oauth.service.SsoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

/**
 * 用于校验Access Token是否为空以及Access Token是否已经失效
 *
 * @author zqz
 * @date 2022/6/30
 * @since 1.0.0
 */
public class SsoAccessTokenInterceptor extends HandlerInterceptorAdapter {
    @Resource(name = "ssoServiceImpl")
    private SsoService ssoService;

    /**
     * 检查Access Token是否已经失效
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = request.getParameter("access_token");

        if (StringUtils.isNoneBlank(accessToken)) {
            //查询数据库中的Access Token
            SsoAccessToken ssoAccessToken = ssoService.selectByAccessToken(accessToken);

            if (ssoAccessToken != null) {
                Long savedExpiresAt = ssoAccessToken.getExpiresIn();
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
