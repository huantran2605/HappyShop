package com.happyshop.article;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.happyshop.UserUtility;
import com.happyshop.article.media.ArticleMediaService;
import com.happyshop.article.topic.ArticleTopicService;
import com.happyshop.common.entity.User;
import com.happyshop.common.entity.article.Article;
import com.happyshop.common.entity.article.ArticleMedia;
import com.happyshop.common.entity.article.ArticleTopic;
import com.happyshop.common.entity.article.ArticleType;

@Controller
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    ArticleTopicService articleTopicService;
    @Autowired
    ArticleService articleService;
    @Autowired
    ArticleMediaService articleMediaService;
    @Autowired
    UserUtility userUtilit;
    
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
        model.addAttribute("topic", topic);
        

        return "article/listArticle";
    }
    
    @GetMapping("/new")
    public String viewArticleForm(Article article, Model model,
            @RequestParam(name ="topicId", required = false) Integer topicId, 
            HttpServletRequest request) {
        User user = userUtilit.getAuthenticationUser(request);
        List<ArticleTopic> listTopic = articleTopicService.findAll();
        
        if(topicId != null) {
            ArticleTopic topic = articleTopicService.findById(topicId).get();
            model.addAttribute("topicName", topic.getName());            
            model.addAttribute("topicId", topic.getId());            
        }else {
            
            model.addAttribute("listTopic", listTopic);
        }
        
        model.addAttribute("article", article);
        model.addAttribute("authorName", user.getFullName());
        model.addAttribute("titlePage", "Create new article");
        return "article/article-form";
    }
    
   @PostMapping("/save")
   public String saveArticle(Article article,
           @RequestParam(name ="topicId") Integer topicId,
           @RequestParam(name = "videoUrl", required = false) String[] videoUrls, 
           @RequestParam(name = "imageName", required = false) String[] imageNames, 
           @RequestParam(name = "imageDes", required = false) String[] imageDescriptions,            
           HttpServletRequest request) {
       ArticleTopic topic = articleTopicService.findById(topicId).get();
       User user = userUtilit.getAuthenticationUser(request);
       article.setAuthor(user);       
       article.setCreatedTime(new Date());
       
       article.setType(ArticleType.FREE);
       article.setTopic(topic);
       
       articleService.save(article);
       List<ArticleMedia> media = article.getMedia();
       
       if(imageNames != null) {
           for (int i = 0; i < imageNames.length; i++) {
               ArticleMedia image = new ArticleMedia();
               image.setCreatedTime(new Date());
               image.setName(imageNames[i]);
               if(imageDescriptions.length > 0 && imageDescriptions[i] != null) {
                   image.setDescription(imageDescriptions[i]);                                    
               }
               image.setArticle(article);
               
               articleMediaService.save(image);
               media.add(image);
           }          
       }
       
       if(videoUrls != null) {
           for (String videoUrl : videoUrls) {
               ArticleMedia video = new ArticleMedia();
               video.setCreatedTime(new Date());
               video.setName(videoUrl);
               video.setArticle(article);
               
               articleMediaService.save(video);
               media.add(video);
           }          
       }
              
       articleService.save(article);
       return "redirect:/";             
   }
}

