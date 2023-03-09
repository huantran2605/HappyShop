package com.happyshop;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.ServletContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

@SpringBootTest
@AutoConfigureMockMvc
public class ReportOrderRestControllerTest {
    @Autowired
    MockMvc mockMvc;
    
    @Test
    @WithMockUser(username = "user1", password = "pass1", authorities = {"Admin"})
    public void testGetReportData7Days() throws Exception {
        
        String url = "/reports/sale_by_date/last_7_days";
        mockMvc.perform(get(url)).andExpect(status().isOk()).andDo(print());
    }
    
    @Test
    @WithMockUser(username = "user1", password = "pass1", authorities = {"Admin"})
    public void testGetReportData6Months() throws Exception {
        
        String url = "/reports/sale_by_date/last_6_months";
        mockMvc.perform(get(url)).andExpect(status().isOk()).andDo(print());
    }
    
    @Test
    @WithMockUser(username = "user1", password = "pass1", authorities = {"Admin"})
    public void testGetReportDataCustomTime() throws Exception {
        
        String url = "/reports/sale_by_date/2023-01-20/2023-02-02";
        mockMvc.perform(get(url)).andExpect(status().isOk()).andDo(print());
    }
    
    @Test
    @WithMockUser(username = "user1", password = "pass1", authorities = {"Admin"})
    public void testGetReportDataByCategory7Days() throws Exception {
        
        String url = "/reports/category/last_7_days";
        mockMvc.perform(get(url)).andExpect(status().isOk()).andDo(print());
    }
    
    @Test
    @WithMockUser(username = "user1", password = "pass1", authorities = {"Admin"})
    public void testGetReportDataByProduct7Days() throws Exception {
        
        String url = "/reports/product/last_7_days";
        mockMvc.perform(get(url)).andExpect(status().isOk()).andDo(print());
    }
    
    
}
