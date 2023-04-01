package com.happyshop.article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.article.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
    
}
