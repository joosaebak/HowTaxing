package com.xmonster.howtaxing.controller.user;

import com.xmonster.howtaxing.dto.user.SocialLoginRequest;
import com.xmonster.howtaxing.service.user.UserService;
import com.xmonster.howtaxing.service.user.UserService2;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController2 {
    private final UserService2 userService;

    /*@PostMapping("/user/socialLogin")
    public ResponseEntity<LoginResponse> doSocialLogin(@RequestBody @Valid SocialLoginRequest request) {
        return ResponseEntity.created(URI.create("/socialLogin"))
                .body(userService.doSocialLogin(request));
    }*/

    @PostMapping("/user/socialLogin")
    public Map<String, Object> doSocialLogin(@RequestBody @Valid SocialLoginRequest request) {
        return userService.doSocialLogin(request);
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
