package com.happyshop.user;



import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.happyshop.common.entity.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepo;
	
	@Override
	public Page<User> searchUser(String keyWord, Pageable pageable) {
	    return userRepo.searchUser(keyWord, pageable);
	}
	
	@Override
	public Page<User> findAll (Pageable pageable, String keyword) {
		if(!keyword.isBlank()) {
			return userRepo.searchUser(keyword, pageable);
		}
		return userRepo.findAll(pageable);
	}

	@Override
	public <S extends User> S save(S entity) {
		return userRepo.save(entity);
	}

	@Override
	public Page<User> findAll(Pageable pageable) {
		return userRepo.findAll(pageable);
	}

	@Override
	public List<User> findAll(Sort sort) {
		return userRepo.findAll(sort);
	}

	@Override
	public List<User> findAllById(Iterable<Long> ids) {
		return userRepo.findAllById(ids);
	}

	@Override
	public Optional<User> findById(Long id) {
		return userRepo.findById(id);
	}

	@Override
	public <S extends User> List<S> saveAll(Iterable<S> entities) {
		return userRepo.saveAll(entities);
	}

	@Override
	public long count() {
		return userRepo.count();
	}

	@Override
	public void deleteById(Long id) {
		userRepo.deleteById(id);
	}

	@Override
	public void delete(User entity) {
		userRepo.delete(entity);
	}

	@Override
	public void deleteAll() {
		userRepo.deleteAll();
	}

	@Override
	public User getById(Long id) {
		return userRepo.getById(id);
	}

	public List<User> findAll() {
		return userRepo.findAll();
	}

	@Override
	public User findByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	@Override
    public boolean IsEmailUnique(Long id,String email) {
	    if(id == null) {
            User user = userRepo.findByEmail(email);
            if(user != null)
                return false;
            else 
                return true;     	     
        }
        else {
            User user = userRepo.findByEmail(email);
            if(user == null) return true;
            if(user.getId() != null) {
                if(user.getId() == id) {
                    return true;
                }
                else {
                    return false;
                }
            }
            else
                return true;
        }       
    }
	@Override
	public String updateEnabledStatus(User user){
		String status= "";
		if(user.isEnable() == true) {
			user.setEnable(false);
			status = "Disable the user has id: " + user.getId()+ " successfully!";
		}
		else {
			user.setEnable(true);
			status = "Enable the user has id: " + user.getId()+ " successfully!";
		} 
		userRepo.save(user);
		return status;
	}
	
	
}
