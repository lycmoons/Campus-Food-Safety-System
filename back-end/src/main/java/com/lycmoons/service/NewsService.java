package com.lycmoons.service;
import com.lycmoons.entity.vo.request.DeleteNewsVO;
import com.lycmoons.entity.vo.response.NewsVO;
import java.util.List;

public interface NewsService {
    List<NewsVO> getTopNews();
    List<NewsVO> getAllNews();
    String deleteNewsById(DeleteNewsVO vo, Integer adminId);
    String addNews(String title, String content, String photo_url, Integer adminId);
}
