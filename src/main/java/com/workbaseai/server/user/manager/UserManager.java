package com.workbaseai.server.user.manager;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.workbaseai.server.user.dao.UserDO;
import com.workbaseai.server.user.dao.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class UserManager {

    private final UserMapper userMapper;

    public UserManager(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public UserDO findById(long id) {
        return userMapper.selectById(id);
    }

    public void insert(UserDO user) {
        userMapper.insert(user);
    }

    public boolean update(UserDO user) {
        String email = user.getEmail();
        int updatedRows = userMapper.update(Wrappers.<UserDO>lambdaUpdate()
                .eq(UserDO::getId, user.getId())
                .set(UserDO::getUsername, user.getUsername())
                .set(UserDO::getDisplayName, user.getDisplayName())
                .set(email != null, UserDO::getEmail, "".equals(email) ? null : email)
                .set(user.getStatus() != null, UserDO::getStatus, user.getStatus()));
        // JDBC 只统计实际变更行时，原值更新也可能返回 0。
        return updatedRows > 0 || findById(user.getId()) != null;
    }

    public boolean deleteById(long id) {
        return userMapper.deleteById(id) > 0;
    }
}
