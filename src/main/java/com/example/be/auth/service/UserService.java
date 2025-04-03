package com.example.be.auth.service;

import com.example.be.auth.dto.*;
import com.example.be.auth.entity.User;
import com.example.be.auth.jwt.JwtTokenProvider;
import com.example.be.auth.properties.GoogleLoginProperties;
import com.example.be.auth.repository.UserRepository;
import com.example.be.common.exception.BadRequestException;
import com.example.be.common.exception.ConflictException;
import com.example.be.common.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@Service
@AllArgsConstructor
@EnableConfigurationProperties(GoogleLoginProperties.class)
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;
    private final GoogleLoginProperties googleLoginProperties;
    private final RestTemplate restTemplate;

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

    public String getGoogleLoginURI(HttpServletRequest request){
        return "https://accounts.google.com/o/oauth2/v2/auth?client_id=" +
                googleLoginProperties.getClientId() +
                "&redirect_uri=" +
                getRedirectURI(request) +
                "&response_type=code&scope=" +
                googleLoginProperties.getScope();
    }

    @Transactional
    public LoginResponse googleLogin(String code, HttpServletRequest request){
        GoogleAccessTokenResponse googleAccessTokenResponse = getGoogleAccessToken(code, request);
        GoogleAccountProfileResponse googleAccountProfileResponse = getGoogleAccountProfile(googleAccessTokenResponse);

        User user;
        if(!userRepository.existsByEmail(googleAccountProfileResponse.email())){
            user = new User(googleAccountProfileResponse.email(),
                    generateRandomName(googleAccountProfileResponse.name()),
                    "google");
            userRepository.save(user);
        }
        else user = userRepository.findByEmail(googleAccountProfileResponse.email()).get();

        return new LoginResponse(jwtTokenProvider.createAccessToken(user), jwtTokenProvider.createRefreshToken(user));
    }

    private String getRedirectURI(HttpServletRequest request){
        return request.getScheme() + "://" + request.getHeader("Host") + "/login/oauth2/code/google";
    }

    private GoogleAccessTokenResponse getGoogleAccessToken(String code, HttpServletRequest request){
        String decodedCode = URLDecoder.decode(code, StandardCharsets.UTF_8);
        String requestUri = getRedirectURI(request);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<GoogleAccessTokenRequest> httpEntity = new HttpEntity<>(
                new GoogleAccessTokenRequest(decodedCode,
                        googleLoginProperties.getClientId(),
                        googleLoginProperties.getClientSecret(),
                        requestUri,
                        "authorization_code"),
                headers
        );

        return restTemplate.exchange(
                googleLoginProperties.getTokenURI(),
                HttpMethod.POST,
                httpEntity,
                GoogleAccessTokenResponse.class
        ).getBody();
    }

    private GoogleAccountProfileResponse getGoogleAccountProfile(GoogleAccessTokenResponse googleAccessTokenResponse){
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + googleAccessTokenResponse.access_token());
        HttpEntity<GoogleAccessTokenRequest> httpEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                googleLoginProperties.getProfileURI(),
                HttpMethod.GET,
                httpEntity, GoogleAccountProfileResponse.class).getBody();
    }

    private String generateRandomName(String name){
        Random random = new Random();
        String randomNumber;
        do{
            randomNumber = String.format("%04d", random.nextInt(10000));
        } while(userRepository.existsByUsername(name + randomNumber));

        return name + randomNumber;
    }
}
