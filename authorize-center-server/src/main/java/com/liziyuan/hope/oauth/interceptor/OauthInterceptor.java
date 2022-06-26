package com.liziyuan.hope.oauth.interceptor;

import com.liziyuan.hope.oauth.common.SpringContextUtils;
import com.liziyuan.hope.oauth.common.constans.SessionConstants;
import com.liziyuan.hope.oauth.common.enums.ErrorCodeEnum;
import com.liziyuan.hope.oauth.common.utils.FieldUtils;
import com.liziyuan.hope.oauth.common.utils.JsonUtils;
import com.liziyuan.hope.oauth.das.mapper.AuthClientDetailsMapper;
import com.liziyuan.hope.oauth.das.mapper.AuthClientUserMapper;
import com.liziyuan.hope.oauth.das.mapper.AuthScopeMapper;
import com.liziyuan.hope.oauth.das.model.AuthClientDetails;
import com.liziyuan.hope.oauth.das.model.AuthClientUser;
import com.liziyuan.hope.oauth.das.model.AuthScope;
import com.liziyuan.hope.oauth.das.model.User;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 检查是否已经存在授权
 * /oauth2.0/authorize
 *
 * @author zqz
 * @date 2022/6/10
 * @since 1.0.0
 */
public class OauthInterceptor extends HandlerInterceptorAdapter {
    @Resource
    private AuthClientUserMapper authClientUserMapper;
    @Resource
    private AuthClientDetailsMapper authClientDetailsMapper;
    @Resource
    private AuthScopeMapper authScopeMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        //参数信息
        String params = "?redirectUri=" + SpringContextUtils.getRequestUrl(request);

        //客户端ID
        String clientIdStr = request.getParameter("client_id");
        //权限范围
        String scopeStr = request.getParameter("scope");
        //回调URL
        String redirectUri = request.getParameter("redirect_uri");
        //返回形式
        String responseType = request.getParameter("response_type");

        //获取session中存储的token
        User user = (User) session.getAttribute(SessionConstants.SESSION_USER);

        if (!FieldUtils.isHaveEmpty(clientIdStr, redirectUri, scopeStr) && "code".equals(responseType)) {
            params = params + "&client_id=" + clientIdStr + "&scope=" + scopeStr;

            //1. 查询是否存在授权信息
            AuthClientDetails clientDetails = authClientDetailsMapper.selectByClientId(clientIdStr);
            AuthScope scope = authScopeMapper.selectByScopeName(scopeStr);

            if (clientDetails == null) {
                return this.generateErrorResponse(response, ErrorCodeEnum.INVALID_CLIENT);
            }

            if (scope == null) {
                return this.generateErrorResponse(response, ErrorCodeEnum.INVALID_SCOPE);
            }

            if (!clientDetails.getRedirectUri().equals(redirectUri)) {
                return this.generateErrorResponse(response, ErrorCodeEnum.REDIRECT_URI_MISMATCH);
            }

            //2. 查询用户给接入的客户端是否已经授权
            AuthClientUser clientUser = authClientUserMapper.selectByClientId(clientDetails.getId(), user.getId(), scope.getId());
            if (clientUser != null) {
                // return this.generateErrorResponse(response, ErrorCodeEnum.REPEAT_AUTHORIZE);
                //response.sendRedirect(request.getContextPath() + "/oauth2.0/authorizePage" + params);
                return true;
            } else {
                //如果没有授权，则跳转到授权页面
                response.sendRedirect(request.getContextPath() + "/oauth2.0/authorizePage" + params);
                return false;
            }
        } else {
            return this.generateErrorResponse(response, ErrorCodeEnum.INVALID_REQUEST);
        }
    }

    /**
     * 组装错误请求的返回
     */
    private boolean generateErrorResponse(HttpServletResponse response, ErrorCodeEnum errorCodeEnum) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        Map<String, String> result = new HashMap<>(2);
        result.put("error", errorCodeEnum.getError());
        result.put("error_description", errorCodeEnum.getErrorDescription());

        response.getWriter().write(JsonUtils.toJson(result));
        return false;
    }

}
