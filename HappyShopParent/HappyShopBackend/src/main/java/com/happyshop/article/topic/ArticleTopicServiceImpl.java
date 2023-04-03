package com.happyshop.article.topic;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.article.ArticleTopic;

@Service
public class ArticleTopicServiceImpl implements ArticleTopicService{

    @Autowired
    ArticleTopicRepository repo;
    
    public Page<ArticleTopic> findAll(String keyword, String sortDir, String sortField, int pageNum) {
        if(!sortField.equals("total-articles")) {
            Sort sort = Sort.by(sortField);
            if(sortDir.equalsIgnoreCase("asc"))  
                sort = Sort.by(sortField).ascending();
            else  sort = Sort.by(sortField).descending(); 
            
            Pageable pageable = PageRequest.of(pageNum - 1,  
                    ArticleTopicService.SIZE_PAGE_ARTICLE_TOPIC, sort);
            
            
            if(!keyword.isBlank() || !keyword.isEmpty()) {
                return repo.findAll(keyword, pageable);                      
            }
            else{
                return repo.findAll(pageable);
            }          
        }
        else {
            Pageable pageable = PageRequest.of(pageNum - 1,  
                    ArticleTopicService.SIZE_PAGE_ARTICLE_TOPIC);
            
            if(sortDir.equals("asc")) {
                if(!keyword.isBlank() || !keyword.isEmpty()) {
                    return repo.findAllOrderByArticlesSizeAsc(keyword, pageable);                      
                }
                else {
                    return repo.findAllOrderByArticlesSizeAsc(pageable);
                }
                
            }
            else {
                if(!keyword.isBlank() || !keyword.isEmpty()) {
                    return repo.findAllOrderByArticlesSizeDesc(keyword, pageable);                      
                }
                else {
                    return repo.findAllOrderByArticlesSizeDesc(pageable);
                }
            }
        }
                
    }

    public Optional<ArticleTopic> findById(Integer id) {
        return repo.findById(id);
    }

    public void deleteById(Integer id) {
        repo.deleteById(id);
    }

    public <S extends ArticleTopic> S save(S entity) {
        return repo.save(entity);
    }
    
    
    
    
    
    
}
