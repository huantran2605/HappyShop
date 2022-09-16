package com.seafoodshop.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seafoodshop.common.entity.Role;
@Repository
public interface RoleRepository  extends JpaRepository<Role, Integer>{

}
