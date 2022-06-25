package com.liziyuan.hope.oauth.service;


import com.liziyuan.hope.oauth.das.model.User;
import com.liziyuan.hope.oauth.das.model.bo.UserBo;

import java.util.Map;

/**
 * 用于相关Service
 *
 * @author zqz
 * @date 2022/6/27
 * @since 1.0.0
 */
public interface UserService {
    /**
     * 通过用户ID查询用户信息
     *
     * @param userId 用户ID
     * @return com.liziyuan.hope.oauth.db.model.User
     * @author zqz
     * @date 2022/6/23 15:07
     * @since 1.0.0
     */
    User selectByUserId(Integer userId);

    /**
     * 注册
     *
     * @param user 用户详情
     * @return boolean
     * @author zqz
     * @date 2022/6/27 10:48
     * @since 1.0.0
     */
    boolean register(User user);

    /**
     * 登录校验
     *
     * @param username 用户名
     * @param password 密码
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author zqz
     * @date 2022/6/27 10:48
     * @since 1.0.0
     */
    Map<String, Object> checkLogin(String username, String password);

    /**
     * 给用户添加角色信息
     *
     * @param userId   用户ID
     * @param roleName 角色名
     * @author zqz
     * @date 2022/6/10 17:30
     * @since 1.0.0
     */
    void addUserRole(Integer userId, String roleName);

    /**
     * 通过scope查询不同程度的用户信息
     *
     * @param userId 用户ID
     * @param scope  scope
     * @return com.liziyuan.hope.oauth.db.model.User
     * @author zqz
     * @date 2022/6/22 20:04
     * @since 1.0.0
     */
    User selectUserInfoByScope(Integer userId, String scope);

    /**
     * 通过用户ID查询用户所属角色等信息
     *
     * @param userId 用户ID
     * @return com.liziyuan.hope.oauth.db.model.bo.UserBo
     * @author zqz
     * @date 2022/6/30 15:22
     * @since 1.0.0
     */
    UserBo selectUserBoByUserId(Integer userId);
}
