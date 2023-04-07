package com.happyshop.article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.article.Article;
import com.happyshop.common.entity.article.ArticleTopic;

@Service
public class ArticleServiceImpl implements ArticleService{
    @Autowired
    ArticleRepository repo;

    public Page<Article> findByTopic(ArticleTopic topic, String keyWord, Pageable pageable) {
        if(!keyWord.isBlank() || !keyWord.isEmpty()) {
            return repo.findByTopic(topic, keyWord, pageable);
        
        }        
        return repo.findByTopic(topic, pageable);
    }

    public <S extends Article> S save(S entity) {
        return repo.save(entity);
    }
    
    
    
}
