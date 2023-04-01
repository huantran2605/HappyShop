package com.happyshop.article.topic;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.article.ArticleTopic;

@Repository
public interface ArticleTopicRepository extends JpaRepository<ArticleTopic, Integer> {
    
    @Query("SELECT aT FROM ArticleTopic aT WHERE CONCAT(aT.name,' ',aT.createdTime) LIKE %?1%")
    public Page<ArticleTopic> findAll(String keyWord, Pageable pageable); 
    
    @Query("SELECT aT FROM ArticleTopic aT WHERE CONCAT(aT.name,' ',aT.createdTime) LIKE %?1% ORDER BY SIZE(aT.articles) DESC")
    public Page<ArticleTopic> findAllOrderByArticlesSizeDesc(String keyWord, Pageable pageable);
    
    @Query("SELECT aT FROM ArticleTopic aT WHERE CONCAT(aT.name,' ',aT.createdTime) LIKE %?1% ORDER BY SIZE(aT.articles) ASC")
    public Page<ArticleTopic> findAllOrderByArticlesSizeAsc(String keyWord, Pageable pageable);
    
    @Query("SELECT aT FROM ArticleTopic aT ORDER BY SIZE(aT.articles) DESC")
    public Page<ArticleTopic> findAllOrderByArticlesSizeDesc(Pageable pageable);
    
    @Query("SELECT aT FROM ArticleTopic aT ORDER BY SIZE(aT.articles) ASC")
    public Page<ArticleTopic> findAllOrderByArticlesSizeAsc(Pageable pageable);

}
