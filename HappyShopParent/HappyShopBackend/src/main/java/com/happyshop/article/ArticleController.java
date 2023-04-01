package com.happyshop.article;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.happyshop.article.topic.ArticleTopicService;
import com.happyshop.common.entity.article.ArticleTopic;

@Controller
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    ArticleTopicService articleTopicService;
    
    
    @GetMapping("/listArticle")
    public String viewArticleTopics() {
       
        return "redirect:/article/free-type/topic/page/1?sortField=createdTime&sortDir=des&keyWord=";
    }
    
    @GetMapping("/free-type/topic/page/{pageNum}")
    private String articleTopicPage (@PathVariable ("pageNum") Integer pageNum,
            @Param("sortField") String sortField,
            @Param("sortDir") String sortDir,
            @Param("keyWord") String keyWord, 
            Model model) {
        
        Page<ArticleTopic> pageArticleTopic = articleTopicService.findAll(keyWord, sortDir, sortField, pageNum);             
              
        List<ArticleTopic> articleTopics = pageArticleTopic.getContent();
        
        long startCount = (pageNum - 1) * ArticleTopicService.SIZE_PAGE_ARTICLE_TOPIC + 1;
        long endCount = startCount + ArticleTopicService.SIZE_PAGE_ARTICLE_TOPIC - 1 ;
        if(endCount > pageArticleTopic.getTotalElements() )
            endCount = pageArticleTopic.getTotalElements();
        
        String reserveDir = sortDir.equalsIgnoreCase("asc") ? "des"  : "asc";
        model.addAttribute("reserveDir", reserveDir);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("totalPage", pageArticleTopic.getTotalPages()); 
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
    
        
        model.addAttribute("topics", articleTopics);
        model.addAttribute("totalElement", pageArticleTopic.getTotalElements());
        
        model.addAttribute("elementsCurrentPerPage", pageArticleTopic.getNumberOfElements());
        model.addAttribute("elementsPerPage", ArticleTopicService.SIZE_PAGE_ARTICLE_TOPIC);
        
        model.addAttribute("keyWord", keyWord);
        model.addAttribute("moduleURL", "/article/free-type/topic");
        
        return"article/listArticle";
    }
}
