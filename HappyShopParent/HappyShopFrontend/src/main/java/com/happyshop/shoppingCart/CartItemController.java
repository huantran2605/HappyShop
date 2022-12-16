package com.happyshop.shoppingCart;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.happyshop.Utility;
import com.happyshop.address.AddressService;
import com.happyshop.common.entity.Address;
import com.happyshop.common.entity.CartItem;
import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.ShippingRate;
import com.happyshop.customer.CustomerService;
import com.happyshop.shipping.ShippingRateService;

@Controller
public class CartItemController {
    @Autowired
    CartItemService cartItemService;
    @Autowired
    CustomerService customerService;
    @Autowired
    ShippingRateService shippingService;
    @Autowired
    AddressService addressService;
    
    @GetMapping("/cart")
    public String showCart(HttpServletRequest request, Model model) {
        Customer customer = cartItemService.getAuthenticationCustomer(request);
        List<CartItem> list = cartItemService.findByCustomer(customer);
        float total = 0;
        for (CartItem cartItem : list) {
            total += cartItem.getSubTotal();           
        }
        Address address = addressService.findByDefaultAddress(customer.getId());
        ShippingRate sr = null;
        boolean usePrimaryAddressAsDefault = false;
        if(address != null) {
            sr = shippingService.findByAddress(address); 
        }else {
            sr = shippingService.findByCustomer(customer); 
            usePrimaryAddressAsDefault = true;
        }
                
        model.addAttribute("listCartItem", list);
        model.addAttribute("shippingSupported", sr != null);
        model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);      
        model.addAttribute("total", total);
        model.addAttribute("sizeOfList", list.size());
        return "cart/shopping_cart";
    }
}
