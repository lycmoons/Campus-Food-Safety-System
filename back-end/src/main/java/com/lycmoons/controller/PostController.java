package com.lycmoons.controller;
import com.lycmoons.entity.RestBean;
import com.lycmoons.entity.dto.PostDTO;
import com.lycmoons.entity.vo.response.PostVO;
import com.lycmoons.service.PhotoService;
import com.lycmoons.service.PostService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {
    @Resource
    PostService postService;

    @Resource
    PhotoService photoService;

    // 帖子的推送
    @GetMapping("/top-post")
    public RestBean<List<PostVO>> getTopPost() {
        List<PostVO> topPost = postService.getTopPost();
        if (topPost == null) {
            return RestBean.failure(400, "帖子推送失败");
        }
        return RestBean.success(topPost);
    }

    // 发送自己的帖子
    @PostMapping("/send-post")
    public RestBean<Void> sendPost(
            HttpServletRequest request,
            @RequestPart("files") MultipartFile[] files, // 接收多个文件
            @RequestParam("title") String title,        // 接收标题字段
            @RequestParam("content") String content     // 接收内容字段
    ) {
        try {
            String photo_url = photoService.storePhoto(files);
            Integer userId = (Integer) request.getAttribute("id");
            if(title!=null&&content!=null&&photo_url!=null&&userId!=null){
                String msg = postService.addPost(new PostDTO(null, title,content, photo_url, userId, new Date()));
                if(msg==null){
                    return RestBean.success(null);
                }
            }
            return RestBean.failure(400, "帖子发送失败");
        } catch (IOException e) {
            return RestBean.failure(400, e.getMessage());
        }
    }

    // 获取所有的帖子
    @GetMapping("/all-post")
    public RestBean<List<PostVO>> getAllPost() {
        List<PostVO> allPost = postService.getAllPost();
        if (allPost == null) {
            return RestBean.failure(400, "帖子推送失败");
        }
        return RestBean.success(allPost);
    }
}
