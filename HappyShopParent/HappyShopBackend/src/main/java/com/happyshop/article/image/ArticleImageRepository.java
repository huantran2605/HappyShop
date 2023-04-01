package com.happyshop.article.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.article.Article;
import com.happyshop.common.entity.article.ArticleImage;

@Repository
public interface ArticleImageRepository extends JpaRepository<ArticleImage, Integer> {

}
