package com.happyshop.customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.happyshop.common.entity.AuthenticationType;
import com.happyshop.common.entity.Category;
import com.happyshop.common.entity.Country;
import com.happyshop.common.entity.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CustomerRepoTest {
    @Autowired
    CustomerRepository repo;
    
    @Autowired
    EntityManager entityManager;
//    @Test
//    public void addCustomerTest() {
//        Integer countryId = 234; // USA
//        Country country = entityManager.find(Country.class, countryId);
//        
//        Customer customer = new Customer();
//        customer.setCountry(country);
//        customer.setFirstName("David");
//        customer.setLastName("Fountaine");
//        customer.setPassword("password123");
//        customer.setEmail("david.s.fountaine@gmail.com");
//        customer.setPhoneNumber("312-462-7518");
//        customer.setAddressLine1("1927  West Drive");
//        customer.setCity("Sacramento");
//        customer.setState("California");
//        customer.setPostalCode("95867");
//        customer.setCreatedTime(new Date());
//        
//        Customer savedCustomer = repo.save(customer);
//        
//        assertThat(savedCustomer).isNotNull();
//        assertThat(savedCustomer.getId()).isGreaterThan(0);
//    }
//    
//    @Test
//    public void testCreateCustomer2() {
//        Integer countryId = 106; // India
//        Country country = entityManager.find(Country.class, countryId);
//        
//        Customer customer = new Customer();
//        customer.setCountry(country);
//        customer.setFirstName("Sanya");
//        customer.setLastName("Lad");
//        customer.setPassword("password456");
//        customer.setEmail("sanya.lad2020@gmail.com");
//        customer.setPhoneNumber("02224928052");
//        customer.setAddressLine1("173 , A-, Shah & Nahar Indl.estate, Sunmill Road");
//        customer.setAddressLine2("Dhanraj Mill Compound, Lower Parel (west)");
//        customer.setCity("Mumbai");
//        customer.setState("Maharashtra");
//        customer.setPostalCode("400013");
//        customer.setCreatedTime(new Date());
//        
//        Customer savedCustomer = repo.save(customer);
//        
//        assertThat(savedCustomer).isNotNull();
//        assertThat(savedCustomer.getId()).isGreaterThan(0);
//    }
//    
//    @Test
//    public void testListCustomers() {
//        Iterable<Customer> customers = repo.findAll();
//        customers.forEach(System.out::println);
//        
//        assertThat(customers).hasSizeGreaterThan(1);
//    }
    
    @Test
    public void updateAuthenType() {
       repo.updateAuthenticationType(1, AuthenticationType.FACEBOOK);
    }
}
