package com.happyshop.setting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.happyshop.common.entity.setting.Country;
import com.happyshop.common.entity.setting.State;
import com.happyshop.setting.country.CountryRepository;
import com.happyshop.setting.state.StateRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class StateRestControllerTest {
    @Autowired
    StateRepository stateRepo;
    
    @Autowired
    CountryRepository countryRepo;
    
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper oM;
    
//    @Test
//    @WithMockUser (username = "huantran2605@gmail.com", password = "something", roles = "ADMIN")
//    public void testGetAll() throws Exception {
//        String url = "/states/list_by_country/" + 4;
//        
//        MvcResult result = mockMvc.perform(get(url))
//                .andExpect(status().isOk())
//                .andReturn();
//        
//        String json = result.getResponse().getContentAsString();
//        System.out.println(json);
//        
//        State[] states = oM.readValue(json, State[].class);
//        
//        for (State state : states) {
//            System.out.println(state);
//        }
//    }

//    @Test
//    @WithMockUser(username = "huantran2605@gmail.com", password = "something", roles = "ADMIN")
//    public void testCreateState() throws JsonProcessingException, Exception {
//        String url = "/states/save";
//        Optional<Country> country =  countryRepo.findById(4);
//        State state = new State("test", country.get());
//        
//        MvcResult result = mockMvc.perform(post(url).contentType("application/json")
//                .content(oM.writeValueAsString(state))
//                .with(csrf()))
//            .andDo(print())
//            .andExpect(status().isOk())
//            .andReturn();
//        
//        String response = result.getResponse().getContentAsString();
//        int id = Integer.parseInt(response);
//        System.out.println(id+"-------" + id);
//        assertThat(id).isGreaterThan(0);
//    }
//    
//    @Test
//    @WithMockUser(username = "huantran2605@gmail.com", password = "something", roles = "ADMIN")
//    public void testUpdateState() throws JsonProcessingException, Exception {
//        String url = "/states/save";
//
//        State state = new State(6, "test2222", countryRepo.findById(4).get());
//        
//        MvcResult result =  mockMvc.perform(post(url).contentType("application/json")
//                .content(oM.writeValueAsString(state))
//                .with(csrf()))
//            .andDo(print())
//            .andExpect(status().isOk())
//            .andReturn();
//        
//        String respond =  result.getResponse().getContentAsString();
//        System.out.println(respond);
//   
//        
//        assertThat(respond).isEqualTo("6");
//    }
    
    @Test
    @WithMockUser(username = "nam@codejava.net", password = "something", roles = "ADMIN")
    public void testDeleteState() throws Exception {
        Integer stateId = 6;
        String url = "/states/delete/" + stateId;
        mockMvc.perform(get(url)).andExpect(status().isOk());
        
        Optional<State> findById = stateRepo.findById(stateId);
        
        assertThat(findById).isNotPresent();
    }
}
