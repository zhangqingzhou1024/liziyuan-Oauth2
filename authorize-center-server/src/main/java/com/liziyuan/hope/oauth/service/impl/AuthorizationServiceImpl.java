package com.liziyuan.hope.oauth.service.impl;

import com.liziyuan.hope.oauth.common.SpringContextUtils;
import com.liziyuan.hope.oauth.common.constans.SessionConstants;
import com.liziyuan.hope.oauth.common.enums.ExpireEnum;
import com.liziyuan.hope.oauth.common.utils.DateUtils;
import com.liziyuan.hope.oauth.common.utils.EncryptUtils;
import com.liziyuan.hope.oauth.das.mapper.*;
import com.liziyuan.hope.oauth.das.model.*;
import com.liziyuan.hope.oauth.service.AuthorizationService;
import com.liziyuan.hope.oauth.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * @author zqz
 * @date 2022/6/3
 * @since 1.0.0
 */
@Service("authorizationServiceImpl")
public class AuthorizationServiceImpl implements AuthorizationService {
    @Resource(name = "redisServiceImpl")
    private RedisService redisService;

    @Resource
    private AuthClientDetailsMapper authClientDetailsMapper;
    @Resource
    private AuthScopeMapper authScopeMapper;
    @Resource
    private AuthClientUserMapper authClientUserMapper;
    @Resource
    private AuthAccessTokenMapper authAccessTokenMapper;
    @Resource
    private AuthRefreshTokenMapper authRefreshTokenMapper;

    @Override
    public AuthClientDetails register(AuthClientDetails clientDetails) throws IllegalAccessException {
        //客户端的名称和回调地址不能为空
        if (StringUtils.isNoneBlank(clientDetails.getClientName()) && StringUtils.isNoneBlank(clientDetails.getRedirectUri())) {

            List<AuthClientDetails> authClientDetails = authClientDetailsMapper.selectByClientName(clientDetails.getClientName());
            if(!CollectionUtils.isEmpty(authClientDetails)){
                throw new IllegalAccessException("客户端的名称已存在!");
            }
            //生成24位随机的clientId
            String clientId = EncryptUtils.getRandomStr1(24);

            AuthClientDetails savedClientDetails = authClientDetailsMapper.selectByClientId(clientId);
            //生成的clientId必须是唯一的
            for (int i = 0; i < 10; i++) {
                if (savedClientDetails == null) {
                    break;
                } else {
                    clientId = EncryptUtils.getRandomStr1(24);
                    savedClientDetails = authClientDetailsMapper.selectByClientId(clientId);
                }
            }

            //生成32位随机的clientSecret
            String clientSecret = EncryptUtils.getRandomStr1(32);

            Date current = new Date();
            HttpSession session = SpringContextUtils.getSession();
            User user = (User) session.getAttribute(SessionConstants.SESSION_USER);

            clientDetails.setClientId(clientId);
            clientDetails.setClientSecret(clientSecret);
            clientDetails.setCreateUser(user.getId());
            clientDetails.setCreateTime(current);
            clientDetails.setUpdateUser(user.getId());
            clientDetails.setUpdateTime(current);
            clientDetails.setStatus(1);

            //保存到数据库
            authClientDetailsMapper.insertSelective(clientDetails);

            return clientDetails;
        } else {
            throw new IllegalAccessException("客户端的名称和回调地址不能为空");
        }
    }

    @Override
    public AuthClientDetails selectClientDetailsById(Integer id) {
        return authClientDetailsMapper.selectByPrimaryKey(id);
    }

    @Override
    public AuthClientDetails selectClientDetailsByClientId(String clientId) {
        return authClientDetailsMapper.selectByClientId(clientId);
    }

    @Override
    public AuthAccessToken selectByAccessToken(String accessToken) {
        return authAccessTokenMapper.selectByAccessToken(accessToken);
    }

    @Override
    public AuthAccessToken selectByAccessId(Integer id) {
        return authAccessTokenMapper.selectByPrimaryKey(id);
    }

    @Override
    public AuthRefreshToken selectByRefreshToken(String refreshToken) {
        return authRefreshTokenMapper.selectByRefreshToken(refreshToken);
    }

