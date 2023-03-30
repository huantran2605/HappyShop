package com.happyshop.question.reply;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.happyshop.common.entity.reply.Reply;

@RestController
public class ReplyRestController {
    
    @Autowired
    ReplyService replyService;
    
    @GetMapping("/reply/checkApproved/{id}")
    private boolean checkApproved(@PathVariable("id") Integer replyId) {
        Reply r = replyService.findById(replyId).get();
        if(r.isApprovalStatus()) {
            return true;
        }
        
        return false;
    }
}
