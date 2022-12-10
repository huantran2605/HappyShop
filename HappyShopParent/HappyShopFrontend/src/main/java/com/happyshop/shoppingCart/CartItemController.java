package com.happyshop.shoppingCart;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.happyshop.Utility;
import com.happyshop.common.entity.CartItem;
import com.happyshop.common.entity.Customer;
import com.happyshop.customer.CustomerService;

@Controller
public class CartItemController {
    @Autowired
    CartItemService cartItemService;
    @Autowired
    CustomerService customerService;
    
    @GetMapping("/cart")
    public String showCart(HttpServletRequest request, Model model) {
        String email = Utility.getEmailAuthenticationCustomer(request);
        Customer customer = customerService.findByEmail(email);
        List<CartItem> list = cartItemService.findByCustomer(customer);
        float estimatedTotal = 0;
        for (CartItem cartItem : list) {
            estimatedTotal += cartItem.getSubTotal();           
        }
        model.addAttribute("listCartItem", list);
        model.addAttribute("estimatedTotal", Math.round(estimatedTotal));
        return "cart/shopping_cart";
    }
}
