package com.liziyuan.hope.oauth.das.mapper;

import com.liziyuan.hope.oauth.das.model.RoleFunc;

public interface RoleFuncMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RoleFunc record);

    int insertSelective(RoleFunc record);

    RoleFunc selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoleFunc record);

    int updateByPrimaryKey(RoleFunc record);
}