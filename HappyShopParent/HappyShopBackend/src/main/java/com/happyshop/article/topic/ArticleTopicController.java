package com.happyshop.article.topic;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.happyshop.admin.AmazonS3Util;
import com.happyshop.common.entity.Brand;
import com.happyshop.common.entity.article.ArticleTopic;

@Controller
@RequestMapping("/article-topic")
public class ArticleTopicController {
    @Autowired
    ArticleTopicService articleTopicService;
    
    String defaultUrl = "redirect:/article-topic/";
    
    @GetMapping("")
    public String viewArticleTopics() {
       
        return "redirect:/article-topic/page/1?sortField=createdTime&sortDir=des&keyWord=";
    }
    
    @GetMapping("/page/{pageNum}")
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
        model.addAttribute("moduleURL", "/article-topic");
        
        return"article/listArticle";
    }
    
    @GetMapping("/delete/{id}")
    private String deleteArticleTopic(@PathVariable("id") Integer topicId,
            RedirectAttributes re,Model model){
        Optional<ArticleTopic> topic =  articleTopicService.findById(topicId);
        
        if (topic.isEmpty()) {
            re.addFlashAttribute("message", "The topic is not exist!");
        }
        else {    
            articleTopicService.deleteById(topicId); 
            
            re.addFlashAttribute("message","Delete topic id: "+ topicId + " successfully!");           
        }
        return defaultUrl;
    
    }
    
    @GetMapping("/update/{id}")
    private String updateArticleTopic(@PathVariable("id") Integer  topicId, Model model,
            RedirectAttributes ra) {
        Optional<ArticleTopic> topic =  articleTopicService.findById(topicId);
        if (topic.isEmpty()) {
            ra.addFlashAttribute("message", "The topic is not exist!");
            return "redirect:/article-topic/";
        }
        else {
            model.addAttribute("topic", topic.get());
            model.addAttribute("titlePage", "Update Article Topic");
        }
              
        return "article/article-topic-form";

    }
    
    @PostMapping("/save")
    public String saveArticleTopic(ArticleTopic topic,
            RedirectAttributes ra,@RequestParam("imageFile") MultipartFile multipartFile) throws IOException {
        
        System.out.println(topic.getId() + "------------------------");
        ArticleTopic topicInDb = new ArticleTopic();
        if(topic.getId() != null) {
            topicInDb = articleTopicService.findById(topic.getId()).get();            
        }
        topic.setName(topic.getName().replaceAll("^\\s+|\\s+$", ""));
        
        if(!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            topic.setImage(fileName);
            
            if(topic.getId() == null) {
                topic.setCreatedTime(new Date());
            }
            else {
                topic.setCreatedTime(topicInDb.getCreatedTime());
            }
            
            ArticleTopic topicSaved = articleTopicService.save(topic);
            System.out.println("00000000000000000 " + topicSaved.getId());
            String uploadDir = "article-topic-images/" + topicSaved.getId();
            // delete old image and upload
            AmazonS3Util.removeFolder(uploadDir);
            AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());          
        }
        else {
            topic.setImage(topicInDb.getImage());     
            topic.setCreatedTime(topicInDb.getCreatedTime());
            articleTopicService.save(topic);        
        }
        
        return defaultUrl;
    }
    
    @GetMapping("/new")
    private String createArticleTopic(ArticleTopic topic,Model model) {
        model.addAttribute("topic", topic);   
        model.addAttribute("titlePage", "Create New Brand");
        return "article/article-topic-form";
    }
}
