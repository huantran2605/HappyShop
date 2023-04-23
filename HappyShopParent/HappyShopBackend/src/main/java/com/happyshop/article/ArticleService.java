package com.happyshop.article;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.happyshop.common.entity.article.Article;
import com.happyshop.common.entity.article.ArticleTopic;

public interface ArticleService {
    int SIZE_PAGE_ARTICLE = 10;
    
    Page<Article> findByTopic(ArticleTopic topic, String keyWord, Pageable pageable);
    
    <S extends Article> S save(S entity);
    
    Optional<Article> findById(Integer id);
    
    int updatePublishedById(boolean published, Integer id);
    
    void deleteById(Integer id);
}
