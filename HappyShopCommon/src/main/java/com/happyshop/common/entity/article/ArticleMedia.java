package com.happyshop.common.entity.article;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.happyshop.common.Constants;
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
    
    @Transient
    public String getArticleMediaKey() {
        return "article-media/" + this.name;
    }
    
    @Transient
    public String getMediaPath () {
        return Constants.S3_BASE_URI+ "/article-media/"+ this.article.getId()+"/" + this.name; 
    }
    
    
    
}
