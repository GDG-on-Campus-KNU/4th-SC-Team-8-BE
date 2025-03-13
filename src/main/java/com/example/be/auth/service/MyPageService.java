package com.example.be.auth.service;

import com.example.be.auth.dto.PasswordRequest;
import com.example.be.auth.entity.User;
import com.example.be.auth.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class MyPageService {
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void logout(User user){
        redisTemplate.delete(user.getEmail());
    }

    @Transactional
    public void withdrawal(User user){
        logout(user);
        userRepository.delete(user);
    }

    @Transactional
    public void changePassword(User user, PasswordRequest passwordRequest){
        user.update(passwordEncoder.encode(passwordRequest.password()));

        userRepository.save(user);
    }
}
