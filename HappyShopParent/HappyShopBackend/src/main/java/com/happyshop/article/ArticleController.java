package com.happyshop.article;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.happyshop.UserUtility;
import com.happyshop.admin.AmazonS3Util;
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
    UserUtility userUtility;  
    
    public String getDefaultUrl(int topicId) {
        return "redirect:/article/topic/"+ topicId +"/page/1?sortField=createdTime&sortDir=des&keyWord=";
    }
    
    @GetMapping("/topic/{topicId}")
    public String viewArticlesFirstPage(@PathVariable("topicId") Integer topicId) {
        
        return getDefaultUrl(topicId);
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
        User user = userUtility.getAuthenticationUser(request);
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
    
//   @PostMapping("/upload-media")
//   public String uploadMedia(@RequestParam(name="imageFile") MultipartFile[] imageMultipart,
//           @RequestParam(name="videoFile") MultipartFile[] videoMultipart,
//           Model model, @RequestParam(name ="topicId") Integer topicId,
//           HttpServletRequest request) throws IOException {
//       if(imageMultipart != null || videoMultipart != null) {
//           Article article = new Article();
//           article.setTitle("no title");
//           article.setContent("no content");
//           article.setCreatedTime(new Date());
//           User user = userUtility.getAuthenticationUser(request);   
//           article.setAuthor(user);
//           ArticleTopic topic =  articleTopicService.findById(topicId).get();
//           article.setTopic(topic);
//           article.setType(ArticleType.FREE);
//           Article savedArticle = articleService.save(article);
//           String imageFileDir = "article-media/" + savedArticle.getId() + "/image";
//           String videoFileDir = "article-media/" + savedArticle.getId() + "/video";          
//           
//           //upload image
//           if(imageMultipart != null) {              
//               for (MultipartFile multipartFile : imageMultipart) {
//                   if(!multipartFile.isEmpty()) {
//                       String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//                       AmazonS3Util.uploadFile(imageFileDir, fileName, multipartFile.getInputStream());
//                   }
//               }
//           }
//           //upload video
//           if(videoMultipart != null) {
//               for (MultipartFile multipartFile : videoMultipart) {
//                   if(!multipartFile.isEmpty()) {
//                       String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//                       AmazonS3Util.uploadFile(videoFileDir, fileName, multipartFile.getInputStream());
//                   }
//               }               
//           }
//           model.addAttribute("article", savedArticle);
//           model.addAttribute("topicName", articleTopicService.findById(topicId).get().getName());
//           model.addAttribute("titlePage", "Create new article");
//           model.addAttribute("topicId", topic.getId());
//       }
//      
//       return "article/article-form";
//   }
    
   @PostMapping("/save")
   public String saveArticle(Article article,
           @RequestParam(name ="topicId") Integer topicId,
           HttpServletRequest request) {
       System.out.println(topicId);
//       ArticleTopic topic = articleTopicService.findById(topicId).get();
//       User user = userUtility.getAuthenticationUser(request);
//      
//       
//       if(article.getId() == null) {
//           article.setAuthor(user);       
//           article.setCreatedTime(new Date());
//           article.setType(ArticleType.FREE);
//           article.setTopic(topic);  
//           
//           articleService.save(article);
//           
//           List<ArticleMedia> media = article.getMedia();
//            
//           if (imageNames != null) {
//               for (int i = 0; i < imageNames.length; i++) {
//                   ArticleMedia image = new ArticleMedia();
//                   image.setCreatedTime(new Date());
//                   image.setName(imageNames[i]);
//                   if (imageDescriptions.length > 0 && imageDescriptions[i] != null) {
//                       image.setDescription(imageDescriptions[i]);
//                   }
//                   image.setArticle(article);
//
//                   articleMediaService.save(image);
//                   media.add(image);
//               }
//           }
//
//           if (videoUrls != null) {
//               for (String videoUrl : videoUrls) {
//                   ArticleMedia video = new ArticleMedia();
//                   video.setCreatedTime(new Date());
//                   video.setName(videoUrl);
//                   video.setArticle(article);
//
//                   articleMediaService.save(video);
//                   media.add(video);
//               }
//           }
//           
//           
//       }else {
//           Article articleInDb = articleService.findById(article.getId()).get();
//           articleInDb.setTitle(article.getTitle());
//           articleInDb.setContent(article.getContent());
//           articleInDb.setPublished(article.isPublished());         
//           
//           //delete image, video files on cloud
//           List<ArticleMedia> articleMedia =  articleInDb.getMedia(); 
//           for (ArticleMedia aM : articleMedia) {
//               String objectKey = aM.getArticleMediaKey(); 
//               AmazonS3Util.deleteFile(objectKey);
//           }
//           //upload changed image, video to cloud
//           //author upload manually
//           
//           articleService.save(articleInDb);
//           //delete all old media and update
//           List<ArticleMedia> media = article.getMedia();
//           media.clear();
//           
//           if (imageNames != null) {
//               for (int i = 0; i < imageNames.length; i++) {
//                   ArticleMedia image = new ArticleMedia();
//                   image.setCreatedTime(new Date());
//                   image.setName(imageNames[i]);
//                   if (imageDescriptions.length > 0 && imageDescriptions[i] != null) {
//                       image.setDescription(imageDescriptions[i]);
//                   }
//                   image.setArticle(articleInDb);
//
//                   articleMediaService.save(image);
//                   media.add(image);
//               }
//           }
//
//           if (videoUrls != null) {
//               for (String videoUrl : videoUrls) {
//                   ArticleMedia video = new ArticleMedia();
//                   video.setCreatedTime(new Date());
//                   video.setName(videoUrl);
//                   video.setArticle(articleInDb);
//
//                   articleMediaService.save(video);
//                   media.add(video);
//               }
//           }
//           
//           
//       }
//      
//              
//       return getDefaultUrl(topicId);
       return "";
   }
   
   @GetMapping("/published/{id}")
   private String updateEnableStatus(@PathVariable("id") Integer articleId,
           @RequestParam(name ="topicId") Integer topicId,
           RedirectAttributes re){
       Article article =  articleService.findById(articleId).get();
       if (article == null) {
           re.addFlashAttribute("message", "The article is not exist!");
           return getDefaultUrl(topicId);
       }
       else {
           if(article.isPublished()) {
               articleService.updatePublishedById(false, articleId);
           }
           else {
               articleService.updatePublishedById(true, articleId);
           }
           re.addFlashAttribute("message", "Update Published Status Successfully!");
       }
       return getDefaultUrl(topicId);
   }
   
   @GetMapping("/detail/{id}")
   private String viewArticleDetail(@PathVariable("id") Integer articleId, Model model) {
       
       Article article =  articleService.findById(articleId).get();
       
       
       model.addAttribute("article", article);
       return  "article/article-detail";
   }
   
   @GetMapping("/delete/{id}")
   private String deleteArticle(@PathVariable("id") Integer articleId,
           @RequestParam(name ="topicId") Integer topicId, RedirectAttributes re) {
       
//       //delete image, video files on cloud
//       Article article =  articleService.findById(articleId).get();
//       List<ArticleMedia> media =  article.getMedia();
//       for (ArticleMedia articleMedia : media) {
//           String objectKey = articleMedia.getArticleMediaKey();
//           AmazonS3Util.deleteFile(objectKey);
//       }
//       
//       articleService.deleteById(articleId);
//       re.addFlashAttribute("message", "delete article id " +articleId + " successfully!");    
//       
//       return getDefaultUrl(topicId);
       return "";
   }
   
   @GetMapping("/update/{id}")
   private String updateArticle(@PathVariable("id") Integer articleId,
           @RequestParam(name ="topicId") Integer topicId,
           Model model, HttpServletRequest request) {
       Article article =  articleService.findById(articleId).get();
       model.addAttribute("article", article);
       model.addAttribute("titlePage", "Update article");
       
       User user = userUtility.getAuthenticationUser(request);
       model.addAttribute("authorName", user.getFullName());
       
       ArticleTopic topic = articleTopicService.findById(topicId).get();
       model.addAttribute("topicName", topic.getName());        
       model.addAttribute("topicId", topicId);
       
       
       model.addAttribute("media", article.getMedia());
       return "article/article-form";
   }
       
   
}

