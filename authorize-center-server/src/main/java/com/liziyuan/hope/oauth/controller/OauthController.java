package com.liziyuan.hope.oauth.controller;


import com.liziyuan.hope.oauth.common.constans.SessionConstants;
import com.liziyuan.hope.oauth.common.enums.ErrorCodeEnum;
import com.liziyuan.hope.oauth.common.enums.ExpireEnum;
import com.liziyuan.hope.oauth.common.enums.GrantTypeEnum;
import com.liziyuan.hope.oauth.common.utils.DateUtils;
import com.liziyuan.hope.oauth.das.model.AuthAccessToken;
import com.liziyuan.hope.oauth.das.model.AuthClientDetails;
import com.liziyuan.hope.oauth.das.model.AuthRefreshToken;
import com.liziyuan.hope.oauth.das.model.User;
import com.liziyuan.hope.oauth.service.AuthorizationService;
import com.liziyuan.hope.oauth.service.RedisService;
import com.liziyuan.hope.oauth.service.UserService;
import lombok.SneakyThrows;
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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 基于oauth2.0相关的授权相关操作
 *
 * @author zqz
 * @date 2022/6/3
 * @since 1.0.0
 */
@Controller
@RequestMapping("/oauth2.0")
public class OauthController {

    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource(name = "authorizationServiceImpl")
    private AuthorizationService authorizationService;

    @Resource(name = "userServiceImpl")
    private UserService userService;

