package com.happyshop.common.entity.article;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.happyshop.common.entity.abstractEntity.Media;

@Entity
@Table(name = "article_media")
public class ArticleMedia extends Media {
    
    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    public ArticleMedia(String name, String description, Date createdTime, Article article) {
        super(name, description, createdTime);
        this.article = article;
    }

    public ArticleMedia() {
        super();
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
    
    
    
}
