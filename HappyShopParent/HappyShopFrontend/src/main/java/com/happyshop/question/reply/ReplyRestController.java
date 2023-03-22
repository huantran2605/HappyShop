package com.happyshop.question.reply;

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
import com.happyshop.common.entity.Question;
import com.happyshop.common.entity.Reply;
import com.happyshop.common.entity.Reply_Person;
import com.happyshop.common.exception.QuestionNotFoundException;
import com.happyshop.question.QuestionService;
    
@RestController
public class ReplyRestController {
    @Autowired
    QuestionService questionService;
    @Autowired
    CustomerUtility customerUtility;
    @Autowired
    Reply_PersonService reply_PersonService;
    @Autowired
    ReplyService replyService;
    
    @GetMapping("/reply/all_replies/{questionId}")
    public List<ReplyDTO> getAllRepliesOfQuestion(@PathVariable("questionId") Integer questionId ) {
        Question question = questionService.findById(questionId).get();       
        
        List<Reply> replies = replyService.findByQuestionAndApprovalStatus(questionId);
        List<ReplyDTO> repliesDTO = new ArrayList<>();
        
        for (Reply r : replies) {
            String customerName = null;
            String adminName = null;
            if(r.getCustomer() != null) {
                customerName = r.getCustomer().getFullName();
            }
            else {
                adminName = r.getAdmin().getFullName();
            }
            ReplyDTO rD = new ReplyDTO(r.getId(), customerName, r.getReply_content(), 
                    adminName, r.getReplyTime());
            repliesDTO.add(rD);
        }
        
        return repliesDTO;
    }
    
    @PostMapping("/reply/save")
    public String saveReply(
            @RequestParam("questionId") Integer questionId,
            @RequestParam("reply_content") String reply_content,
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
        r.setReply_content(reply_content);
        r.setReplyTime(new Date());
        if(customer == null) {
            Reply_Person rP = new Reply_Person(fullName, phoneNumber, email);
            reply_PersonService.save(rP);
            r.setPersonReply(rP);
        }
        
        replyService.save(r);
        return "save reply successfully!";
        
    }
}
