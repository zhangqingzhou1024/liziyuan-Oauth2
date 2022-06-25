package com.liziyuan.hope.oauth.das.mapper;

import com.liziyuan.hope.oauth.das.model.SsoClientDetails;
import org.apache.ibatis.annotations.Param;

public interface SsoClientDetailsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SsoClientDetails record);

    int insertSelective(SsoClientDetails record);

    SsoClientDetails selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SsoClientDetails record);

    int updateByPrimaryKey(SsoClientDetails record);

    /**
     * 根据URL查询记录
     *
     * @param redirectUrl 回调URL
     * @return com.liziyuan.hope.oauth.db.model.SsoClientDetails
     * @author zqz
     * @date 2022/6/30 16:36
     * @since 1.0.0
     */
    SsoClientDetails selectByRedirectUrl(@Param("redirectUrl") String redirectUrl);
}