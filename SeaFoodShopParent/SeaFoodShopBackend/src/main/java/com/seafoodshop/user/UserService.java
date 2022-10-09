package com.seafoodshop.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.seafoodshop.common.entity.User;

public interface UserService {

	int SIZE_PAGE_USER = 2;

	User getById(Long id);

	void deleteAll();

	void delete(User entity);

	void deleteById(Long id);

	long count();

	<S extends User> List<S> saveAll(Iterable<S> entities);

	Optional<User> findById(Long id);

	List<User> findAllById(Iterable<Long> ids);

	List<User> findAll(Sort sort);

	Page<User> findAll(Pageable pageable);

	<S extends User> S save(S entity);
	List<User> findAll();

	User findByEmail(String email);


	String updateEnabledStatus(User user);

	Page<User> searchUser(String keyWord, Pageable pageable);

	Page<User> findAll(Pageable pageable, String keyword);

    boolean IsEmailUnique(Long id, String email);  



}
