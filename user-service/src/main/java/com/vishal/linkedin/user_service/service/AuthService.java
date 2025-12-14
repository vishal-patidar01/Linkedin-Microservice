package com.vishal.linkedin.user_service.service;

import com.vishal.linkedin.user_service.dto.LoginRequestDto;
import com.vishal.linkedin.user_service.dto.SignupRequestDto;
import com.vishal.linkedin.user_service.dto.UserDto;
import com.vishal.linkedin.user_service.entity.User;
import com.vishal.linkedin.user_service.exceptions.BadRequestException;
import com.vishal.linkedin.user_service.exceptions.ResourceNotFoundException;
import com.vishal.linkedin.user_service.repository.UserRepository;
import com.vishal.linkedin.user_service.utils.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

    public UserDto signUp(SignupRequestDto signupRequestDto) {

        boolean exists = userRepository.existsByEmail(signupRequestDto.getEmail());
        if(exists) {
            throw new BadRequestException("user already exists, cannot signup again");
        }

        User user = modelMapper.map(signupRequestDto, User.class);
        user.setPassword(PasswordUtil.hashPassword(signupRequestDto.getPassword()));

        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    public String login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("user not found with email: "+loginRequestDto.getEmail()));

        boolean isPasswordMatch = PasswordUtil.checkPassword(loginRequestDto.getPassword(), user.getPassword());

        if(!isPasswordMatch) throw new BadRequestException("Incorrect password");

        return jwtService.generateAccessToken(user);
    }
}
