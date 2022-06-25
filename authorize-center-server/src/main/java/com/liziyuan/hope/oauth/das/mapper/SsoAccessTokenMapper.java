package com.liziyuan.hope.oauth.das.mapper;

import com.liziyuan.hope.oauth.das.model.SsoAccessToken;
import org.apache.ibatis.annotations.Param;

public interface SsoAccessTokenMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SsoAccessToken record);

    int insertSelective(SsoAccessToken record);

    SsoAccessToken selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SsoAccessToken record);

    int updateByPrimaryKey(SsoAccessToken record);

    /**
     * 通过用户ID查询记录
     *
     * @param userId   用户ID
     * @param clientId 请求Token的渠道
     * @return com.liziyuan.hope.oauth.db.model.SsoAccessToken
     * @author zqz
     * @date 2022/6/30 14:24
     * @since 1.0.0
     */
    SsoAccessToken selectByUserIdAndClientId(@Param("userId") Integer userId, @Param("clientId") Integer clientId);

    /**
     * 通过Access Token查询记录
     *
     * @param accessToken Access Token
     * @return com.liziyuan.hope.oauth.db.model.SsoAccessToken
     * @author zqz
     * @date 2022/6/30 14:24
     * @since 1.0.0
     */
    SsoAccessToken selectByAccessToken(@Param("accessToken") String accessToken);
}