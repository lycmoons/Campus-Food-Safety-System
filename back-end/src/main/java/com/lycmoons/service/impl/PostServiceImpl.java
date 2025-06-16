package com.lycmoons.service.impl;
import com.lycmoons.entity.dto.PostDTO;
import com.lycmoons.entity.vo.response.PostVO;
import com.lycmoons.mapper.AccountMapper;
import com.lycmoons.mapper.PostMapper;
import com.lycmoons.service.PostService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Resource
    PostMapper postMapper;

    @Resource
    AccountMapper accountMapper;

    @Override
    public List<PostVO> getTopPost() {
        List<PostDTO> topPost = postMapper.getTopPost();
        if (topPost == null || topPost.isEmpty()) {
            return null;
        }

        List<PostVO> postVOS = new ArrayList<>();
        for (PostDTO postDTO : topPost) {
            String username = accountMapper.findAccountById(postDTO.getUser_id()).getUsername();
            postVOS.add(new PostVO(postDTO, username));
        }
        return postVOS;
    }

    @Override
    public List<PostVO> getAllPost() {
        List<PostDTO> allPost = postMapper.getAllPost();
        if (allPost == null || allPost.isEmpty()) {
            return null;
        }

        List<PostVO> postVOS = new ArrayList<>();
        for (PostDTO postDTO : allPost) {
            String username = accountMapper.findAccountById(postDTO.getUser_id()).getUsername();
            postVOS.add(new PostVO(postDTO, username));
        }
        return postVOS;
    }

    @Override
    public String addPost(PostDTO dto) {
        int cnt = postMapper.addPost(dto);
        if(cnt > 0){
            return null;
        }
        else{
            return "帖子发布失败";
        }
    }
}
