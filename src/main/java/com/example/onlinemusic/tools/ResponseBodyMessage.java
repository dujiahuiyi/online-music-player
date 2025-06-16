package com.example.onlinemusic.tools;

import lombok.Data;

@Data
public class ResponseBodyMessage<T> {
    // TODO: 具体意义
    private int status; // 状态码

    private String Message; // 返回的信息

    private T data; // 返回的数据

    public ResponseBodyMessage(int status, String message, T data) {
        this.status = status;
        this.Message = message;
        this.data = data;
    }
}
