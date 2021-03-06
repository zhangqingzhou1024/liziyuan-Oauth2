package com.liziyuan.hope.oauth.service.impl;

import com.liziyuan.hope.oauth.common.enums.ExpireEnum;
import com.liziyuan.hope.oauth.common.utils.DateUtils;
import com.liziyuan.hope.oauth.common.utils.EncryptUtils;
import com.liziyuan.hope.oauth.das.mapper.SsoAccessTokenMapper;
import com.liziyuan.hope.oauth.das.mapper.SsoClientDetailsMapper;
import com.liziyuan.hope.oauth.das.mapper.SsoRefreshTokenMapper;
import com.liziyuan.hope.oauth.das.model.SsoAccessToken;
import com.liziyuan.hope.oauth.das.model.SsoClientDetails;
import com.liziyuan.hope.oauth.das.model.SsoRefreshToken;
import com.liziyuan.hope.oauth.das.model.User;
import com.liziyuan.hope.oauth.service.SsoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author zqz
 * @date 2022/6/30
 * @since 1.0.0
 */
@Service("ssoServiceImpl")
public class SsoServiceImpl implements SsoService {
    @Resource
    private SsoAccessTokenMapper ssoAccessTokenMapper;
    @Resource
    private SsoRefreshTokenMapper ssoRefreshTokenMapper;
    @Resource
    private SsoClientDetailsMapper ssoClientDetailsMapper;

    @Override
    public SsoClientDetails selectByPrimaryKey(Integer id) {
        return ssoClientDetailsMapper.selectByPrimaryKey(id);
    }

    @Override
    public SsoClientDetails selectByRedirectUrl(String redirectUrl) {
        return ssoClientDetailsMapper.selectByRedirectUrl(redirectUrl);
    }

    @Override
    public SsoAccessToken selectByAccessId(Integer id) {
        return ssoAccessTokenMapper.selectByPrimaryKey(id);
    }

    @Override
    public SsoAccessToken selectByAccessToken(String accessToken) {
        return ssoAccessTokenMapper.selectByAccessToken(accessToken);
    }

    @Override
    public SsoRefreshToken selectByTokenId(Integer tokenId) {
        return ssoRefreshTokenMapper.selectByTokenId(tokenId);
    }

    @Override
    public SsoRefreshToken selectByRefreshToken(String refreshToken) {
        return ssoRefreshTokenMapper.selectByRefreshToken(refreshToken);
    }

    @Override
    public String createAccessToken(User user, Long expiresIn, String requestIP, SsoClientDetails ssoClientDetails) {
        Date current = new Date();
        //??????????????????
        Long expiresAt = DateUtils.nextDaysSecond(ExpireEnum.ACCESS_TOKEN.getTime(), null);

        //1. ???????????????????????????username + ??????CODE + ????????????????????????????????????
        String str = user.getUsername() + ssoClientDetails.getClientName() + String.valueOf(DateUtils.currentTimeMillis());

        //2. SHA1??????
        String accessTokenStr = "11." + EncryptUtils.sha1Hex(str) + "." + expiresIn + "." + expiresAt;

        //3. ??????Access Token
        SsoAccessToken savedAccessToken = ssoAccessTokenMapper.selectByUserIdAndClientId(user.getId(), ssoClientDetails.getId());
        //???????????????????????????????????????????????????????????????????????????????????????
        if (savedAccessToken != null) {
            savedAccessToken.setAccessToken(accessTokenStr);
            savedAccessToken.setExpiresIn(expiresAt);
            savedAccessToken.setUpdateUser(user.getId());
            savedAccessToken.setUpdateTime(current);
            ssoAccessTokenMapper.updateByPrimaryKeySelective(savedAccessToken);
        } else {
            savedAccessToken = new SsoAccessToken();
            savedAccessToken.setAccessToken(accessTokenStr);
            savedAccessToken.setUserId(user.getId());
            savedAccessToken.setUserName(user.getUsername());
            savedAccessToken.setIp(requestIP);
            savedAccessToken.setClientId(ssoClientDetails.getId());
            savedAccessToken.setChannel(ssoClientDetails.getClientName());
            savedAccessToken.setExpiresIn(expiresAt);
            savedAccessToken.setCreateUser(user.getId());
            savedAccessToken.setUpdateUser(user.getId());
            savedAccessToken.setCreateTime(current);
            savedAccessToken.setUpdateTime(current);
            ssoAccessTokenMapper.insertSelective(savedAccessToken);
        }

        //4. ??????Access Token
        return accessTokenStr;
    }

    @Override
    public String createRefreshToken(User user, SsoAccessToken ssoAccessToken) {
        Date current = new Date();
        //????????????
        Long expiresIn = DateUtils.dayToSecond(ExpireEnum.REFRESH_TOKEN.getTime());
        //??????????????????
        Long expiresAt = DateUtils.nextDaysSecond(ExpireEnum.REFRESH_TOKEN.getTime(), null);

        //1. ???????????????????????????username + accessToken + ????????????????????????????????????
        String str = user.getUsername() + ssoAccessToken.getAccessToken() + String.valueOf(DateUtils.currentTimeMillis());

        //2. SHA1??????
        String refreshTokenStr = "12." + EncryptUtils.sha1Hex(str) + "." + expiresIn + "." + expiresAt;

        //3. ??????Refresh Token
        SsoRefreshToken savedRefreshToken = ssoRefreshTokenMapper.selectByTokenId(ssoAccessToken.getId());
        //????????????tokenId???????????????????????????????????????????????????????????????????????????
        if (savedRefreshToken != null) {
            savedRefreshToken.setRefreshToken(refreshTokenStr);
            savedRefreshToken.setExpiresIn(expiresAt);
            savedRefreshToken.setUpdateUser(user.getId());
            savedRefreshToken.setUpdateTime(current);
            ssoRefreshTokenMapper.updateByPrimaryKeySelective(savedRefreshToken);
        } else {
            savedRefreshToken = new SsoRefreshToken();
            savedRefreshToken.setTokenId(ssoAccessToken.getId());
            savedRefreshToken.setRefreshToken(refreshTokenStr);
            savedRefreshToken.setExpiresIn(expiresAt);
            savedRefreshToken.setCreateUser(user.getId());
            savedRefreshToken.setUpdateUser(user.getId());
            savedRefreshToken.setCreateTime(current);
            savedRefreshToken.setUpdateTime(current);
            ssoRefreshTokenMapper.insertSelective(savedRefreshToken);
        }

        //4. ??????Refresh Tokens
        return refreshTokenStr;
    }

}
