package com.happyshop.reply;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.happyshop.CustomerUtility;
import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.question.Question;
import com.happyshop.common.entity.reply.Reply;
import com.happyshop.common.entity.reply.ReplyVisitor;
import com.happyshop.common.exception.QuestionNotFoundException;
import com.happyshop.question.QuestionService;
    
@RestController
public class ReplyRestController {
    @Autowired
    QuestionService questionService;
    @Autowired
    CustomerUtility customerUtility;
    @Autowired
    ReplyVisitorService replyVisitorService;
    @Autowired
    ReplyService replyService;
    
    @GetMapping("/reply/all_replies/{questionId}")
    public List<ReplyDTO> getAllRepliesOfQuestion(@PathVariable("questionId") Integer questionId ) {
        
        List<Reply> replies = replyService.findByQuestionAndApprovalStatus(questionId);
        List<ReplyDTO> repliesDTO = new ArrayList<>();
        
        for (Reply r : replies) {
            String customerName = null;
            String adminName = null;
            String replyVisitorName = null;
            if(r.getCustomer() != null) {
                customerName = r.getCustomer().getFullName();
            }
            else if(r.getAdmin() != null) {
                adminName = r.getAdmin().getFullName();
            }
            else {
                replyVisitorName = r.getVisitor().getFullName();
            }
            ReplyDTO rD = new ReplyDTO(r.getId(), customerName, r.getContent(), 
                    adminName, r.getReplyTime(), replyVisitorName);
            repliesDTO.add(rD);
        }
        
        return repliesDTO;
    }
    
    @PostMapping("/reply/save")
    public String saveReply(
            @RequestParam("questionId") Integer questionId,
            @RequestParam("content") String content,
            @RequestParam("adminReplyRequired") Boolean adminReplyRequired,          
            
            @RequestParam(name = "fullName", required = false) String fullName,
            @RequestParam(name = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(name = "email", required = false) String email,
            HttpServletRequest request
            ) {
        Customer customer = customerUtility.getAuthenticationCustomer(request); 
        Question question = questionService.findById(questionId).get();
        Reply r = new Reply();
        r.setAdminReplyRequired(adminReplyRequired);
        r.setApprovalStatus(false);
        r.setCustomer(customer);
        r.setQuestion(question);
        r.setContent(content);
        r.setReplyTime(new Date());
        if(customer == null) {
            ReplyVisitor rV = new ReplyVisitor(fullName, phoneNumber, email);
            replyVisitorService.save(rV);
            r.setVisitor(rV);
        }
        
        replyService.save(r);
        return "save reply successfully!";
        
    }
}
