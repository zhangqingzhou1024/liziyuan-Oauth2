package com.liziyuan.hope.oauth.das.mapper;

import com.liziyuan.hope.oauth.das.model.UserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserRoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserRole record);

    int insertSelective(UserRole record);

    UserRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserRole record);

    int updateByPrimaryKey(UserRole record);

    /**
     * 通过用户ID和角色ID查询用户角色信息
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return com.liziyuan.hope.oauth.db.model.UserRole
     * @author zqz
     * @date 2022/6/18 11:10
     * @since 1.0.0
     */
    UserRole selectByUserIdRoleId(@Param("userId") Integer userId, @Param("roleId") Integer roleId);

    /**
     * 通过角色名查询用户角色信息
     *
     * @param roleName 角色名
     * @return com.liziyuan.hope.oauth.db.model.UserRole
     * @author zqz
     * @date 2022/6/18 11:10
     * @since 1.0.0
     */
    UserRole selectByRoleName(@Param("roleName") String roleName);

    /**
     * 通过用户ID查询用户角色信息
     *
     * @param userId 用户ID
     * @return com.liziyuan.hope.oauth.db.model.UserRole
     * @author zqz
     * @date 2022/6/18 11:10
     * @since 1.0.0
     */
    List<UserRole> selectByUserId(@Param("userId") Integer userId);
}