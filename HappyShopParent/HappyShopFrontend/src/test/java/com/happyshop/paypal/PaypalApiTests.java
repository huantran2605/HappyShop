package com.happyshop.paypal;


import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.happyshop.checkout.paypal.PaypalOrderResponse;

public class PaypalApiTests {
    
    
    private static final String BASE_URL = "https://api.sandbox.paypal.com";
    private static final String GET_ORDER_API = "/v2/checkout/orders/";
    private static final String CLIENT_ID = "AV9y1AVXDbLCSFOQ5bg1oagCs3uoMysa992SSeupDOUJM8yVDM116XgugasEi1T_QbeEfDQKyXqEXZ6R";
    private static final String CLIENT_SECRET = "EBOS7PjgiFkIpMbKGWbp-iUSeWSrYmJCrTMCeNXTtv89AXxfjVZEDvSkvfVZEf_nIlZ4eynUSLn9dxul";
    
    @Test
    public void testGetOrderDetails() {  
        String orderId = "54Y15034R5101323D";
        String requestURL = BASE_URL + GET_ORDER_API + orderId;
        
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Accept-Language", "en_US");
        headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);
        
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        
        ResponseEntity<PaypalOrderResponse> response = restTemplate.exchange(
                requestURL, HttpMethod.GET, request, PaypalOrderResponse.class);
        PaypalOrderResponse orderResponse = response.getBody();
//        System.out.println(response);
        System.out.println("Order ID: " + orderResponse.getId());
        System.out.println("Validated: " + orderResponse.validate(orderId));
        
    }
}
