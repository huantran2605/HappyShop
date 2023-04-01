package com.happyshop.common.entity.article;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.annotation.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "article_topics")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArticleTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, length = 300)
    private String name;
    
    @Column(nullable = false)
    private String image;
    
    @Column(nullable = false)
    private Date createdTime;
    
    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Article> articles = new ArrayList<>();

    public ArticleTopic(String name, String image, Date createdTime) {
        this.name = name;
        this.image = image;
        this.createdTime = createdTime;
    }
    
    @Transient
    public int getSizeOfArticles() {
        return this.articles.size();
    }
}

