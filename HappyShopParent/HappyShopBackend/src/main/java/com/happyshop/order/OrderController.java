package com.happyshop.order;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.happyshop.common.entity.Customer;
import com.happyshop.common.entity.order.Order;
import com.happyshop.common.entity.order.OrderDetail;
import com.happyshop.common.entity.order.OrderStatus;
import com.happyshop.common.entity.order.OrderTrack;
import com.happyshop.common.entity.product.Product;
import com.happyshop.common.entity.setting.Country;
import com.happyshop.common.entity.setting.Currency;
import com.happyshop.common.entity.setting.Setting;
import com.happyshop.common.entity.setting.SettingCategory;
import com.happyshop.order.OrderService;
import com.happyshop.product.ProductService;
import com.happyshop.setting.CurrencyService;
import com.happyshop.setting.SettingService;
import com.happyshop.setting.country.CountryService;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    SettingService settingService;
    @Autowired
    CountryService countryService;
    @Autowired
    ProductService productService;
    
    private String defaultRedirectURL = "redirect:/order/page/1?sortField=orderTime&sortDir=des&keyWord=";
    
    @GetMapping("/listOrder")
    private String viewFirstPageProduct(Model model,
            RedirectAttributes re, HttpServletRequest request) {
        return defaultRedirectURL;
    }
    
    @GetMapping("/page/{pageNum}") 
    private String orderPage (@PathVariable ("pageNum") Integer pageNum,
            @Param("sortField") String sortField,
            @Param("sortDir") String sortDir,
            @Param("keyWord") String keyWord,          
            Model model, HttpServletRequest request) {
        //sort
        Sort sort = null;
        if(sortField.equals("destination")) {
            sort = Sort.by("country").and(Sort.by("state").and(Sort.by("city")));
        }
        else {
            sort = Sort.by(sortField);           
        }
        if(sortDir.equalsIgnoreCase("asc"))
            sort = sort.ascending();  
        else  sort = sort.descending();
        
        Pageable pageable = PageRequest.of(pageNum - 1,
                OrderService.SIZE_PAGE_ORDER, sort);
        
        Page<Order> pageOrder = orderService.findAll(keyWord, pageable); 
        List<Order> listOrder = pageOrder.getContent();
        //list order     
        long startCount = (pageNum - 1) * OrderService.SIZE_PAGE_ORDER + 1;
        long endCount = startCount + OrderService.SIZE_PAGE_ORDER - 1;
        if(endCount > pageOrder.getTotalElements() )
            endCount = pageOrder.getTotalElements();
        
        String reserveDir = sortDir.equalsIgnoreCase("asc") ? "des"  : "asc";  
        model.addAttribute("reserveDir", reserveDir);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("totalPage", pageOrder.getTotalPages()); 
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        
        if(endCount >  pageOrder.getTotalElements()) {
            endCount = pageOrder.getTotalElements();
        }
        
        model.addAttribute("totalElement", pageOrder.getTotalElements());
        
        model.addAttribute("orders", listOrder);
        
        model.addAttribute("elementsCurrentPerPage", pageOrder.getNumberOfElements());
        model.addAttribute("elementsPerPage", OrderService.SIZE_PAGE_ORDER);
        model.addAttribute("keyWord", keyWord);
        model.addAttribute("moduleURL", "/order");        
        loadCurrencySetting(request);  
        return"order/listOrder";
    }
    
    public void loadCurrencySetting( HttpServletRequest  request) {
        List<Setting> list = settingService.findByCategory(SettingCategory.CURRENCY);
        
        for (Setting setting : list) {
            request.setAttribute(setting.getKey(), setting.getValue());
        }     
    }
    
    @GetMapping("detail/{id}")
    private String detailOrder(@PathVariable("id") Integer  id,            
            Model model, RedirectAttributes re, HttpServletRequest request) {
        Optional<Order> order = orderService.findById(id);
        if (order.isEmpty()) {
            re.addFlashAttribute("message", "The order is not existed!");
            return defaultRedirectURL;
        } else {
            model.addAttribute("order", order.get()); 
            Set<OrderDetail> orderDetails = order.get().getOrderDetails();
            model.addAttribute("orderDetails", orderDetails); 
            loadCurrencySetting(request);
        }
        return "order/order_detail_modal";
    }
    
    @GetMapping("/delete/{id}")
    private String deleteOrder(@PathVariable("id") Integer id,
            RedirectAttributes re,Model model) throws IOException {
        Optional<Order> order =  orderService.findById(id);
        
        if (order.isEmpty()) {
            re.addFlashAttribute("message", "The order is not existed!");
            return defaultRedirectURL;
        }
        else {    
            orderService.deleteById(id); 
            re.addFlashAttribute("message","Delete an order id: "+ id + " successfully!");           
            return defaultRedirectURL;
        }
    }
    
    @GetMapping("/update/{orderId}")
    public String updateOrder(@PathVariable("orderId") Integer orderId, Model model) {
        List<Country> listCountry = countryService.findAllByOrderByNameAsc();
        Order order = orderService.findById(orderId).get();
        model.addAttribute("order", order);
        model.addAttribute("listCountry", listCountry);
        
        
        return "order/order_form";
    }
    
    @PostMapping("/save")
    public String saveOrder(Order order,RedirectAttributes re, HttpServletRequest request) {
        String country = request.getParameter("countryName");
        order.setCountry(country);
        
        updateOrderDetails(order, request);
        updateOrderTrack(order, request); 
        
        Order orderInDb = orderService.findById(order.getId()).get();
        order.setOrderTime(orderInDb.getOrderTime());
        order.setDeliverDate(orderInDb.getDeliverDate());
        
        orderService.save(order);
        re.addFlashAttribute("message","Update an order successfully!");      
        return defaultRedirectURL;
    }

    private void updateOrderTrack(Order order, HttpServletRequest request) {
        String[] trackIds = request.getParameterValues("trackId");
        String[] trackDates = request.getParameterValues("trackDate");
        String[] trackStatuses = request.getParameterValues("trackStatus");
        String[] trackNotes = request.getParameterValues("trackNote");
        
        List<OrderTrack> listOrderTrack = order.getOrderTracks();
        
        for (int i = 0; i < trackIds.length; i++) {
            OrderTrack ot = new OrderTrack();
            int trackId = Integer.parseInt(trackIds[i]);
            if (trackId > 0) {
                ot.setId(trackId);
            }
            ot.setStatus(OrderStatus.valueOf(trackStatuses[i]));
            ot.setNote(trackNotes[i]);
            ot.setOrder(order);
            ot.setUpdatedTimeOnForm(trackDates[i]);
            
            listOrderTrack.add(ot);
        }

    }

    private void updateOrderDetails(Order order, HttpServletRequest request) {
        String[] detailIds = request.getParameterValues("detailId");
        String[] productIds = request.getParameterValues("productId");
        String[] productDetailCosts = request.getParameterValues("productDetailCost");
        String[] productQuantities = request.getParameterValues("productQuantity");
        String[] productPrices = request.getParameterValues("productPrice");
        String[] productSubTotals = request.getParameterValues("productSubTotal");
        String[] productShipCosts = request.getParameterValues("productShipCost");
        
        Set<OrderDetail> setOrderDetails = order.getOrderDetails();
        for (int i = 0; i < detailIds.length; i++) {
            OrderDetail od = new OrderDetail();
            int odId = Integer.parseInt(detailIds[i]);
            if(odId > 0) {
                od.setId(odId);
            }
            od.setQuantity(Integer.parseInt(productQuantities[i]));
            od.setProductCost(Float.parseFloat(productDetailCosts[i]));
            od.setShippingCost(Float.parseFloat(productShipCosts[i]));
            od.setUnitPrice(Float.parseFloat(productPrices[i]));
            od.setSubtotal(Float.parseFloat(productSubTotals[i]));  
            od.setProduct(productService.findById(Integer.parseInt(productIds[i])).get());
            od.setOrder(order);
            
            setOrderDetails.add(od);
        }
    }
     
}
