package com.happyshop.common.entity.article;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.happyshop.common.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table (name="articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    
    @Column(nullable = false, length = 200)
    private String title;
        
    @Column(nullable = false)
    private String content;
    
    @Column(nullable = false)
    private Date createdTime;
    
    @Column
    private boolean published;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArticleType type;
    
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)    
    private List<ArticleMedia> media = new ArrayList<>();
        
    @Column
    private int likes;
        
    @ManyToOne
    @JoinColumn(name = "topic_id")
    private ArticleTopic topic;

    public Article(Integer id) {
        this.id = id;
    }
    
    
    
   
}
