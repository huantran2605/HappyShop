package com.happyshop.article.topic;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.happyshop.common.entity.article.ArticleTopic;

public interface ArticleTopicService {
    int SIZE_PAGE_ARTICLE_TOPIC = 6;
    Page<ArticleTopic> findAll(String keyword, String sortDir, String sortField, int pageNum);
    
    Optional<ArticleTopic> findById(Integer id);
    
    void deleteById(Integer id);
    
    <S extends ArticleTopic> S save(S entity);
}
