package com.happyshop.order;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderRestController {
    @Autowired
    OrderService orderService;
    
    @PostMapping("/order_shippers/update/{id}/{status}")
    public Response updateStatus(@PathVariable("id") Integer id, @PathVariable("status") String status ) {
        orderService.updateStatus(id, status);
        return new Response(id, status);
    }
    
}

class Response {
    private Integer orderId;
    private String status;
    public Response(Integer id, String status) {
        this.orderId = id;
        this.status = status;
    }
    public Integer getId() {
        return orderId;
    }
    public void setId(Integer id) {
        this.orderId = id;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
      
}