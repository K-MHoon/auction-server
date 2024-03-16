package com.kmhoon.web.controller.user;

import com.kmhoon.web.controller.dto.user.request.CreateUserControllerRequest;
import com.kmhoon.web.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/api/user/register")
    @ResponseStatus(HttpStatus.OK)
    public void register(@RequestBody @Valid CreateUserControllerRequest request) {
        service.register(request);
    }
}
