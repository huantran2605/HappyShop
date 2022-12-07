package com.happyshop.security.oauth2;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomerOauth2User implements OAuth2User {

    private OAuth2User oauth2User;
    private String fullName;
    private String clientName;
    
    public CustomerOauth2User(OAuth2User oauth2User, String clientName) {
        this.oauth2User = oauth2User;
        this.clientName = clientName;
    }
    
    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return oauth2User.getAuthorities();
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return oauth2User.getAttribute("name");
    }
    
    public String getEmail() {
        return oauth2User.getAttribute("email");
    }
    
    public String getFullname() {
        return fullName == null ?  oauth2User.getAttribute("name") : fullName;
    }
    
    public String getClientName() {
        return this.clientName;
    }
    
    public void setFullName (String fullName) {
        this.fullName = fullName; 
    }

}
