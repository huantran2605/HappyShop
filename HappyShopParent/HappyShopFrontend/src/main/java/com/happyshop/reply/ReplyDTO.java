package com.happyshop.reply;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {
    private Integer id;
    private String customerName;
    private String content;
    private String adminName;
    private Date replyTime;
    private String visitorName;
      
}
