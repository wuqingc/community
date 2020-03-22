package com.lele.community.service;

import com.lele.community.mapper.UserMapper;
import com.lele.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void updateOrInsert(User user) {
        User dbUser = userMapper.selectByCountId(user.getAccount_id());
        if (dbUser == null){
            userMapper.insert(user);
        } else {
            dbUser.setToken(user.getToken());
            dbUser.setGmt_modified(System.currentTimeMillis());
            dbUser.setAvatar_url(user.getAvatar_url());
            userMapper.update(dbUser);
        }
    }
}
