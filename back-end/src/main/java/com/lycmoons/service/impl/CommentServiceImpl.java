package com.lycmoons.service.impl;
import com.lycmoons.entity.dto.AccountDTO;
import com.lycmoons.entity.dto.CommentDTO;
import com.lycmoons.entity.vo.request.SendCommentVO;
import com.lycmoons.entity.vo.response.CommentVO;
import com.lycmoons.mapper.AccountMapper;
import com.lycmoons.mapper.CommentMapper;
import com.lycmoons.service.CommentService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Resource
    CommentMapper commentMapper;

    @Resource
    AccountMapper accountMapper;

    @Override
    public List<CommentVO> getCommentByPostId(Integer postId) {
        List<CommentDTO> comments = commentMapper.getCommentByPostId(postId);
        if (comments == null || comments.isEmpty()) {
            return null;
        }

        List<CommentVO> commentVOS = new ArrayList<>();
        for (CommentDTO commentDTO : comments) {
            AccountDTO account = accountMapper.findAccountById(commentDTO.getUser_id());
            if (account == null) {
                continue;
            }
            commentVOS.add(new CommentVO(commentDTO, account.getUsername()));
        }

        return commentVOS;
    }

    @Override
    public String addComment(SendCommentVO vo, Integer userId) {
        int cnt = commentMapper.addComment(new CommentDTO(null, userId, vo.getPost_id(), vo.getContent(), new Date()));
        if (cnt > 0) {
            return null;
        }
        else {
            return "评论发送失败";
        }
    }
}
