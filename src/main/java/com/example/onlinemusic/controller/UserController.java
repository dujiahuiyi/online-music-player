package com.example.onlinemusic.controller;

import com.example.onlinemusic.model.User;
import com.example.onlinemusic.service.UserService;
import com.example.onlinemusic.tools.Constant;
import com.example.onlinemusic.tools.ResponseBodyMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public ResponseBodyMessage<User> login(@RequestParam String username, @RequestParam String password,
                                           HttpServletRequest request) {
        if (!StringUtils.hasLength(username) || !StringUtils.hasLength(password)) {
            return new ResponseBodyMessage<>(-1, "用户名或密码为空", null);
        }

        User loginUser = userService.login(username, password);
        if (loginUser == null) {
            return new ResponseBodyMessage<>(-1, "用户名或密码错误", null);
        }

        request.getSession().setAttribute(Constant.USERINFO_SESSION_KEY, loginUser);
        return new ResponseBodyMessage<>(0, "登陆成功", loginUser);
    }

}
