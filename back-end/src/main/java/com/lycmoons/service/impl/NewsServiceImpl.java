package com.lycmoons.service.impl;
import com.lycmoons.entity.dto.LogDTO;
import com.lycmoons.entity.dto.NewsDTO;
import com.lycmoons.entity.vo.request.DeleteNewsVO;
import com.lycmoons.entity.vo.response.NewsVO;
import com.lycmoons.mapper.LogMapper;
import com.lycmoons.mapper.NewsMapper;
import com.lycmoons.service.NewsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {
    @Resource
    NewsMapper newsMapper;

    @Resource
    LogMapper logMapper;

    @Override
    public List<NewsVO> getTopNews() {
        List<NewsDTO> topNews = newsMapper.getTopNews();
        if (topNews == null || topNews.isEmpty()) {
            return null;
        }

        List<NewsVO> topNewsVO = new ArrayList<>();
        for (NewsDTO newsDTO : topNews) {
            topNewsVO.add(new NewsVO(newsDTO));
        }

        return topNewsVO;
    }

    @Override
    public List<NewsVO> getAllNews() {
        List<NewsDTO> allNews = newsMapper.getAllNews();
        if (allNews == null || allNews.isEmpty()) {
            return null;
        }
        List<NewsVO> allNewsVO = new ArrayList<>();
        for (NewsDTO newsDTO : allNews) {
            allNewsVO.add(new NewsVO(newsDTO));
        }
        return allNewsVO;
    }

    @Override
    public String deleteNewsById(DeleteNewsVO vo, Integer adminId) {
        int cnt = 0;
        for(Integer id : vo.getNewsIds()){
            cnt += newsMapper.deleteNews(id);
        }
        if (cnt <= 0) {
            return "新闻信息删除失败";
        }
        else {
            // 管理员日志
            logMapper.addLog(new LogDTO(null,adminId,"News","新闻信息删除",new Date()));
            return null;
        }
    }

    @Override
    public String addNews(String title, String content, String photo_url, Integer adminId) {
        // 录入新闻信息
        int cnt = newsMapper.insertNews(new NewsDTO(null,title,content,new Date(),photo_url));
        if (cnt <= 0) {
            return "新闻信息录入失败";
        }

        // 管理员日志
        logMapper.addLog(new LogDTO(null,adminId,"News","新闻信息录入",new Date()));
        return null;
    }
}
