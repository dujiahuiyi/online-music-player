package com.example.onlinemusic.service;

import com.example.onlinemusic.exception.AlreadyLikedException;
import com.example.onlinemusic.exception.OperationFailedException;
import com.example.onlinemusic.mapper.LikeMusicMapper;
import com.example.onlinemusic.model.Music;
import com.example.onlinemusic.tools.Constant;
import com.example.onlinemusic.tools.ResponseBodyMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class LikeMusicService {

    @Autowired
    LikeMusicMapper likeMusicMapper;

    public void deleteLoveMusicByMusicId(Integer musicId) {
        likeMusicMapper.deleteLoveMusicByMusicId(musicId);
    }

    public void setLikeMusic(int userId, int musicId) throws AlreadyLikedException, OperationFailedException {
        Music music = likeMusicMapper.findLikeMusicByTwoId(userId, musicId);
        // 2. 检查用户是否已经收藏过了
        if (music != null) {
            throw new AlreadyLikedException("你已经收藏过这个音乐了");
        }

        int result = likeMusicMapper.insertLikeMusic(userId, musicId);
        // 3. 检查是否插入成功
        if (result < 1) {
            throw new OperationFailedException("收藏失败");
        }

    }

    public List<Music> findLikeMusic(int userId, String musicName) {
        if (musicName == null) {
            return likeMusicMapper.findAllLikeMusic(userId);
        }
        return likeMusicMapper.findLikeMusicByMusicName(userId, musicName);
    }

    public int deleteLoveMusic(int userId, Integer musicId) {
        return likeMusicMapper.deleteLoveMusic(userId, musicId);
    }
}
