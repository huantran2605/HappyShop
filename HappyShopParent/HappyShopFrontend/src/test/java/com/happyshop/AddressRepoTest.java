package com.happyshop;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.happyshop.address.AddressRepository;
import com.happyshop.common.entity.Address;
import com.happyshop.common.entity.Country;
import com.happyshop.common.entity.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class AddressRepoTest {
    @Autowired
    AddressRepository repo;
    
    @Autowired
    EntityManager em;
    
    @Test
    public void addTest() {
        Address a = new Address();
        a.setFirstName("huyen");
        a.setLastName("Tran");
        a.setPhoneNumber("41234");
        a.setAddressLine1("tp nam dinh");
        a.setCity("nam  dinh");
        a.setState("nam dinh");
        a.setPostalCode("34561");
        
        Country country = em.find(Country.class, 5);
        a.setCountry(country);
        Customer customer = em.find(Customer.class, 40);
        a.setCustomer(customer);
        
        repo.save(a);
        assertThat(a.getFirstName()).isEqualTo("hoa");
    }
    
    @Test
    public void setdefaultShippingTest() {
        repo.setDefaultAddress(14);
        Address a = em.find(Address.class, 14);
        assertThat(a.isDefaultForShipping()).isTrue();
    }
    
    @Test
    public void setNondefaultShippingTest() {
        repo.setNonDefaultAddress(3, 40);
        Address a = em.find(Address.class, 3);
        assertThat(a.isDefaultForShipping()).isFalse();
    }
    
    
}
