package com.liziyuan.hope.oauth.das.mapper;

import com.liziyuan.hope.oauth.das.model.AuthRefreshToken;
import org.apache.ibatis.annotations.Param;

public interface AuthRefreshTokenMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AuthRefreshToken authRefreshToken);

    int insertSelective(AuthRefreshToken authRefreshToken);

    AuthRefreshToken selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AuthRefreshToken authRefreshToken);

    int updateByPrimaryKey(AuthRefreshToken authRefreshToken);

    /**
     * 通过tokenId查询记录
     *
     * @param tokenId tokenId
     * @return com.liziyuan.hope.oauth.db.model.AuthRefreshToken
     * @author zqz
     * @date 2022/6/20 17:27
     * @since 1.0.0
     */
    AuthRefreshToken selectByTokenId(@Param("tokenId") Integer tokenId);

    /**
     * 通过Refresh Token查询记录
     *
     * @param refreshToken Refresh Token
     * @return com.liziyuan.hope.oauth.db.model.AuthRefreshToken
     * @author zqz
     * @date 2022/6/22 11:35
     * @since 1.0.0
     */
    AuthRefreshToken selectByRefreshToken(@Param("refreshToken") String refreshToken);
}