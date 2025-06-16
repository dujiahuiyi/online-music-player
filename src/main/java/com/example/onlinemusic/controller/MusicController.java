package com.example.onlinemusic.controller;

import com.example.onlinemusic.model.Music;
import com.example.onlinemusic.model.User;
import com.example.onlinemusic.service.MusicService;
import com.example.onlinemusic.tools.Constant;
import com.example.onlinemusic.tools.ResponseBodyMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequestMapping("/music")
@RestController
public class MusicController {

    @Autowired
    private MusicService musicService;

    @RequestMapping("/upload")
    public ResponseBodyMessage<Boolean> insertMusic(@RequestParam String singer,
                                                    @RequestParam("filename") MultipartFile file,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) {

        if (!StringUtils.hasLength(singer) || file.isEmpty()) {
            return new ResponseBodyMessage<>(-1, "参数不正确", false);
        }

        // 1. 检验用户是否登录
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(Constant.USERINFO_SESSION_KEY);
        log.info("用户{}已登录", user.getId());
        try {
            Music music = musicService.insertMusic(singer, file, user);
            log.info("music={}", music);
            if (music != null) {
                response.sendRedirect("/list.html");
            }
            return new ResponseBodyMessage<>(0, "上传成功", true);
        } catch (IOException e) {
            return new ResponseBodyMessage<>(-1, "参数错误", false);
        } catch (RuntimeException e) {
            return new ResponseBodyMessage<>(-1, "上传失败", false);
        }
    }

    @RequestMapping("/get")
    public ResponseEntity<byte[]> get(@RequestParam String path) {
        if (!StringUtils.hasLength(path)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            byte[] date = musicService.get(path);
            return ResponseEntity.ok().body(date);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @RequestMapping("/delete")
    public ResponseBodyMessage<Boolean> deleteMusicById(@RequestParam String id) {
        if (!StringUtils.hasLength(id)) {
            return new ResponseBodyMessage<>(-1, "id不能为空", false);
        }
        boolean ret = musicService.deleteMusicById(id);
        if (ret) {
            return new ResponseBodyMessage<>(0, "删除音乐成功", true);
        }
        return new ResponseBodyMessage<>(-1, "删除音乐失败", false);
    }

    @RequestMapping("/deleteSel")
    public ResponseBodyMessage<Boolean> deleteSelMusic(@RequestParam("id[]") List<Integer> ids) {
        // TODO: 着重测试
        if (ids == null) {
            return new ResponseBodyMessage<>(-1, "id数组为空", false);
        }
        for (int id : ids) {
            boolean ret = musicService.deleteMusicById(id);
            if (!ret) {
                return new ResponseBodyMessage<>(-1, "删除全部音乐失败", false);
            }
        }
        return new ResponseBodyMessage<>(0, "删除音乐成功", true);
    }

    @RequestMapping("/findmusic")
    public ResponseBodyMessage<List<Music>> findMusic(@RequestParam(required = false) String musicName) {
        List<Music> musicList = null;
        if (musicName != null) {
            musicList = musicService.findMusicByName(musicName);
        } else {
            musicList = musicService.findMusic();
        }
        return new ResponseBodyMessage<>(0, "查找成功", musicList);
    }
}
