package com.happyshop.common.entity.article;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.happyshop.common.entity.abstractEntity.Media;

@Entity
@Table(name = "article_images")
public class ArticleImage extends Media {
    
    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    public ArticleImage(String url, String description, Date createdTime, Article article) {
        super(url, description, createdTime);
        this.article = article;
    }

    public ArticleImage() {
        super();
    }
    
    
    
}
