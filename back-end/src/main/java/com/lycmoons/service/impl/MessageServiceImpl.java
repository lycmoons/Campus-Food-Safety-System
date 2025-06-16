package com.lycmoons.service.impl;
import com.lycmoons.entity.dto.MessageDTO;
import com.lycmoons.entity.vo.response.MessageVO;
import com.lycmoons.mapper.MessageMapper;
import com.lycmoons.service.MessageService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Resource
    MessageMapper messageMapper;

    @Override
    public List<MessageVO> getMessageList(Integer userId) {
        List<MessageDTO> messages = messageMapper.findMessageByUserId(userId);
        if (messages == null || messages.isEmpty()) {
            return null;
        }

        List<MessageVO> messageVOS = new ArrayList<>();
        for (MessageDTO messageDTO : messages) {
            messageVOS.add(new MessageVO(messageDTO));
        }
        return messageVOS;
    }
}
