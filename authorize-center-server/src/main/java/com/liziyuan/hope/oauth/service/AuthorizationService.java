package com.liziyuan.hope.oauth.service;


import com.liziyuan.hope.oauth.das.model.AuthAccessToken;
import com.liziyuan.hope.oauth.das.model.AuthClientDetails;
import com.liziyuan.hope.oauth.das.model.AuthRefreshToken;
import com.liziyuan.hope.oauth.das.model.User;

/**
 * 授权相关Service
 *
 * @author zqz
 * @date 2022/6/3
 * @since 1.0.0
 */
public interface AuthorizationService {

    /**
     * 注册需要接入的客户端信息
     *
     * @param clientDetails 用户传递进来的关键信息
     * @return boolean
     * @author zqz
     * @date 2022/6/3 16:24
     * @since 1.0.0
     */
    AuthClientDetails register(AuthClientDetails clientDetails) throws IllegalAccessException;

    /**
     * 通过id查询客户端信息
     *
     * @param id client_id
     * @return com.liziyuan.hope.oauth.db.model.AuthClientDetails
     * @author zqz
     * @date 2022/6/3 16:45
     * @since 1.0.0
     */
    AuthClientDetails selectClientDetailsById(Integer id);

    /**
     * 通过client_id查询客户端信息
     *
     * @param clientId client_id
     * @return com.liziyuan.hope.oauth.db.model.AuthClientDetails
     * @author zqz
     * @date 2022/6/3 16:45
     * @since 1.0.0
     */
    AuthClientDetails selectClientDetailsByClientId(String clientId);

    /**
     * 通过Access Token查询记录
     *
     * @param accessToken Access Token
     * @return com.liziyuan.hope.oauth.db.model.AuthAccessToken
     * @author zqz
     * @date 2022/6/20 14:33
     * @since 1.0.0
     */
    AuthAccessToken selectByAccessToken(String accessToken);

    /**
     * 通过主键查询记录
     *
     * @param
     * @return com.liziyuan.hope.oauth.db.model.AuthAccessToken
     * @author zqz
     * @date 2022/6/22 11:40
     * @since 1.0.0
     */
    AuthAccessToken selectByAccessId(Integer id);

    /**
     * 通过Refresh Token查询记录
     *
     * @param refreshToken Refresh Token
     * @return com.liziyuan.hope.oauth.db.model.AuthRefreshToken
     * @author zqz
     * @date 2022/6/22 11:35
     * @since 1.0.0
     */
    AuthRefreshToken selectByRefreshToken(String refreshToken);

    /**
     * 保存哪个用户授权哪个接入的客户端哪种访问范围的权限
     *
     * @param userId      用户ID
     * @param clientIdStr 接入的客户端client_id
     * @param scopeStr    可被访问的用户的权限范围，比如：basic、super
     * @author zqz
     * @date 2022/6/6 17:46
     * @since 1.0.0
     */
    boolean saveAuthClientUser(Integer userId, String clientIdStr, String scopeStr);

    /**
     * 根据clientId、scope以及当前时间戳生成AuthorizationCode（有效期为10分钟）
     *
     * @param clientIdStr 客户端ID
     * @param scopeStr    scope
     * @param user        用户信息
     * @return java.lang.String
     * @author zqz
     * @date 2022/6/18 14:13
     * @since 1.0.0
     */
    String createAuthorizationCode(String clientIdStr, String scopeStr, User user);

    /**
     * 生成Access Token
     *
     * @param user               用户信息
     * @param savedClientDetails 接入的客户端信息
     * @param grantType          授权方式
     * @param scope              允许访问的用户权限范围
     * @param expiresIn          过期时间
     * @return java.lang.String
     * @author zqz
     * @date 2022/6/18 17:11
     * @since 1.0.0
     */
    String createAccessToken(User user, AuthClientDetails savedClientDetails, String grantType, String scope, Long expiresIn);


    /**
     * 生成Refresh Token
     *
     * @param user            用户信息
     * @param authAccessToken 生成的Access Token信息
     * @return java.lang.String
     * @author zqz
     * @date 2022/6/18 17:11
     * @since 1.0.0
     */
    String createRefreshToken(User user, AuthAccessToken authAccessToken);
}
