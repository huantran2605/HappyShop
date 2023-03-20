package com.happyshop.question;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDTO {
    private String customerName;
    private String reply_content;
    private String userName;
    private Date replyTime;
      
}
