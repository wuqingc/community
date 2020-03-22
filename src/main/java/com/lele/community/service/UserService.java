package com.lele.community.service;

import com.lele.community.mapper.UserMapper;
import com.lele.community.model.User;
import com.lele.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void updateOrInsert(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andTokenEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() == 0){
            userMapper.insert(user);
        } else {
            User dbUser = users.get(0);

            User updateUser = new User();
            updateUser.setToken(user.getToken());
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setAvatarUrl(user.getAvatarUrl());

            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser,example);
        }
    }
}
