package com.seafoodshop.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.seafoodshop.common.entity.Role;
import com.seafoodshop.common.entity.User;
import com.seafoodshop.role.RoleRepository;
import com.seafoodshop.user.UserRepository;
import com.seafoodshop.user.UserService;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {
	

	@Autowired
	UserRepository userRepo;
	
	
	@Autowired
	TestEntityManager testEntity;
	@Test
	public void creatFristUser() {
		Role r = testEntity.find(Role.class, 1);
		Set<Role> roles = new HashSet<>();
		roles.add(r);
		User firstUser = new User("huantran2605@gmail.com", "123456", "Huan","Tran", roles );
		userRepo.save(firstUser);
		assertThat(firstUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void checkEmailDb() {
		User user = userRepo.findByEmail("huantran2605@gmail.com");
		assertThat(user).isNotNull();
	}
	

}
