package com.backend.blogApp.service;

import com.backend.blogApp.entity.CustomUserDetails;
import com.backend.blogApp.entity.User;
import com.backend.blogApp.exception.ResourceNotFoundException;
import com.backend.blogApp.repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    public CustomUserDetailsService(UserRepo userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User","email", username));
        return new CustomUserDetails(user);
    }
}
