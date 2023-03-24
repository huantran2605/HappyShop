package com.happyshop.question;

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
import com.happyshop.common.entity.Review;
import com.happyshop.common.entity.User;
import com.happyshop.common.entity.product.Product;
import com.happyshop.common.exception.QuestionNotFoundException;
import com.happyshop.common.exception.ReviewNotFoundException;
import com.happyshop.review.ReviewService;

@Controller
@RequestMapping("/question")
public class QuestionController {
    @Autowired
    QuestionService questionService;
    @Autowired
    UserUtility userUtility;
    
    String defaultUrl = "redirect:/question/page/1?sortField=askTime&sortDir=des&keyWord=&questionStatus=";
    @GetMapping("/listQuestion")
    public String viewListQuestion() {
       
        return defaultUrl + "NAp";
    }
    
    @GetMapping("/page/{pageNum}")
    private String questionPage (@PathVariable ("pageNum") Integer pageNum,
            @Param("sortField") String sortField,
            @Param("sortDir") String sortDir,
            @Param("keyWord") String keyWord, 
            @Param("questionStatus") String questionStatus, 
            Model model) {
        //sort
        Sort sort = Sort.by(sortField);
        if(sortDir.equalsIgnoreCase("asc"))  
            sort = Sort.by(sortField).ascending();
        else  sort = Sort.by(sortField).descending();  
        
        Pageable pageable = PageRequest.of(pageNum - 1,  
                QuestionService.SIZE_PAGE_QUESTION, sort);
        
        Page<Question> pageQuestion = questionService.findAllNotApproved(keyWord, pageable);             
        
        if(questionStatus.equals("NAn")) {
            pageQuestion = questionService.findAllNotAnswered(keyWord, pageable);                       
        }else if(questionStatus.equals("NApANAn")) {
            pageQuestion = questionService.findAllNotApprovedAndNotAnswered(keyWord, pageable);            
        }
        
        List<Question> questions = pageQuestion.getContent();
        
        long startCount = (pageNum - 1) * QuestionService.SIZE_PAGE_QUESTION + 1;
        long endCount = startCount + QuestionService.SIZE_PAGE_QUESTION - 1 ;
        if(endCount > pageQuestion.getTotalElements() )
            endCount = pageQuestion.getTotalElements();
        
        String reserveDir = sortDir.equalsIgnoreCase("asc") ? "des"  : "asc";
        model.addAttribute("reserveDir", reserveDir);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("totalPage", pageQuestion.getTotalPages()); 
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
    
        
        model.addAttribute("questions", questions);
        model.addAttribute("totalElement", pageQuestion.getTotalElements());
        
        model.addAttribute("elementsCurrentPerPage", pageQuestion.getNumberOfElements());
        model.addAttribute("elementsPerPage", QuestionService.SIZE_PAGE_QUESTION);
        
        model.addAttribute("keyWord", keyWord);
        model.addAttribute("moduleURL", "/question");
        model.addAttribute("questionStatus", questionStatus);
        
        
        
        return"question/listQuestion";
    }
    
    @GetMapping("delete/{id}")
    private String deleteQuestion(@PathVariable("id") Integer id,
            @Param("questionStatus") String questionStatus,
            RedirectAttributes re,Model model) throws IOException {
        try {
            questionService.deleteById(id);
            re.addFlashAttribute("message", "Delete the question id: " + id + " successfully!");            
        } catch (QuestionNotFoundException e) {
            re.addFlashAttribute("message", e.getMessage());
        }
        
        
        return defaultUrl + questionStatus;   
    }
    
    @GetMapping("/approve")
    private String approveQuestion(@RequestParam(name="questionId", required = false) Integer questionId,
            @RequestParam(name="questionsSelectedId", required = false) Integer[] questionIds,
            RedirectAttributes re) throws QuestionNotFoundException {
        if(questionId != null) {
            Question q = questionService.findById(questionId);
            q.setApprovalStatus(true);
            questionService.save(q);
            re.addFlashAttribute("message", "Approve question Id " + questionId + " successfully!");            
        }
        else if(questionIds != null) { 
            for (Integer id : questionIds) {
                Question q = questionService.findById(id);
                q.setApprovalStatus(true);
                questionService.save(q);
            }
            re.addFlashAttribute("message", "Approve questions Id successfully!"); 
        }
            
                
        return defaultUrl + "NAp";
    }
   
    
    
        
}
