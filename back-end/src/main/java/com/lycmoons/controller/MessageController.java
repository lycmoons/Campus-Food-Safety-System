package com.lycmoons.controller;
import com.lycmoons.entity.RestBean;
import com.lycmoons.entity.vo.response.MessageVO;
import com.lycmoons.service.MessageService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessageController {
    @Resource
    MessageService messageService;

    @GetMapping("/all-message")
    public RestBean<List<MessageVO>> getAllMessage(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("id");
        List<MessageVO> messageList = messageService.getMessageList(userId);

        if (messageList == null) {
            return RestBean.failure(400, "未找到任何系统消息");
        }
        return RestBean.success(messageList);
    }
}
