package com.lycmoons.service;
import com.lycmoons.entity.vo.response.MessageVO;
import java.util.List;

public interface MessageService {
    List<MessageVO> getMessageList(Integer userId);
}
