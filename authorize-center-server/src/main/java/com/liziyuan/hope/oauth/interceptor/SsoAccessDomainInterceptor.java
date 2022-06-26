package com.liziyuan.hope.oauth.interceptor;

import com.liziyuan.hope.oauth.common.enums.ErrorCodeEnum;
import com.liziyuan.hope.oauth.common.utils.SystemErrorUtils;
import com.liziyuan.hope.oauth.das.mapper.SsoClientDetailsMapper;
import com.liziyuan.hope.oauth.das.model.SsoClientDetails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于校验请求获取Token的回调URL是否在白名单中
 *
 * @author zqz
 * @date 2022/6/30
 * @since 1.0.0
 */
public class SsoAccessDomainInterceptor extends HandlerInterceptorAdapter {
    @Resource
    private SsoClientDetailsMapper ssoClientDetailsMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String redirectUri = request.getParameter("redirect_uri");

        if (StringUtils.isNoneBlank(redirectUri)) {
            //查询数据库中的回调地址的白名单
            SsoClientDetails ssoClientDetails = ssoClientDetailsMapper.selectByRedirectUrl(redirectUri);

            if (ssoClientDetails != null) {
                return true;
            } else {
                //如果回调URL不在白名单中，则返回错误提示
                return SystemErrorUtils.generateErrorResponse(response, ErrorCodeEnum.INVALID_REDIRECT_URI);
            }
        } else {
            return SystemErrorUtils.generateErrorResponse(response, ErrorCodeEnum.INVALID_REQUEST);
        }
    }

}
