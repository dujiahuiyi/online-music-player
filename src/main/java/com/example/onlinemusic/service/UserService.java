package com.example.onlinemusic.service;

import com.example.onlinemusic.mapper.UserMapper;
import com.example.onlinemusic.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User login(String username, String password) {
        User loginUser = userMapper.selectUserByName(username);
        if (loginUser == null) {
            return null;
        }
        boolean matches = bCryptPasswordEncoder.matches(password, loginUser.getPassword());
        if (!matches) {
            return null;
        }
        return loginUser;
    }
}
