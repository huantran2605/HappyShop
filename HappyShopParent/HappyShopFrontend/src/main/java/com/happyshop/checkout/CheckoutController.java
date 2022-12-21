package com.happyshop.checkout;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.happyshop.address.AddressService;
import com.happyshop.common.entity.Address;
import com.happyshop.common.entity.CartItem;
import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.ShippingRate;
import com.happyshop.common.entity.order.PaymentMethod;
import com.happyshop.common.entity.product.Product;
import com.happyshop.common.entity.setting.Setting;
import com.happyshop.common.entity.setting.SettingCategory;
import com.happyshop.order.OrderRepository;
import com.happyshop.order.OrderService;
import com.happyshop.product.ProductService;
import com.happyshop.setting.SettingService;
import com.happyshop.shipping.ShippingRateService;
import com.happyshop.shoppingCart.CartItemService;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
    @Autowired
    CheckoutService checkoutService;
    @Autowired
    CartItemService cartItemService;
    @Autowired
    AddressService addressService;
    @Autowired
    ShippingRateService shippingService;
    @Autowired
    SettingService settingService;
    @Autowired
    OrderService orderService;
    @Autowired
    ProductService productService;
    
    @RequestMapping("")
    public String viewCheckoutPage(@Param("selectedProduct") String selectedProduct,
            HttpServletRequest request, Model model) {
        Customer customer = cartItemService.getAuthenticationCustomer(request);
      
        String[] selectedProductId = selectedProduct.split("-");
        List<CartItem> selectedCartItems = new ArrayList<>();
        for (String id : selectedProductId) {
            CartItem cartItem = cartItemService.findByCustomerAndProduct(customer,Integer.parseInt(id));
            selectedCartItems.add(cartItem);
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
        if(sr == null) {
            return "redirect:/cart";
        }
        
        CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(selectedCartItems, sr);        
        loadCurrencySetting(request);
              
        model.addAttribute("checkoutInfo", checkoutInfo);
        model.addAttribute("defaultAddress", address);
        model.addAttribute("customer", customer);
        model.addAttribute("cartItems", selectedCartItems);
        model.addAttribute("selectedProduct", selectedProduct);
        model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);
        return "checkout/checkout";
    }
    
    public void loadCurrencySetting( HttpServletRequest  request) {
        List<Setting> list = settingService.findByCategory(SettingCategory.CURRENCY);
        
        for (Setting setting : list) {
            request.setAttribute(setting.getKey(), setting.getValue());
        }     
    }
    
    @PostMapping("/place_order")
    public String placeOrder(HttpServletRequest request,
            @Param("selectedProduct") String selectedProduct) {
        Customer customer = cartItemService.getAuthenticationCustomer(request);
        
        List<CartItem> selectedCartItems = new ArrayList<>();
        String[] selectedProductId = selectedProduct.split("-");
        for (String id : selectedProductId) {
            int productId = Integer.parseInt(id);
            CartItem cartItem = cartItemService.findByCustomerAndProduct(customer,productId);
            selectedCartItems.add(cartItem);         
        }
        
        Address address = addressService.findByDefaultAddress(customer.getId());
        
        ShippingRate sr = null;
        if(address != null) {
            sr = shippingService.findByAddress(address); 
        }else {
            sr = shippingService.findByCustomer(customer); 
        }
        if(sr == null) {
            return "redirect:/cart";
        }
        CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(selectedCartItems, sr); 
        
        String paymentType = request.getParameter("paymentMethod");
        PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentType);
        
        orderService.createOrder(customer, address, selectedCartItems, paymentMethod, checkoutInfo);
        
        //after place order
        for (String id : selectedProductId) {
            int productId = Integer.parseInt(id);
            Product product = productService.findById(productId).get();
            CartItem cartItem = cartItemService.findByCustomerAndProduct(customer,productId);
            //update quantity of Product when order was placed    
            productService.updateQuantity(product.getQuantity() - cartItem.getQuantity(), productId);  
            
            //delete selectedcartItem in shopping cart
            cartItemService.deleteByCustomerAndProduct(customer, productId); 
        }
        
        return "checkout/order_completed";
    }
}
