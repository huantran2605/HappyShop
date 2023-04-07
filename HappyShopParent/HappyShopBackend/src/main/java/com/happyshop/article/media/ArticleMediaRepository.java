package com.happyshop.article.media;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.article.Article;
import com.happyshop.common.entity.article.ArticleMedia;

@Repository
public interface ArticleMediaRepository extends JpaRepository<ArticleMedia, Integer> {

}
