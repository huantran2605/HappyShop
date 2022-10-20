package com.happyshop.user;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.happyshop.common.entity.User;
@Repository
public interface UserRepository  extends JpaRepository<User, Long>{
	
	@Query("SELECT u FROM User u WHERE u.email = :email")
	public User findByEmail( @Param("email") String email); 
	
	@Query("SELECT u FROM User u WHERE CONCAT(u.id,' ',u.email,' ', u.firstName,' ',"
			+ " u.lastName ) LIKE %?1%")
	public Page<User> searchUser (String keyWord, Pageable pageable);		

	
}
