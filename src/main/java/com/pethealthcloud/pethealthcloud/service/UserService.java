package com.pethealthcloud.pethealthcloud.service;

import com.pethealthcloud.pethealthcloud.dto.LoginResponse;
import com.pethealthcloud.pethealthcloud.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.pethealthcloud.pethealthcloud.dto.LoginRequest;
import com.pethealthcloud.pethealthcloud.dto.PetResponse;
import com.pethealthcloud.pethealthcloud.dto.RegisterRequest;
import com.pethealthcloud.pethealthcloud.dto.RegisterResponse;
import com.pethealthcloud.pethealthcloud.entity.User;
import com.pethealthcloud.pethealthcloud.config.JwtUtil;

// 使用者相關商業邏輯
@Service
public class UserService {
    // 操作 users 資料表
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public UserService(
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder,
            JwtUtil jwtUtil
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    // 使用者登入
    public LoginResponse login(
            LoginRequest loginRequest
    ) {
        User user = userRepository.findByEmail(
                loginRequest.getEmail()
        );

            LoginResponse response =
                    new LoginResponse();

            if (user == null) {
                response.setMessage("帳號不存在");
                return response;
            }

            if (!passwordEncoder.matches(
                    loginRequest.getPassword(),
                    user.getPassword()
            )) {
                response.setMessage("密碼錯誤");
                return response;
            }

            String token =
                    jwtUtil.generateToken(
                            user.getEmail()
                    );

            response.setId(
                    user.getId()
            );

            response.setEmail(
                    user.getEmail()
            );


            response.setToken(
                    token
            );

            response.setMessage("登入成功");

            return response;
    }
    // 使用者註冊
    public RegisterResponse register(
            RegisterRequest registerRequest
    ){
        User existUser = userRepository.findByEmail(
                registerRequest.getEmail()
        );

        if (existUser != null) {
            RegisterResponse response = new RegisterResponse();

            response.setMessage("Email已存在");

            return response;
        }

        User user = new User();

        user.setEmail(
                registerRequest.getEmail()
        );

        user.setPassword(
                passwordEncoder.encode(
                        registerRequest.getPassword()
                )

        );

        userRepository.save(user);

        RegisterResponse response =
                new RegisterResponse();

        response.setMessage("註冊成功");

        return response;
    }
}
