package com.happyshop.article.media;

import com.happyshop.common.entity.article.ArticleMedia;

public interface ArticleMediaService {
    <S extends ArticleMedia> S save(S entity);
}
