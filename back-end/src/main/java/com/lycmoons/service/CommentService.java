package com.lycmoons.service;
import com.lycmoons.entity.vo.request.SendCommentVO;
import com.lycmoons.entity.vo.response.CommentVO;
import java.util.List;

public interface CommentService {
    List<CommentVO> getCommentByPostId(Integer postId);
    String addComment(SendCommentVO vo, Integer userId);
}
