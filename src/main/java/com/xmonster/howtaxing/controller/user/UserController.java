package com.xmonster.howtaxing.controller.user;

import com.xmonster.howtaxing.dto.user.SocialLoginRequest;
import com.xmonster.howtaxing.dto.user.LoginResponse;
import com.xmonster.howtaxing.dto.user.UserResponse;
import com.xmonster.howtaxing.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/social-login")
    public ResponseEntity<LoginResponse> doSocialLogin(@RequestBody @Valid SocialLoginRequest request) {
        return ResponseEntity.created(URI.create("/social-login"))
                .body(userService.doSocialLogin(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                userService.getUser(id)
        );
    }
}
