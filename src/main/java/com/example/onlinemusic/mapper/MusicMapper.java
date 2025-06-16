package com.example.onlinemusic.mapper;

import com.example.onlinemusic.model.Music;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MusicMapper {
    int insert(Music music);

    Music findMusicById(Integer id);

    int deleteMusicById(Integer id);

    List<Music> findMusicByName(String musicName);

    List<Music> findMusic();
}