    @Override
    public boolean saveAuthClientUser(Integer userId, String clientIdStr, String scopeStr) {
        AuthClientDetails clientDetails = authClientDetailsMapper.selectByClientId(clientIdStr);
        AuthScope scope = authScopeMapper.selectByScopeName(scopeStr);

        if (clientDetails != null && scope != null) {
            AuthClientUser clientUser = authClientUserMapper.selectByClientId(clientDetails.getId(), userId, scope.getId());
            //如果数据库中不存在记录，则插入
            if (clientUser == null) {
                clientUser = new AuthClientUser();
                clientUser.setUserId(userId);
                clientUser.setAuthClientId(clientDetails.getId());
                clientUser.setAuthScopeId(scope.getId());
                authClientUserMapper.insert(clientUser);
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public String createAuthorizationCode(String clientIdStr, String scopeStr, User user) {
        //1. 拼装待加密字符串（clientId + scope + 当前精确到毫秒的时间戳）
        String str = clientIdStr + scopeStr + String.valueOf(DateUtils.currentTimeMillis());

        //2. SHA1加密
        String encryptedStr = EncryptUtils.sha1Hex(str);

        //3.1 保存本次请求的授权范围
        redisService.setWithExpire(encryptedStr + ":scope", scopeStr, (ExpireEnum.AUTHORIZATION_CODE.getTime()), ExpireEnum.AUTHORIZATION_CODE.getTimeUnit());
        //3.2 保存本次请求所属的用户信息
        redisService.setWithExpire(encryptedStr + ":user", user, (ExpireEnum.AUTHORIZATION_CODE.getTime()), ExpireEnum.AUTHORIZATION_CODE.getTimeUnit());

        //4. 返回Authorization Code
        return encryptedStr;
    }

    @Override
    public String createAccessToken(User user, AuthClientDetails savedClientDetails, String grantType, String scope, Long expiresIn) {
        Date current = new Date();
        //过期的时间戳
        Long expiresAt = DateUtils.nextDaysSecond(ExpireEnum.ACCESS_TOKEN.getTime(), null);

        //1. 拼装待加密字符串（username + clientId + 当前精确到毫秒的时间戳）
        String str = user.getUsername() + savedClientDetails.getClientId() + String.valueOf(DateUtils.currentTimeMillis());

        //2. SHA1加密
        String accessTokenStr = "1." + EncryptUtils.sha1Hex(str) + "." + expiresIn + "." + expiresAt;

        //3. 保存Access Token
        AuthAccessToken savedAccessToken = authAccessTokenMapper.selectByUserIdClientIdScope(user.getId()
                , savedClientDetails.getId(), scope);
        //如果存在userId + clientId + scope匹配的记录，则更新原记录，否则向数据库中插入新记录
        if (savedAccessToken != null) {
            savedAccessToken.setAccessToken(accessTokenStr);
            savedAccessToken.setExpiresIn(expiresAt);
            savedAccessToken.setUpdateUser(user.getId());
            savedAccessToken.setUpdateTime(current);
            authAccessTokenMapper.updateByPrimaryKeySelective(savedAccessToken);
        } else {
            savedAccessToken = new AuthAccessToken();
            savedAccessToken.setAccessToken(accessTokenStr);
            savedAccessToken.setUserId(user.getId());
            savedAccessToken.setUserName(user.getUsername());
            savedAccessToken.setClientId(savedClientDetails.getId());
            savedAccessToken.setExpiresIn(expiresAt);
            savedAccessToken.setScope(scope);
            savedAccessToken.setGrantType(grantType);
            savedAccessToken.setCreateUser(user.getId());
            savedAccessToken.setUpdateUser(user.getId());
            savedAccessToken.setCreateTime(current);
            savedAccessToken.setUpdateTime(current);
            authAccessTokenMapper.insertSelective(savedAccessToken);
        }

        //4. 返回Access Token
        return accessTokenStr;
    }

    @Override
    public String createRefreshToken(User user, AuthAccessToken authAccessToken) {
        Date current = new Date();
        //过期时间
        Long expiresIn = DateUtils.dayToSecond(ExpireEnum.REFRESH_TOKEN.getTime());
        //过期的时间戳
        Long expiresAt = DateUtils.nextDaysSecond(ExpireEnum.REFRESH_TOKEN.getTime(), null);

        //1. 拼装待加密字符串（username + accessToken + 当前精确到毫秒的时间戳）
        String str = user.getUsername() + authAccessToken.getAccessToken() + String.valueOf(DateUtils.currentTimeMillis());

        //2. SHA1加密
        String refreshTokenStr = "2." + EncryptUtils.sha1Hex(str) + "." + expiresIn + "." + expiresAt;

        //3. 保存Refresh Token
        AuthRefreshToken savedRefreshToken = authRefreshTokenMapper.selectByTokenId(authAccessToken.getId());
        //如果存在tokenId匹配的记录，则更新原记录，否则向数据库中插入新记录
        if (savedRefreshToken != null) {
            savedRefreshToken.setRefreshToken(refreshTokenStr);
            savedRefreshToken.setExpiresIn(expiresAt);
            savedRefreshToken.setUpdateUser(user.getId());
            savedRefreshToken.setUpdateTime(current);
            authRefreshTokenMapper.updateByPrimaryKeySelective(savedRefreshToken);
        } else {
            savedRefreshToken = new AuthRefreshToken();
            savedRefreshToken.setTokenId(authAccessToken.getId());
            savedRefreshToken.setRefreshToken(refreshTokenStr);
            savedRefreshToken.setExpiresIn(expiresAt);
            savedRefreshToken.setCreateUser(user.getId());
            savedRefreshToken.setUpdateUser(user.getId());
            savedRefreshToken.setCreateTime(current);
            savedRefreshToken.setUpdateTime(current);
            authRefreshTokenMapper.insertSelective(savedRefreshToken);
        }

        //4. 返回Refresh Token
        return refreshTokenStr;
    }

}
