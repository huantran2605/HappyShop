package com.happyshop.common.entity;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.*;

import com.happyshop.common.Constants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
     
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
     
    @Column(nullable = false, unique = true, length = 45)
    private String email;
     
    @Column(nullable = false, length = 64)
    private String password;  
     
    @Column(name = "first_name", nullable = false, length = 20)
    private String firstName;
     
    @Column(name = "last_name", nullable = false, length = 20)
    private String lastName;
    @Column( length = 64)
    private String photo;
     
    @ManyToMany( fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
            )
    private Set<Role> roles = new HashSet<>();
    
    @Column(nullable = false)
    private boolean enable ;
    
    public void updateUser(User user) {
    	this.setFirstName(user.getFirstName());
    	this.setLastName(user.getLastName());
    }

	public User(String email, String password, String firstName, String lastName, Set<Role> roles) {
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.roles = roles;
	}
	
	
	@Transient
	public String getUserPhotoPath () {
		return Constants.S3_BASE_URI+ "/users-photo/"+ this.id+"/" + this.photo ;   
	}
	
	@Transient
	public String getFullName () {
		
		return this.firstName + " " + this.lastName ; 
	}
	
	public boolean hasRole(String roleName) {
        Iterator<Role> iterator = roles.iterator();
        
        while (iterator.hasNext()) {
            Role role = iterator.next();
            if (role.getName().equals(roleName)) {
                return true;
            }
        }
        
        return false;
    }

    public User(Long id) {        
        this.id = id;
    }
		
	
    
    
}