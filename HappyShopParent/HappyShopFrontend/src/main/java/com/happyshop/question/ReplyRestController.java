package com.happyshop.question;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.happyshop.common.entity.Question;
import com.happyshop.common.entity.Reply;
import com.happyshop.common.exception.QuestionNotFoundException;
    
@RestController
public class ReplyRestController {
    @Autowired
    QuestionService questionService;

    
    @GetMapping("/reply/all_replies/{questionId}")
    public List<ReplyDTO> getAllRepliesOfQuestion(@PathVariable("questionId") Integer questionId ) {
        Question question = questionService.findById(questionId).get();       
        System.out.println(question.getQuestion_content());
        
        List<Reply> replies = question.getReplies();
        List<ReplyDTO> repliesDTO = new ArrayList<>();
        
        for (Reply r : replies) {
            String customerName = null;
            String userName = null;
            if(r.getCustomer() != null) {
                customerName = r.getCustomer().getFullName();
            }
            else {
                userName = r.getUser().getFullName();
            }
            ReplyDTO rD = new ReplyDTO(customerName, r.getReply_content(), 
                    userName, r.getReplyTime());
            repliesDTO.add(rD);
        }
        
        return repliesDTO;
    }
}
