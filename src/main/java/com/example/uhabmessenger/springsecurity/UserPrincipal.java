package com.example.uhabmessenger.springsecurity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserPrincipal implements UserDetails {
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(String username, String password, Collection<? extends GrantedAuthority> authorities) {

        this.username = username;
        this.password = password;
        this.authorities = authorities;

    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return authorities;

    }

    @Override
    public String getPassword() {

        return password;

    }

    @Override
    public String getUsername() {

        return username;

    }

}

