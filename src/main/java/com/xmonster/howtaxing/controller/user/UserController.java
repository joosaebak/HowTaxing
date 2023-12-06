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
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/user/socialLogin")
    public ResponseEntity<LoginResponse> doSocialLogin(@RequestBody @Valid SocialLoginRequest request) {
        return ResponseEntity.created(URI.create("/socialLogin"))
                .body(userService.doSocialLogin(request));
    }

    @GetMapping("/user/{id}")
    public Map<String, Object> getUser(@PathVariable("id") Long id) {
        return userService.getUser(id);
    }
    /*public ResponseEntity<UserResponse> getUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                userService.getUser(id)
        );
    }*/
}
