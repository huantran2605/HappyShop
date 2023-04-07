package com.happyshop.article.media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.article.ArticleMedia;

@Service
public class ArticleMediaServiceImpl implements ArticleMediaService{
    @Autowired
    ArticleMediaRepository repo;


    public <S extends ArticleMedia> S save(S entity) {
        return repo.save(entity);
    }
    
    
    
}
