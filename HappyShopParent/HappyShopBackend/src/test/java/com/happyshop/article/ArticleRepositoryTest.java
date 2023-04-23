package com.happyshop.article;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.happyshop.article.media.ArticleMediaRepository;
import com.happyshop.article.topic.ArticleTopicRepository;
import com.happyshop.common.entity.User;
import com.happyshop.common.entity.article.Article;
import com.happyshop.common.entity.article.ArticleMedia;
import com.happyshop.common.entity.article.ArticleTopic;
import com.happyshop.common.entity.article.ArticleType;

@DataJpaTest  
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ArticleRepositoryTest {
    @Autowired
    private ArticleRepository articleRepo;
    @Autowired
    private ArticleMediaRepository articleImageRepo;
    @Autowired
    private ArticleTopicRepository articleTopicRepo;
    
    @Test
    public  void testCreateArticleTopic() {
        ArticleTopic aT = new ArticleTopic("topic1", "https://images.samsung.com/is/image/"
                + "samsung/assets/vn/2208/pfs/01-hd02-B4-kv-pc-1440x640.jpg?imwidth=1366", new Date());
        articleTopicRepo.save(aT);
        assertThat(aT.getId()).isGreaterThan(0);
    }
    
    @Test
    public  void testCreateArticleImage() {
        ArticleMedia aI = new ArticleMedia("https://cdn2.cellphones.com.vn/358x358,webp,q100/media/"
                + "catalog/product/s/m/sm-s908_galaxys22ultra_front_burgundy_211119_2.jpg",
                "description1", new Date(), new Article(4));
        articleImageRepo.save(aI);
        assertThat(aI.getId()).isGreaterThan(0);
    }
    
    @Test
    public  void testCreateArticle() {
       Article a = new Article();
       a.setTitle("title1");
       a.setContent("content1");
       a.setCreatedTime(new Date());
       a.setType(ArticleType.FREE);
       a.setAuthor(new User((long) 1));
       
       articleRepo.save(a);
       
       //assertThat().isNotNull();
       System.out.println(a.getTitle() + " " + a.getContent() + a.getAuthor().getFullName());
    }
    
    @Test
    public void testArticleMedia() {
        Article article = articleRepo.findById(60).get();
        List<ArticleMedia> listMedia = article.getMedia();
        for (ArticleMedia articleMedia : listMedia) {
            System.out.println(articleMedia.getName());
        }
        assertThat(listMedia.size()).isGreaterThan(0);
    }
    
    
}
