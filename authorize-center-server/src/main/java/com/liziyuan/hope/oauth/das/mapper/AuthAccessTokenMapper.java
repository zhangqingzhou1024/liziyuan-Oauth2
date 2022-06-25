package com.liziyuan.hope.oauth.das.mapper;

import com.liziyuan.hope.oauth.das.model.AuthAccessToken;
import org.apache.ibatis.annotations.Param;

public interface AuthAccessTokenMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AuthAccessToken authAccessToken);

    int insertSelective(AuthAccessToken authAccessToken);

    AuthAccessToken selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AuthAccessToken authAccessToken);

    int updateByPrimaryKey(AuthAccessToken authAccessToken);

    /**
     * 通过userId + clientId + scope查询记录
     *
     * @param userId   用户ID
     * @param clientId 接入的客户端ID
     * @param scope    scope
     * @return com.liziyuan.hope.oauth.db.model.AuthAccessToken
     * @author zqz
     * @date 2022/6/20 11:08
     * @since 1.0.0
     */
    AuthAccessToken selectByUserIdClientIdScope(@Param("userId") Integer userId, @Param("clientId") Integer clientId, @Param("scope") String scope);

    /**
     * 通过Access Token查询记录
     *
     * @param accessToken Access Token
     * @return com.liziyuan.hope.oauth.db.model.AuthAccessToken
     * @author zqz
     * @date 2022/6/20 14:33
     * @since 1.0.0
     */
    AuthAccessToken selectByAccessToken(@Param("accessToken") String accessToken);
}