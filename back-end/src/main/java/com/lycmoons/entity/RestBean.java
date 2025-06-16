package com.lycmoons.entity;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RestBean<T> {
    int code;
    T data;
    String message;

    // 服务器处理成功的响应
    public static <T> RestBean<T> success(T data) {
        return new RestBean<>(200, data, "请求成功");
    }

    // 服务器处理失败的响应
    public static <T> RestBean<T> failure(int code, String message) {
        return new RestBean<>(code, null, message);
    }

    // 手动转 JSON 的方法
    public String toJSON(){
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }
}


