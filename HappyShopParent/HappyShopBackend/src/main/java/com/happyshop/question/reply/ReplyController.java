package com.happyshop.question.reply;

import java.io.IOException;  
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.happyshop.UserUtility;
import com.happyshop.common.entity.Question;
import com.happyshop.common.entity.Reply;
import com.happyshop.common.entity.Reply;
import com.happyshop.common.entity.Review;
import com.happyshop.common.entity.User;
import com.happyshop.common.entity.product.Product;
import com.happyshop.common.exception.QuestionNotFoundException;
import com.happyshop.common.exception.ReviewNotFoundException;
import com.happyshop.review.ReviewService;

@Controller
@RequestMapping("/reply")
public class ReplyController {
    
    @Autowired 
    ReplyService replyService;
    
    String defaultUrl = "redirect:/reply/page/1?sortField=replyTime&sortDir=des&keyWord=&replyStatus=";
    @GetMapping("/listReply")
    public String viewListReply() {
       
        return defaultUrl + "NAp";
    }
    
    @GetMapping("/page/{pageNum}")
    private String replyPage (@PathVariable ("pageNum") Integer pageNum,
            @Param("sortField") String sortField,
            @Param("sortDir") String sortDir,
            @Param("keyWord") String keyWord, 
            @Param("replyStatus") String replyStatus, 
            
            Model model) {
        //sort
        Sort sort = Sort.by(sortField);
        if(sortDir.equalsIgnoreCase("asc"))  
            sort = Sort.by(sortField).ascending();
        else  sort = Sort.by(sortField).descending();  
        
        Pageable pageable = PageRequest.of(pageNum - 1,  
                ReplyService.SIZE_PAGE_REPLY, sort);
        
        Page<Reply> pageReply = replyService.findAllNotApproved(keyWord, pageable); 
        if(replyStatus.equals("ARR")) {
            pageReply = replyService.findAllAdminReplyRequired(keyWord, pageable);
        }
        List<Reply> replies = pageReply.getContent();
        
        long startCount = (pageNum - 1) * ReplyService.SIZE_PAGE_REPLY + 1;
        long endCount = startCount + ReplyService.SIZE_PAGE_REPLY - 1 ;
        if(endCount > pageReply.getTotalElements() )
            endCount = pageReply.getTotalElements();
        
        String reserveDir = sortDir.equalsIgnoreCase("asc") ? "des"  : "asc";
        model.addAttribute("reserveDir", reserveDir);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("totalPage", pageReply.getTotalPages()); 
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
    
        
        model.addAttribute("replies", replies);
        model.addAttribute("totalElement", pageReply.getTotalElements());
        
        model.addAttribute("elementsCurrentPerPage", pageReply.getNumberOfElements());
        model.addAttribute("elementsPerPage", ReplyService.SIZE_PAGE_REPLY);
        
        model.addAttribute("keyWord", keyWord);
        model.addAttribute("moduleURL", "/reply");
        model.addAttribute("replyStatus", replyStatus);
               
        return"question/listQuestion";
    }
    
    @GetMapping("/approve")
    private String approveReply(@RequestParam(name="replyId", required = false) Integer replyId,
            @RequestParam(name="repliesSelectedId", required = false) Integer[] replyIds,
            @Param("replyStatus") String replyStatus,
            RedirectAttributes re) {
        if(replyId != null) {
            Reply q = replyService.findById(replyId).get();
            q.setApprovalStatus(true);
            replyService.save(q);
            re.addFlashAttribute("message", "Approve reply Id " + replyId + " successfully!");            
        }
        else if(replyIds != null) { 
            for (Integer id : replyIds) {
                Reply q = replyService.findById(id).get();
                q.setApprovalStatus(true);
                replyService.save(q);
            }
            re.addFlashAttribute("message", "Approve replies Id successfully!"); 
        }
                           
        return defaultUrl + replyStatus;
    }
}
