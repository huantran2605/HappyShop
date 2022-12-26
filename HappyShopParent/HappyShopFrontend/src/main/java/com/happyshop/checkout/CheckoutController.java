package com.happyshop.checkout;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.happyshop.Utility;
import com.happyshop.address.AddressService;
import com.happyshop.checkout.paypal.PayPalApiException;
import com.happyshop.checkout.paypal.PaypalService;
import com.happyshop.common.entity.Address;
import com.happyshop.common.entity.CartItem;
import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.ShippingRate;
import com.happyshop.common.entity.order.Order;
import com.happyshop.common.entity.order.PaymentMethod;
import com.happyshop.common.entity.product.Product;
import com.happyshop.common.entity.setting.Setting;
import com.happyshop.common.entity.setting.SettingCategory;
import com.happyshop.order.OrderRepository;
import com.happyshop.order.OrderService;
import com.happyshop.product.ProductService;
import com.happyshop.setting.CurrencySettingBag;
import com.happyshop.setting.EmailSettingBag;
import com.happyshop.setting.PaymentSettingBag;
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
    @Autowired
    PaypalService paypalService;
      
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
        
        String currencyCode = settingService.getCurrencyCode();
        PaymentSettingBag paymentSettings = settingService.getPaymentSetting();  
        String paypalClientId = paymentSettings.getClientID();
        
        model.addAttribute("paypalClientId", paypalClientId);
        model.addAttribute("currencyCode", currencyCode);
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
            @Param("selectedProduct") String selectedProduct) throws UnsupportedEncodingException, MessagingException {
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
        
        Order createdOrder = orderService.createOrder(customer, address, selectedCartItems, paymentMethod, checkoutInfo);
        sendOrderConfirmationEmail(createdOrder);
        
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

    private void sendOrderConfirmationEmail(Order order) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettings =  settingService.getEmailSetting();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
        mailSender.setDefaultEncoding("utf-8");
        
        String toAddress = order.getCustomer().getEmail();
        String subject = emailSettings.getOrderConfirmationVerifySubject();
        String content = emailSettings.getOrderConfirmationVerifyContent();
        
        subject = subject.replace("[[orderId]]", String.valueOf(order.getId()));
        
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        
        helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);
        
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss E, dd MMM yyyy");
        String orderTime = dateFormat.format(order.getOrderTime());
        
        CurrencySettingBag currencySettingBag = settingService.getCurrencySetting();
        
        String totalAmount =  Utility.formatCurrency(order.getTotal(), currencySettingBag);
        
        content = content.replace("[[name]]", order.getCustomer().getFullName());
        content = content.replace("[[orderId]]", String.valueOf(order.getId()));
        content = content.replace("[[orderTime]]", orderTime);
        content = content.replace("[[shippingAddress]]", order.getShippingAddress());
        content = content.replace("[[total]]", totalAmount);
        content = content.replace("[[paymentMethod]]", order.getPaymentMethod().toString());
                
        helper.setText(content, true);
        mailSender.send(message);    
    }
    
    @PostMapping("/process_paypal_order")
    public String processPaypalOrder(HttpServletRequest request, Model model
            ,@RequestParam("selectedProduct") String selectedProduct)
            throws UnsupportedEncodingException, MessagingException {
        String orderId = request.getParameter("orderId");
        String message = null;
        String title = "Checkout Failure";
        try {
            if(paypalService.validateOrder(orderId)) {
                return placeOrder(request, selectedProduct);
            }
            else {
                title = "ERROR: Transaction could not be completed because order information is invalid.";
            }
            
        } catch (PayPalApiException e) {
            message = "ERROR: Transaction failed due to error: " + e.getMessage();
        }
        model.addAttribute("message", message);
        model.addAttribute("title", title);
        
        return "message";
    }
}
