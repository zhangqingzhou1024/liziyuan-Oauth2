package com.liziyuan.hope.oauth.das.mapper;

import com.liziyuan.hope.oauth.das.model.AuthClientDetails;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AuthClientDetailsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AuthClientDetails record);

    int insertSelective(AuthClientDetails record);

    AuthClientDetails selectByPrimaryKey(Integer id);



    int updateByPrimaryKeySelective(AuthClientDetails record);

    int updateByPrimaryKey(AuthClientDetails record);

    /**
     * 通过clientId查询接入的客户端详情
     *
     * @param clientId clientId
     * @return com.liziyuan.hope.oauth.db.model.AuthClientDetails
     * @author zqz
     * @date 2022/6/3 10:31
     * @since 1.0.0
     */
    AuthClientDetails selectByClientId(@Param("clientId") String clientId);

    List<AuthClientDetails> selectByClientName(@Param("clientName") String clientName);
}