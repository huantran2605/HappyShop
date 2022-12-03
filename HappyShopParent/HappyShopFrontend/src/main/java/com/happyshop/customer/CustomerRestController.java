package com.happyshop.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {
    @Autowired
    CustomerService customerService;
    
    @PostMapping("/customers/check_unique_email")
    public String checkUniqueEmail(@Param("email") String email) {
        boolean result =  customerService.checkUniqueEmail(email);
        if(result) {
            return "Ok";
        }
        return "Duplicated";
    }
}
