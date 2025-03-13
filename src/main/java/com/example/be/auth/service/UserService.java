package com.example.be.auth.service;

import com.example.be.auth.dto.LoginRequest;
import com.example.be.auth.dto.LoginResponse;
import com.example.be.auth.dto.RefreshRequest;
import com.example.be.auth.dto.RegisterRequest;
import com.example.be.auth.entity.User;
import com.example.be.auth.jwt.JwtTokenProvider;
import com.example.be.auth.repository.UserRepository;
import com.example.be.common.exception.BadRequestException;
import com.example.be.common.exception.ConflictException;
import com.example.be.common.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public void register(RegisterRequest registerRequest){
        checkEmail(registerRequest.email());
        checkUsername(registerRequest.username());

        User user = new User(registerRequest.email(), registerRequest.username(), passwordEncoder.encode(registerRequest.password()));

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response){
        try{
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();

            return new LoginResponse(jwtTokenProvider.createAccessToken(user), jwtTokenProvider.createRefreshToken(user));
        }
        catch(BadCredentialsException e){
            throw new ConflictException(ErrorCode.LOGIN_FAIL);
        }
    }

    @Transactional(readOnly = true)
    public void checkEmail(String email) {
        if (userRepository.existsByEmail(email)) throw new ConflictException(ErrorCode.DUPLICATE_EMAIL);
    }

    @Transactional(readOnly = true)
    public void checkUsername(String username) {
        if (userRepository.existsByUsername(username)) throw new ConflictException(ErrorCode.DUPLICATE_USERNAME);
    }

    @Transactional(readOnly = true)
    public LoginResponse refresh(RefreshRequest refreshRequest){
        if(jwtTokenProvider.validateRefreshToken(refreshRequest.refreshToken())){
            String email = jwtTokenProvider.getEmail(refreshRequest.refreshToken());
            String storedRefreshToken = (String) redisTemplate.opsForHash().get(email, "refreshToken");

            if(storedRefreshToken.equals(refreshRequest.refreshToken())){
                User user = userRepository.findByEmail(email).get();
                return new LoginResponse(jwtTokenProvider.createAccessToken(user), jwtTokenProvider.createRefreshToken(user));
            }
        }
        
        throw new BadRequestException(ErrorCode.INVALID_REFRESH_TOKEN);
    }
}
