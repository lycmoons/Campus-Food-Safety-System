package com.lycmoons.service;
import com.lycmoons.entity.dto.PostDTO;
import com.lycmoons.entity.vo.response.PostVO;
import java.util.List;

public interface PostService {
    List<PostVO> getTopPost();
    List<PostVO> getAllPost();
    String addPost(PostDTO dto);
}
