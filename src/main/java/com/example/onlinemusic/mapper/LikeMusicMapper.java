package com.example.onlinemusic.mapper;

import com.example.onlinemusic.model.Music;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LikeMusicMapper {

    int deleteLoveMusicByMusicId(Integer musicId);

    Music findLikeMusicByTwoId(int userId, int musicId);

    int insertLikeMusic(int userId, int musicId);

    List<Music> findAllLikeMusic(int userId);

    List<Music> findLikeMusicByMusicName(int userId, String musicName);

    int deleteLoveMusic(int userId, Integer musicId);
}
