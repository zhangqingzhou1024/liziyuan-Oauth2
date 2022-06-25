package com.liziyuan.hope.oauth.service;

import com.liziyuan.hope.oauth.das.model.SsoAccessToken;
import com.liziyuan.hope.oauth.das.model.SsoClientDetails;
import com.liziyuan.hope.oauth.das.model.SsoRefreshToken;
import com.liziyuan.hope.oauth.das.model.User;

/**
 * SSO单点登录相关Service
 *
 * @author zqz
 * @date 2022/6/30
 * @since 1.0.0
 */
public interface SsoService {

    /**
     * 根据ID查询接入客户端
     *
     * @param id id
     * @return com.liziyuan.hope.oauth.db.model.SsoClientDetails
     * @author zqz
     * @date 2022/6/30 16:36
     * @since 1.0.0
     */
    SsoClientDetails selectByPrimaryKey(Integer id);

    /**
     * 根据URL查询记录
     *
     * @param redirectUrl 回调URL
     * @return com.liziyuan.hope.oauth.db.model.SsoClientDetails
     * @author zqz
     * @date 2022/6/30 16:36
     * @since 1.0.0
     */
    SsoClientDetails selectByRedirectUrl(String redirectUrl);

    /**
     * 通过主键ID查询记录
     *
     * @param id ID
     * @return com.liziyuan.hope.oauth.db.model.SsoAccessToken
     * @author zqz
     * @date 2022/6/30 13:11
     * @since 1.0.0
     */
    SsoAccessToken selectByAccessId(Integer id);

    /**
     * 通过Access Token查询记录
     *
     * @param accessToken Access Token
     * @return com.liziyuan.hope.oauth.db.model.SsoAccessToken
     * @author zqz
     * @date 2022/6/30 13:11
     * @since 1.0.0
     */
    SsoAccessToken selectByAccessToken(String accessToken);

    /**
     * 通过tokenId查询记录
     *
     * @param tokenId tokenId
     * @return com.liziyuan.hope.oauth.db.model.SsoRefreshToken
     * @author zqz
     * @date 2022/6/30 13:11
     * @since 1.0.0
     */
    SsoRefreshToken selectByTokenId(Integer tokenId);

    /**
     * 通过Refresh Token查询记录
     *
     * @param refreshToken Refresh Token
     * @return com.liziyuan.hope.oauth.db.model.SsoRefreshToken
     * @author zqz
     * @date 2022/6/30 13:11
     * @since 1.0.0
     */
    SsoRefreshToken selectByRefreshToken(String refreshToken);

    /**
     * 生成Access Token
     *
     * @param user             用户信息
     * @param expiresIn        过期时间
     * @param ssoClientDetails 接入客户端详情
     * @param requestIP        用户请求的IP
     * @return java.lang.String
     * @author zqz
     * @date 2022/6/30 13:11
     * @since 1.0.0
     */
    String createAccessToken(User user, Long expiresIn, String requestIP, SsoClientDetails ssoClientDetails);


    /**
     * 生成Refresh Token
     *
     * @param user           用户信息
     * @param ssoAccessToken 生成的Access Token信息
     * @return java.lang.String
     * @author zqz
     * @date 2022/6/30 13:11
     * @since 1.0.0
     */
    String createRefreshToken(User user, SsoAccessToken ssoAccessToken);

}
