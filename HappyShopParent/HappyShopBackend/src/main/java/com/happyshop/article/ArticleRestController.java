package com.happyshop.article;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.happyshop.UserUtility;
import com.happyshop.admin.AmazonS3Util;
import com.happyshop.article.media.ArticleMediaService;
import com.happyshop.article.topic.ArticleTopicService;
import com.happyshop.common.Constants;
import com.happyshop.common.entity.User;
import com.happyshop.common.entity.article.Article;
import com.happyshop.common.entity.article.ArticleMedia;
import com.happyshop.common.entity.article.ArticleTopic;
import com.happyshop.common.entity.article.ArticleType;

@RestController
public class ArticleRestController {
    @Autowired
    UserUtility userUtility;   
    @Autowired
    ArticleTopicService articleTopicService;
    @Autowired
    ArticleService articleService;
    @Autowired
    ArticleMediaService articleMediaService;
    
    @PostMapping("/article/upload-media")
    public ResponseEntity<Response> uploadMedia(@RequestParam(name="files") MultipartFile[] multipartFiles,
            @RequestParam("topicId") Integer topicId,
            @RequestParam(name = "articleId", required = false) Integer articleId,
            HttpServletRequest request) throws IOException{
        String fileDir = "";
        List<String> urls = new ArrayList<>();
        int articleIdResponse = 0;
        if(articleId != null) {
            Article articleInDb = articleService.findById(articleId).get();
            fileDir = "article-media/" + articleInDb.getId();
            
            urls = uploadMediaToServer(articleInDb, multipartFiles, fileDir);
            setArticleMedia(articleInDb, multipartFiles);
            articleIdResponse = articleId;
        }else {
            Article newArticle = new Article();
            newArticle.setTitle("no title");
            newArticle.setContent("no content");
            newArticle.setCreatedTime(new Date());
            
            User user = userUtility.getAuthenticationUser(request);   
            newArticle.setAuthor(user); 
            
            ArticleTopic topic = articleTopicService.findById(topicId).get();
            newArticle.setTopic(topic);
            
            newArticle.setType(ArticleType.FREE);
            Article savedArticle = articleService.save(newArticle);
            
            fileDir = "article-media/" + savedArticle.getId();
            
            articleIdResponse = savedArticle.getId();
            urls = uploadMediaToServer(savedArticle, multipartFiles, fileDir);
            setArticleMedia(savedArticle, multipartFiles);
        }
                      
        return ResponseEntity.ok(new Response(urls, articleIdResponse));
    }
    
    class Response {
        private List<String> urls;
        private int articleId;
        public List<String> getUrls() {
            return urls; 
        }
        public void setUrls(List<String> stringList) {
            this.urls = stringList;
        }
        public int getArticleId() {
            return articleId;
        }
        public void setArticleId(int articleId) {
            this.articleId = articleId;
        }
        public Response(List<String> urls, int articleId) {
            this.urls = urls;
            this.articleId = articleId;
        } 
            
    }
    
    public void setArticleMedia(Article article, MultipartFile[] multipartFiles) {
       //add image to list images of article
        List<ArticleMedia> listMedia = article.getMedia();
        for (MultipartFile multipartFile : multipartFiles) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            
            if(!checkDuplicateMedia(listMedia, fileName)) {                
                ArticleMedia media = new ArticleMedia();
                media.setCreatedTime(new Date());
                media.setName(fileName);
                media.setArticle(article);
                articleMediaService.save(media); 
                listMedia.add(media);
            }
            
        }
    }
    
    public boolean checkDuplicateMedia(List<ArticleMedia> listMedia, String fileName) {
        for (ArticleMedia articleMedia : listMedia) {
            if(articleMedia.getName().equals(fileName)) {                
                return true;
            }
        }
        
        return false;
    }
    
    public List<String> uploadMediaToServer(Article article, MultipartFile[] multipartFiles, String fileDir)
            throws IOException {
        List<String> urls = new ArrayList<>();
        List<ArticleMedia> listMedia = article.getMedia();
        // upload media to server
        for (MultipartFile multipartFile : multipartFiles) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            if (!checkDuplicateMedia(listMedia, fileName)) {
                AmazonS3Util.uploadFile(fileDir, fileName, multipartFile.getInputStream());
                urls.add(Constants.S3_BASE_URI + "/" + fileDir + "/" + fileName);
            }
            

        }
        for (String string : urls) {
            System.out.println(string);
        }
        return urls;
    }
    
    @PostMapping("/article/delete-uploaded-media")
    public ResponseEntity<String> deleteUploadedMedia(@RequestParam("mediaUrl") String mediaUrl) {
        
        String[] stringArray = mediaUrl.split("/");
        int articleId = Integer.parseInt(stringArray[stringArray.length-2]);
        Article article = articleService.findById(articleId).get();
        String key = stringArray[stringArray.length-3] +"/"+ stringArray[stringArray.length-2]+"/" +
                stringArray[stringArray.length-1];
        AmazonS3Util.deleteFile(key);
        
        List<ArticleMedia> listMedia = article.getMedia();
        for (ArticleMedia articleMedia : listMedia) {
            if(articleMedia.getName().equals(stringArray[stringArray.length-1])) {
                listMedia.remove(articleMedia);
                break;
            }
        }
        article.setMedia(listMedia);
        articleService.save(article);
        return ResponseEntity.ok("delete uploaded media successfully!");
    }
    
}
