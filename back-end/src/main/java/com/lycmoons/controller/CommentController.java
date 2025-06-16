package com.lycmoons.controller;
import com.lycmoons.entity.RestBean;
import com.lycmoons.entity.vo.request.GetCommentVO;
import com.lycmoons.entity.vo.request.SendCommentVO;
import com.lycmoons.entity.vo.response.CommentVO;
import com.lycmoons.service.CommentService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Resource
    CommentService commentService;

    // 获取某一条帖子的全部评论
    @PostMapping("/get-comment")
    public RestBean<List<CommentVO>> getComment(@RequestBody GetCommentVO vo) {
        List<CommentVO> comments = commentService.getCommentByPostId(vo.getPost_id());

        if(comments==null){
            return RestBean.failure(400, "未找到任何评论信息");
        }
        return RestBean.success(comments);
    }

    // 发送一条评论
    @PostMapping("/send-comment")
    public RestBean<Void> sendComment(
            HttpServletRequest request,
            @RequestBody SendCommentVO vo
            ) {
        Integer userId = (Integer) request.getAttribute("id");
        String msg = commentService.addComment(vo, userId);
        if(msg==null){
            return RestBean.success(null);
        }
        return RestBean.failure(400, msg);
    }
}
