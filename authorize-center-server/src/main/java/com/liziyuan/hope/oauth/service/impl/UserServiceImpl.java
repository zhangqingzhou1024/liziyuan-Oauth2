package com.liziyuan.hope.oauth.service.impl;

import com.liziyuan.hope.oauth.common.enums.ScopeEnum;
import com.liziyuan.hope.oauth.common.utils.EncryptUtils;
import com.liziyuan.hope.oauth.common.utils.FieldUtils;
import com.liziyuan.hope.oauth.das.mapper.RoleMapper;
import com.liziyuan.hope.oauth.das.mapper.UserMapper;
import com.liziyuan.hope.oauth.das.mapper.UserRoleMapper;
import com.liziyuan.hope.oauth.das.model.Role;
import com.liziyuan.hope.oauth.das.model.User;
import com.liziyuan.hope.oauth.das.model.UserRole;
import com.liziyuan.hope.oauth.das.model.bo.RoleBo;
import com.liziyuan.hope.oauth.das.model.bo.UserBo;
import com.liziyuan.hope.oauth.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author zqz
 * @date 2022/6/3
 * @since 1.0.0
 */
@Slf4j
@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public User selectByUserId(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public boolean register(User user) {
        if (FieldUtils.isHaveEmpty(user.getUsername(), user.getPassword())) {
            throw new IllegalArgumentException("账号 & 密码 都不可为空！");
        }

        Date current = new Date();

        //密码加密存储
        user.setPassword(EncryptUtils.sha256Crypt(user.getPassword(), null));
        user.setCreateTime(current);
        user.setUpdateTime(current);
        user.setStatus(1);

        userMapper.insertSelective(user);
        log.info("用户注册 --> " + user);

        return true;
    }

    @Override
    public Map<String, Object> checkLogin(String username, String password) {
        //返回信息
        Map<String, Object> result = new HashMap<>(2);
        log.info("用户登录 --> username:{},password:{}", username, password);

        User correctUser = userMapper.selectByUsername(username);
        result.put("result", EncryptUtils.checkSha256Crypt(password, correctUser.getPassword()));
        result.put("user", correctUser);

        return result;
    }

    @Override
    public void addUserRole(Integer userId, String roleName) {
        if (userId != null && StringUtils.isNoneBlank(roleName)) {
            //1. 查询角色ID
            Role role = roleMapper.selectByRoleName(roleName);

            if (role != null) {
                UserRole savedUserRole = userRoleMapper.selectByUserIdRoleId(userId, role.getId());

                if (savedUserRole == null) {
                    //2. 给用户添加角色信息
                    UserRole userRole = new UserRole(userId, role.getId());
                    userRoleMapper.insert(userRole);
                }
            }
        }
    }

    @Override
    public User selectUserInfoByScope(Integer userId, String scope) {
        User user = userMapper.selectByPrimaryKey(userId);

        //如果是基础权限，则部分信息不返回
        if (ScopeEnum.BASIC.getCode().equalsIgnoreCase(scope)) {
            user.setPassword(null);
            user.setCreateTime(null);
            user.setUpdateTime(null);
            user.setStatus(null);
        }

        return user;
    }

    @Override
    public UserBo selectUserBoByUserId(Integer userId) {
        UserBo result = new UserBo();
        User user = userMapper.selectByPrimaryKey(userId);


        if (user != null) {
            BeanUtils.copyProperties(user, result);

            List<UserRole> userRoleList = userRoleMapper.selectByUserId(userId);

            if (userRoleList != null && userRoleList.size() > 0) {
                //查询用户对应的所有角色
                Set<RoleBo> roles = new HashSet<>();

                for (UserRole userRole : userRoleList) {
                    Role role = roleMapper.selectByPrimaryKey(userRole.getRoleId());
                    RoleBo roleBo = new RoleBo();
                    BeanUtils.copyProperties(role, roleBo);
                    //可访问的功能权限相关信息在这个demo中就省略了
                    roles.add(roleBo);
                }

                result.setRoles(roles);
            }

        }

        return result;
    }
}
