package com.happyshop.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.article.Article;
import com.happyshop.common.entity.article.ArticleTopic;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
    
    @Query("SELECT a FROM Article a WHERE a.topic = ?1 AND"
            + " CONCAT(a.title,' ', a.content,' ',a.createdTime,' ',a.author.firstName,' ',a.author.lastName) LIKE %?2%")
    public Page<Article> findByTopic(ArticleTopic topic, String keyWord, Pageable pageable);
    
    public Page<Article> findByTopic(ArticleTopic topic, Pageable pageable);
    
}
