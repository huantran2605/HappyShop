//package com.happyshop.question.reply;
//
//import java.io.IOException;  
//import java.util.Date;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import com.happyshop.UserUtility;
//import com.happyshop.common.entity.User;
//import com.happyshop.common.entity.product.Product;
//import com.happyshop.common.entity.question.Question;
//import com.happyshop.common.entity.reply.Reply;
//import com.happyshop.common.entity.review.Review;
//import com.happyshop.common.exception.QuestionNotFoundException;
//import com.happyshop.common.exception.ReviewNotFoundException;
//import com.happyshop.question.QuestionService;
//import com.happyshop.review.ReviewService;
//
//@Controller
//@RequestMapping("/reply")
//public class ReplyController {
//    
//    @Autowired 
//    ReplyService replyService;
//    @Autowired
//    QuestionService questionService;
//    @Autowired
//    UserUtility userUtility;
//    
//    String defaultUrl = "redirect:/reply/page/1?sortField=replyTime&sortDir=des&keyWord=&replyStatus=";
//    @GetMapping("/listReply")
//    public String viewListReply() {
//       
//        return defaultUrl + "NAp";
//    }
//    
//    @GetMapping("/page/{pageNum}")
//    private String replyPage (@PathVariable ("pageNum") Integer pageNum,
//            @Param("sortField") String sortField,
//            @Param("sortDir") String sortDir,
//            @Param("keyWord") String keyWord, 
//            @Param("replyStatus") String replyStatus, 
//            
//            Model model) {
//        //sort
//        Sort sort = Sort.by(sortField);
//        if(sortDir.equalsIgnoreCase("asc"))  
//            sort = Sort.by(sortField).ascending();
//        else  sort = Sort.by(sortField).descending();  
//        
//        Pageable pageable = PageRequest.of(pageNum - 1,  
//                ReplyService.SIZE_PAGE_REPLY, sort);
//        
//        Page<Reply> pageReply = replyService.findAllNotApproved(keyWord, pageable); 
//        if(replyStatus.equals("ARR")) {
//            pageReply = replyService.findAllAdminReplyRequired(keyWord, pageable);
//        }
//        List<Reply> replies = pageReply.getContent();
//        
//        long startCount = (pageNum - 1) * ReplyService.SIZE_PAGE_REPLY + 1;
//        long endCount = startCount + ReplyService.SIZE_PAGE_REPLY - 1 ;
//        if(endCount > pageReply.getTotalElements() )
//            endCount = pageReply.getTotalElements();
//        
//        String reserveDir = sortDir.equalsIgnoreCase("asc") ? "des"  : "asc";
//        model.addAttribute("reserveDir", reserveDir);
//        model.addAttribute("sortField", sortField);
//        model.addAttribute("sortDir", sortDir);
//        model.addAttribute("pageNum", pageNum);
//        model.addAttribute("totalPage", pageReply.getTotalPages()); 
//        model.addAttribute("currentPage", pageNum);
//        model.addAttribute("startCount", startCount);
//        model.addAttribute("endCount", endCount);
//    
//        
//        model.addAttribute("replies", replies);
//        model.addAttribute("totalElement", pageReply.getTotalElements());
//        
//        model.addAttribute("elementsCurrentPerPage", pageReply.getNumberOfElements());
//        model.addAttribute("elementsPerPage", ReplyService.SIZE_PAGE_REPLY);
//        
//        model.addAttribute("keyWord", keyWord);
//        model.addAttribute("moduleURL", "/reply");
//        model.addAttribute("replyStatus", replyStatus);
//        
//        model.addAttribute("sizeListReplyNotApproved",
//                replyService.findAllNotApproved("", pageable).getContent().size());
//        model.addAttribute("sizeListReplyAdminReplyRequired",
//                replyService.findAllAdminReplyRequired("", pageable).getContent().size());
//               
//        return"question/listQuestion";
//    }
//    
//    @GetMapping("/approve")
//    private String approveReply(@RequestParam(name="replyId", required = false) Integer replyId,
//            @RequestParam(name="repliesSelectedId", required = false) Integer[] replyIds,
//            @Param("replyStatus") String replyStatus,
//            RedirectAttributes re) {
//        if(replyId != null) {
//            Reply q = replyService.findById(replyId).get();
//            q.setApprovalStatus(true);
//            replyService.save(q);
//            re.addFlashAttribute("message", "Approve reply Id " + replyId + " successfully!");            
//        }
//        else if(replyIds != null) { 
//            for (Integer id : replyIds) {
//                Reply q = replyService.findById(id).get();
//                q.setApprovalStatus(true);
//                replyService.save(q);
//            }
//            re.addFlashAttribute("message", "Approve replies Id successfully!"); 
//        }
//                           
//        return defaultUrl + replyStatus;
//    }
//    
//    @PostMapping("/save-answer")
//    private String saveAnswer(@RequestParam("reply_content") String reply_content,
//            @RequestParam("questionId") Integer questionId,
//            @RequestParam(name = "replyId", required = false) Integer replyId,
//            @RequestParam("object") String object,
//            RedirectAttributes re,
//            HttpServletRequest request) throws QuestionNotFoundException {
//        
//        Question q = questionService.findById(questionId);
//        q.setAnswerStatus(true);
//        if(replyId != null) {
//            Reply  r = replyService.findById(replyId).get();
//            r.setAdminReplyRequired(false);
//        }
//        User admin = userUtility.getAuthenticationUser(request);
//        Reply  r = new Reply();
//        r.setQuestion(q);
//        r.setAdmin(admin);
//        r.setAdminReplyRequired(false);
//        r.setApprovalStatus(true);
//        r.setReply_content(reply_content);
//        r.setReplyTime(new Date());
//        
//        replyService.save(r);
//        String redirectUrl  = "";
//        if(object.equals("question")) {
//            re.addFlashAttribute("message", "Answer question id " + questionId +" successfully!");  
//            redirectUrl = "redirect:/question/page/1?sortField=askTime&sortDir=des&keyWord=&questionStatus=NAn";
//        }
//        else {
//            re.addFlashAttribute("message", "Answer reply id " + replyId +" successfully!");                        
//            redirectUrl = "redirect:/reply/page/1?sortField=replyTime&sortDir=des&keyWord=&replyStatus=ARR";
//        }
//        return redirectUrl;
//    }
//    
//    @GetMapping("answer/{id}")
//    private String showFormAnwerReply(@PathVariable("id") Integer replyId, Model model){
//        Reply r = replyService.findById(replyId).get();
//        
//        model.addAttribute("question", r.getQuestion());
//        model.addAttribute("replyId", replyId);
//        model.addAttribute("reply", r);
//        
//        model.addAttribute("replies", r.getQuestion().getReplies());
//        
//        return "question/reply_answer_form";
//    }
//    
//    @GetMapping("delete/{id}")
//    private String deleteReply(@PathVariable("id") Integer id,
//            @Param("replyStatus") String replyStatus,
//            RedirectAttributes re,Model model) throws IOException {
//       
//        if(replyService.findById(id).isEmpty()) {
//            re.addFlashAttribute("message","The reply is not existed!");
//        }
//        else {
//            replyService.deleteById(id);
//            re.addFlashAttribute("message", "Delete the reply id: " + id + " successfully!");                        
//        }
//               
//        return defaultUrl + replyStatus;   
//    }
//}
