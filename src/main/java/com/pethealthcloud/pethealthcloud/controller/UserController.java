package com.pethealthcloud.pethealthcloud.controller;

import com.pethealthcloud.pethealthcloud.service.UserService;
import org.aspectj.bridge.IMessage;
import org.springframework.web.bind.annotation.RestController;
import com.pethealthcloud.pethealthcloud.dto.LoginRequest;
import com.pethealthcloud.pethealthcloud.dto.LoginResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.pethealthcloud.pethealthcloud.dto.RegisterRequest;
import com.pethealthcloud.pethealthcloud.dto.RegisterResponse;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(
            UserService userService
    ) {
        this.userService = userService;
    }
    // 登入 API
@PostMapping("/login")
public LoginResponse login(
        @RequestBody LoginRequest loginRequest
) {
        return userService.login(
                loginRequest
        );
}
    // 註冊 API
@PostMapping("/register")
    public RegisterResponse register(
            @RequestBody RegisterRequest registerRequest
) {
        return userService.register(
                registerRequest
        );
}

}
