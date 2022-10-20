package com.happyshop.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.happyshop.common.entity.Role;
import com.happyshop.role.RoleRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTest {
	
	@Autowired
	RoleRepository roleRepo;
	
	@Test
	public void creatFristUserRole() {
		Role r = new Role("Admin", "manage everything");
		Role savedRole = roleRepo.save(r);
		assertThat(savedRole.getId()).isGreaterThan(0);
	}
	
	@Test
	public void creatUserRole() {
		Role r1 = new Role("Salepersons", "manage product, customers, shipping"
				+ ", orders and sales report");
		Role r2 = new Role("Editor", "manage categories, brands, products, articles and menus");
		Role r3 = new Role("Shipper", "view product, view orders, updates orders status");
		Role r4 = new Role("Assistant", "manage questions and reviews");
		List<Role> listRole = roleRepo.saveAll(List.of(r1,r2,r3,r4));
	}
}
