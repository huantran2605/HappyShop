package com.happyshop.common.entity.article;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.happyshop.common.entity.abstractEntity.Media;

@Entity
@Table(name = "article_videos")
public class ArticleVideo extends Media{

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;
}
