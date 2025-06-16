package com.example.onlinemusic.controller;

import com.example.onlinemusic.exception.AlreadyLikedException;
import com.example.onlinemusic.exception.OperationFailedException;
import com.example.onlinemusic.model.Music;
import com.example.onlinemusic.model.User;
import com.example.onlinemusic.service.LikeMusicService;
import com.example.onlinemusic.tools.Constant;
import com.example.onlinemusic.tools.ResponseBodyMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/lovemusic")
public class LikeMusicController {

    @Autowired
    private LikeMusicService likeMusicService;

    @RequestMapping("/likeMusic")
    public ResponseBodyMessage<Boolean> setLikeMusic(@RequestParam("id") Integer musicId,
                                                     HttpServletRequest request) {
        if (musicId == null || musicId <= 0) {
            return new ResponseBodyMessage<>(-1, "id错误", false);
        }

        User user = (User) request.getSession(false).getAttribute(Constant.USERINFO_SESSION_KEY);
        int userId = user.getId();

        try {
            likeMusicService.setLikeMusic(userId, musicId);
            return new ResponseBodyMessage<>(0, "收藏成功", true);
        } catch (Exception e) {
            log.error("发生异常，e{}", e.getMessage());
            return new ResponseBodyMessage<>(-1, e.getMessage(), false);
        }
    }

    @RequestMapping("/findlovemusic")
    public ResponseBodyMessage<List<Music>> findLikeMusic(@RequestParam(required = false) String musicName,
                                                          HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute(Constant.USERINFO_SESSION_KEY);
        int userId = user.getId();
        List<Music> musicList = null;
        musicList = likeMusicService.findLikeMusic(userId, musicName);
        return new ResponseBodyMessage<>(0, "查找到了喜欢列表", musicList);
    }

    @RequestMapping("/deletelovemusic")
    public ResponseBodyMessage<Boolean> deleteLoveMusic(@RequestParam("id") Integer musicId, HttpServletRequest request) {
        if (musicId == null || musicId <= 0) {
            return new ResponseBodyMessage<>(-1, "id不合法", false);
        }

        User user = (User) request.getSession().getAttribute(Constant.USERINFO_SESSION_KEY);
        int userId = user.getId();
        int result = likeMusicService.deleteLoveMusic(userId, musicId);
        if (result < 1) {
            return new ResponseBodyMessage<>(-1, "删除收藏失败", false);
        }
        return new ResponseBodyMessage<>(0, "删除收藏成功", true);
    }
}
