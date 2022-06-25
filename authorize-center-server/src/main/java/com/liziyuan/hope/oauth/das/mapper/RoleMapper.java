package com.liziyuan.hope.oauth.das.mapper;

import com.liziyuan.hope.oauth.das.model.Role;
import org.apache.ibatis.annotations.Param;

public interface RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    Role selectByRoleName(@Param("roleName") String roleName);

}