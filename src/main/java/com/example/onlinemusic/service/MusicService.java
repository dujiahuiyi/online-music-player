package com.example.onlinemusic.service;

import com.example.onlinemusic.mapper.MusicMapper;
import com.example.onlinemusic.model.Music;
import com.example.onlinemusic.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class MusicService {

    @Value("${music.local.path}")
    private String SAVE_PATH;
    @Autowired
    private LikeMusicService likeMusicService;
    @Autowired
    private MusicMapper musicMapper;

    public Music insertMusic(String singer, MultipartFile file,
                             User user) throws IOException {

        // 2. 上传到服务器（文件夹）
        String fileNameAndType = file.getOriginalFilename(); // xxx.mp3
        log.info("fileNameAndType={}", fileNameAndType);
        String path = Paths.get(SAVE_PATH, fileNameAndType).toString();
        File dest = new File(path);
        log.info("dest={}", dest);
        if (!dest.exists()) {
            log.info("文件夹是否存在:{}", dest.exists());
            dest.getParentFile().mkdirs();
        }

        try {
            file.transferTo(dest);
        } catch (IOException e) {
            log.info("上传服务器失败");
            throw new RuntimeException("上传服务器失败", e);
        }

        // 3. 上传到数据库
        int index = fileNameAndType.lastIndexOf(".");
        String title = fileNameAndType.substring(0, index);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = simpleDateFormat.format(new Date());

        int userId = user.getId();
        String url = "/music/get?path=" + title;

        Music music = new Music();
        music.setTitle(title);
        music.setSinger(singer);
        music.setTime(time);
        music.setUserid(userId);
        music.setUrl(url);
        log.info("title:{}", title);
        log.info("singer:{}", singer);
        log.info("time:{}", time);
        log.info("userId:{}", userId);
        log.info("url:{}", url);
        try {
            int ret = musicMapper.insert(music);
            log.info("ret:{}", ret);
            if (ret == 1) {
                log.info("正常返回");
                return music; // 返回包含自增id的music对象
            } else {
                dest.delete();
                log.error("插入失败，影响行数不为1");
                throw new RuntimeException("插入失败，影响行数不为1");
            }
        } catch (Exception e) {
            log.error("数据库插入异常，详细信息: ", e);
            dest.delete();
            throw new RuntimeException("数据库异常");
        }
    }

    public byte[] get(String path) throws IOException {
        File file = new File(SAVE_PATH + "/" + path);
        byte[] date = null;
        try {
            date = Files.readAllBytes(file.toPath());
            return date;
        } catch (IOException e) {
            throw e;
        }
    }

    public boolean deleteMusicById(String id) {
        Integer iid = Integer.parseInt(id);
        // 1. 检查id是否存在
        Music music = musicMapper.findMusicById(iid);
        if (music == null) {
            return false;
        }
        // 2，进行删除
        // 2.1 删除数据库数据
        int retForData = musicMapper.deleteMusicById(iid);
        if (retForData != 1) {
            return false;
        }
        // 2.2 删除服务器数据
        String url = music.getUrl();
        int index = url.lastIndexOf("=");
        String path = url.substring(index + 1) + ".mp3";
        File file = new File(SAVE_PATH + "/" + path);
        if (file.delete()) {
            // 删除喜欢列表的数据
            likeMusicService.deleteLoveMusicByMusicId(iid);
            return true;
        }
        return false;
    }

    public boolean deleteMusicById(int id) {
        return deleteMusicById(String.valueOf(id));
    }

    public List<Music> findMusicByName(String musicName) {
        return musicMapper.findMusicByName(musicName);
    }

    public List<Music> findMusic() {
        return musicMapper.findMusic();
    }
}
