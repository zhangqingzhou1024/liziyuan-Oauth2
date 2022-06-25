package com.liziyuan.hope.oauth.das.mapper;

import com.liziyuan.hope.oauth.das.model.SsoRefreshToken;
import org.apache.ibatis.annotations.Param;

public interface SsoRefreshTokenMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SsoRefreshToken record);

    int insertSelective(SsoRefreshToken record);

    SsoRefreshToken selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SsoRefreshToken record);

    int updateByPrimaryKey(SsoRefreshToken record);

    /**
     * 通过tokenId查询记录
     *
     * @param tokenId tokenId
     * @return com.liziyuan.hope.oauth.db.model.SsoRefreshToken
     * @author zqz
     * @date 2022/6/30 14:27
     * @since 1.0.0
     */
    SsoRefreshToken selectByTokenId(@Param("tokenId") Integer tokenId);

    /**
     * 通过Refresh Token查询记录
     *
     * @param refreshToken Refresh Token
     * @return com.liziyuan.hope.oauth.db.model.SsoRefreshToken
     * @author zqz
     * @date 2022/6/32 14:27
     * @since 1.0.0
     */
    SsoRefreshToken selectByRefreshToken(@Param("refreshToken") String refreshToken);
}