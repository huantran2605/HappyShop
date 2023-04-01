package com.happyshop.article.topic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.happyshop.common.entity.article.ArticleTopic;

public interface ArticleTopicService {
    int SIZE_PAGE_ARTICLE_TOPIC = 2;
    Page<ArticleTopic> findAll(String keyword, String sortDir, String sortField, int pageNum);
}
