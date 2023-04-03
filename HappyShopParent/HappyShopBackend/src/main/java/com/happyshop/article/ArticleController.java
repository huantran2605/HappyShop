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
import com.happyshop.common.entity.article.Article;
import com.happyshop.common.entity.article.ArticleTopic;
import com.happyshop.question.QuestionService;

@Controller
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    ArticleTopicService articleTopicService;
    @Autowired
    ArticleService articleService;
    
    @GetMapping("/topic/{topicId}")
    public String viewArticlesFirstPage(@PathVariable("topicId") Integer topicId) {
        
        return "redirect:/article/topic/"+ topicId +"/page/1?sortField=createdTime&sortDir=des&keyWord=";
    }
    
    
    @GetMapping("/topic/{topicId}/page/{pageNum}")
    public String viewArticles(@PathVariable("topicId") Integer topicId,
            @PathVariable("pageNum") Integer pageNum,
            @Param("sortField") String sortField,
            @Param("sortDir") String sortDir,
            @Param("keyWord") String keyWord,
            Model model) {

        ArticleTopic topic = articleTopicService.findById(topicId).get();
        // sort
        Sort sort = Sort.by(sortField);
        if (sortDir.equalsIgnoreCase("asc"))
            sort = Sort.by(sortField).ascending();
        else
            sort = Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNum - 1,
                ArticleService.SIZE_PAGE_ARTICLE, sort);

        Page<Article> articlePage = articleService.findByTopic(topic, keyWord, pageable);
        
        List<Article> articles = articlePage.getContent();
        
        long startCount = (pageNum - 1) * ArticleService.SIZE_PAGE_ARTICLE + 1;
        long endCount = startCount + ArticleService.SIZE_PAGE_ARTICLE - 1 ;
        if(endCount > articlePage.getTotalElements() )
            endCount = articlePage.getTotalElements();
        
        String reserveDir = sortDir.equalsIgnoreCase("asc") ? "des"  : "asc";
        model.addAttribute("reserveDir", reserveDir);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("totalPage", articlePage.getTotalPages()); 
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
    
        
        model.addAttribute("articles", articles);
        model.addAttribute("totalElement", articlePage.getTotalElements());
        
        model.addAttribute("elementsCurrentPerPage", articlePage.getNumberOfElements());
        model.addAttribute("elementsPerPage", ArticleService.SIZE_PAGE_ARTICLE);
        
        model.addAttribute("keyWord", keyWord);
        model.addAttribute("moduleURL", "/article/topic/" + topicId);
        model.addAttribute("moduleURL", "/article/topic/" + topicId);
        model.addAttribute("topicName", topic.getName());
        

        return "article/listArticle";
    }
    
   
}
