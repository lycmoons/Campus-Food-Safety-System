package com.lycmoons.controller;
import com.lycmoons.entity.RestBean;
import com.lycmoons.entity.dto.NewsDTO;
import com.lycmoons.entity.vo.request.DeleteNewsVO;
import com.lycmoons.entity.vo.response.NewsVO;
import com.lycmoons.mapper.NewsMapper;
import com.lycmoons.service.NewsService;
import com.lycmoons.service.PhotoService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {
    @Resource
    NewsService newsService;

    @Resource
    PhotoService photoService;

    @Resource
    NewsMapper newsMapper;

    // 新闻推送
    @GetMapping("/top-news")
    public RestBean<List<NewsVO>> getTopNews() {
        List<NewsVO> topNews = newsService.getTopNews();
        if (topNews == null) {
            return RestBean.failure(400, "新闻推送失败");
        }
        return RestBean.success(topNews);
    }

    // 获取所有的新闻内容
    @GetMapping("/all-news")
    public RestBean<List<NewsVO>> getAllNews() {
        List<NewsVO> allNews = newsService.getAllNews();
        if (allNews == null) {
            return RestBean.failure(400, "未找到任何新闻信息");
        }
        return RestBean.success(allNews);
    }

    // 删除一系列新闻内容
    @PostMapping("/delete-news")
    public RestBean<Void> deleteNewsById(HttpServletRequest request,
                                         @RequestBody DeleteNewsVO vo
    ) {
        Integer adminId = (Integer) request.getAttribute("id");
        if (adminId == null) {
            return RestBean.failure(400, "身份认证失败");
        }

        // 删除图片
        for(Integer id : vo.getNewsIds()) {
            NewsDTO n = newsMapper.getNewsById(id);
            photoService.deletePhoto(n.getPhoto_url());
        }

        // 删除数据库存储
        String msg = newsService.deleteNewsById(vo, adminId);
        if (msg == null) {
            return RestBean.success(null);
        }
        return RestBean.failure(400, msg);
    }

    // 录入一条新闻内容
    @PostMapping("/add-news")
    public RestBean<Void> addNews(HttpServletRequest request,
                                  @RequestParam("image") MultipartFile[] image,
                                  @RequestParam("title") String title,
                                  @RequestParam("content") String content
    ){
        Integer adminId = (Integer) request.getAttribute("id");
        if (adminId == null) {
            return RestBean.failure(400, "身份认证失败");
        }

        // 图片的上传
        try {
            String photo_url = photoService.storePhoto(image);

            // 信息录入
            String msg = newsService.addNews(title,content,photo_url,adminId);
            if (msg == null) {
                return RestBean.success(null);
            }
            return RestBean.failure(400, msg);
        } catch (IOException e) {
            return RestBean.failure(400, e.getMessage());
        }
    }
}
