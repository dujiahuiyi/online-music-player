package com.example.onlinemusic.mapper;

import com.example.onlinemusic.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    User selectUserByName(String username);
}