    /**
     * 注册需要接入的客户端信息
     *
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author zqz
     * @date 2022/6/3 16:24
     * @since 1.0.0
     */
    @PostMapping(value = "/clientRegister", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public AuthClientDetails clientRegister(@RequestBody(required = true) AuthClientDetails clientDetails) throws IllegalAccessException {
        return authorizationService.register(clientDetails);
    }

    /**
     * 授权页面
     *
     * @return org.springframework.web.servlet.ModelAndView
     * @author zqz
     * @date 2022/6/3 16:31
     * @since 1.0.0
     */
    @RequestMapping("/authorizePage")
    public ModelAndView authorizePage(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("authorize");

        //在页面同意授权后的回调地址
        String redirectUrl = request.getParameter("redirectUri");
        //客户端ID
        String clientId = request.getParameter("client_id");
        //权限范围
        String scope = request.getParameter("scope");

        if (StringUtils.isNoneBlank(redirectUrl)) {
            HttpSession session = request.getSession();
            //将回调地址添加到session中
            session.setAttribute(SessionConstants.SESSION_AUTH_REDIRECT_URL, redirectUrl);
        }

        //查询请求授权的客户端信息
        AuthClientDetails clientDetails = authorizationService.selectClientDetailsByClientId(clientId);
        modelAndView.addObject("clientId", clientId);
        modelAndView.addObject("clientName", clientDetails.getClientName());
        modelAndView.addObject("scope", scope);

        return modelAndView;
    }

    /**
     * 同意授权
     *
     * @param request HttpServletRequest
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author zqz
     * @date 2022/6/6 17:40
     * @since 1.0.0
     */
    @SneakyThrows
    @PostMapping(value = "/agree", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> agree(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>(2);
        HttpSession session = request.getSession();

        //客户端ID
        String clientIdStr = request.getParameter("client_id");
        AuthClientDetails authClientDetails = authorizationService.selectClientDetailsByClientId(clientIdStr);
        if(null == authClientDetails){
            throw new IllegalAccessException("client_id : "+clientIdStr+" 对应的客户端信息不存在！");
        }
        // http://127.0.0.1:7080/login
        String redirectUrl = authClientDetails.getRedirectUri();
        //权限范围
        String scopeStr = request.getParameter("scope");

        if (StringUtils.isNoneBlank(clientIdStr) && StringUtils.isNoneBlank(scopeStr)) {
            User user = (User) session.getAttribute(SessionConstants.SESSION_USER);

            //1. 向数据库中保存授权信息
            boolean saveFlag = authorizationService.saveAuthClientUser(user.getId(), clientIdStr, scopeStr);

            //2. 返回给页面的数据
            if (saveFlag) {
                result.put("code", 200);

                //授权成功之后的回调地址
               // String redirectUrl = (String) session.getAttribute(SessionConstants.SESSION_AUTH_REDIRECT_URL);
                session.removeAttribute(SessionConstants.SESSION_AUTH_REDIRECT_URL);
                // 重定向 到客户端
                if (StringUtils.isNoneBlank(redirectUrl)) {
                    result.put("redirect_uri", redirectUrl);
                }
            } else {
                result.put("msg", "授权失败！");
            }
        } else {
            result.put("msg", "请求参数不能为空！");
        }

        return result;
    }

    /**
     * 获取Authorization Code
     *
     * @param request HttpServletRequest
     * @return org.springframework.web.servlet.ModelAndView
     * @author zqz
     * @date 2022/6/6 17:40
     * @since 1.0.0
     */
    @RequestMapping("/authorize")
    public ModelAndView authorize(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(SessionConstants.SESSION_USER);

        //客户端ID
        String clientIdStr = request.getParameter("client_id");
        //权限范围
        String scopeStr = request.getParameter("scope");
        //回调URL
        String redirectUri = request.getParameter("redirect_uri");
        //status，用于防止CSRF攻击（非必填）
        String status = request.getParameter("status");

        //生成Authorization Code
        String authorizationCode = authorizationService.createAuthorizationCode(clientIdStr, scopeStr, user);

        String params = "?code=" + authorizationCode;
        if (StringUtils.isNoneBlank(status)) {
            params = params + "&status=" + status;
        }

        return new ModelAndView("redirect:" + redirectUri + params);
    }

    /**
     * 通过Authorization Code获取Access Token
     *
     * @param request HttpServletRequest
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author zqz
     * @date 2022/6/18 15:11
     * @since 1.0.0
     */
    @RequestMapping(value = "/token", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> token(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>(8);

        //授权方式
        String grantType = request.getParameter("grant_type");
        //前面获取的Authorization Code
        String code = request.getParameter("code");
        //客户端ID- appKey
        String clientIdStr = request.getParameter("client_id");
        //接入的客户端的密钥 - appSecret
        String clientSecret = request.getParameter("client_secret");
        //回调URL
        String redirectUri = request.getParameter("redirect_uri");

        //校验授权方式
        if (!GrantTypeEnum.AUTHORIZATION_CODE.getType().equals(grantType)) {
            this.generateErrorResponse(result, ErrorCodeEnum.UNSUPPORTED_GRANT_TYPE);
            return result;
        }

        try {
            AuthClientDetails savedClientDetails = authorizationService.selectClientDetailsByClientId(clientIdStr);
            //校验请求的客户端秘钥和已保存的秘钥是否匹配
            if (!(savedClientDetails != null && savedClientDetails.getClientSecret().equals(clientSecret))) {
                this.generateErrorResponse(result, ErrorCodeEnum.INVALID_CLIENT);
                return result;
            }

            //校验回调URL
            if (!savedClientDetails.getRedirectUri().equals(redirectUri)) {
                this.generateErrorResponse(result, ErrorCodeEnum.REDIRECT_URI_MISMATCH);
                return result;
            }

            //从Redis获取允许访问的用户权限范围
            String scope = redisService.get(code + ":scope");
            //从Redis获取对应的用户信息
            User user = redisService.get(code + ":user");

            //如果能够通过Authorization Code获取到对应的用户信息，则说明该Authorization Code有效
            if (StringUtils.isNoneBlank(scope) && user != null) {
                //过期时间
                Long expiresIn = DateUtils.dayToSecond(ExpireEnum.ACCESS_TOKEN.getTime());

                //生成Access Token
                String accessTokenStr = authorizationService.createAccessToken(user, savedClientDetails, grantType, scope, expiresIn);
                //查询已经插入到数据库的Access Token
                AuthAccessToken authAccessToken = authorizationService.selectByAccessToken(accessTokenStr);
                //生成Refresh Token
                String refreshTokenStr = authorizationService.createRefreshToken(user, authAccessToken);

                //返回数据
                result.put("access_token", authAccessToken.getAccessToken());
                result.put("refresh_token", refreshTokenStr);
                result.put("expires_in", expiresIn);
                result.put("scope", scope);
                return result;
            } else {
                this.generateErrorResponse(result, ErrorCodeEnum.INVALID_GRANT);
                return result;
            }
        } catch (Exception e) {
            this.generateErrorResponse(result, ErrorCodeEnum.UNKNOWN_ERROR);
            return result;
        }
    }

    /**
     * 通过Refresh Token刷新Access Token
     *
     * @param request HttpServletRequest
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author zqz
     * @date 2022/6/22 11:11
     * @since 1.0.0
     */
    @RequestMapping(value = "/refreshToken", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> refreshToken(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>(8);

        //获取Refresh Token
        String refreshTokenStr = request.getParameter("refresh_token");

        try {
            AuthRefreshToken authRefreshToken = authorizationService.selectByRefreshToken(refreshTokenStr);

            if (authRefreshToken != null) {
                Long savedExpiresAt = authRefreshToken.getExpiresIn();
                //过期日期
                LocalDateTime expiresDateTime = DateUtils.ofEpochSecond(savedExpiresAt, null);
                //当前日期
                LocalDateTime nowDateTime = DateUtils.now();

                //如果Refresh Token已经失效，则需要重新生成
                if (expiresDateTime.isBefore(nowDateTime)) {
                    this.generateErrorResponse(result, ErrorCodeEnum.EXPIRED_TOKEN);
                    return result;
                } else {
                    //获取存储的Access Token
                    AuthAccessToken authAccessToken = authorizationService.selectByAccessId(authRefreshToken.getTokenId());
                    //获取对应的客户端信息
                    AuthClientDetails savedClientDetails = authorizationService.selectClientDetailsById(authAccessToken.getClientId());
                    //获取对应的用户信息
                    User user = userService.selectByUserId(authAccessToken.getUserId());

                    //新的过期时间
                    Long expiresIn = DateUtils.dayToSecond(ExpireEnum.ACCESS_TOKEN.getTime());
                    //生成新的Access Token
                    String newAccessTokenStr = authorizationService.createAccessToken(user, savedClientDetails
                            , authAccessToken.getGrantType(), authAccessToken.getScope(), expiresIn);

                    //返回数据
                    result.put("access_token", newAccessTokenStr);
                    result.put("refresh_token", refreshTokenStr);
                    result.put("expires_in", expiresIn);
                    result.put("scope", authAccessToken.getScope());
                    return result;
                }
            } else {
                this.generateErrorResponse(result, ErrorCodeEnum.INVALID_GRANT);
                return result;
            }
        } catch (Exception e) {
            this.generateErrorResponse(result, ErrorCodeEnum.UNKNOWN_ERROR);
            return result;
        }
    }

    /**
     * 组装错误请求的返回
     */
    private void generateErrorResponse(Map<String, Object> result, ErrorCodeEnum errorCodeEnum) {
        result.put("error", errorCodeEnum.getError());
        result.put("error_description", errorCodeEnum.getErrorDescription());
    }

}
