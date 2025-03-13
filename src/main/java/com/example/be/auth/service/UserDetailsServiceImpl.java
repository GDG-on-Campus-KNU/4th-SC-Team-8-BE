package com.example.be.auth.service;

import com.example.be.auth.entity.User;
import com.example.be.auth.repository.UserRepository;
import com.example.be.common.exception.BadRequestException;
import com.example.be.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserDetailsServiceImpl implements UserDetailsService{
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.map(CustomUserDetails::new).orElseThrow(() -> new BadRequestException(ErrorCode.UNAUTHORIZED));
    }
}
