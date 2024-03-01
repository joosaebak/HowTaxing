package com.xmonster.howtaxing.controller.user;

import com.xmonster.howtaxing.CustomException;
import com.xmonster.howtaxing.dto.common.ApiResponse;
import com.xmonster.howtaxing.dto.user.UserSignUpDto;
import com.xmonster.howtaxing.service.user.UserService;
import com.xmonster.howtaxing.type.ErrorCode;
import static com.xmonster.howtaxing.constant.CommonConstant.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/user/signUp")
    public Object signUp(Authentication authentication, @RequestBody UserSignUpDto userSignUpDto) throws Exception {
        return userService.signUp(authentication, userSignUpDto);
    }

    // 삭제 예정
    @GetMapping("/user/withdraw")
    public Object withdraw(Authentication authentication) throws Exception {
        return userService.withdraw(authentication);
    }

    @DeleteMapping("/user/delete")
    public Object deleteUser(Authentication authentication) throws Exception {
        return userService.deleteUser(authentication);
    }

    // 미사용
    @GetMapping("/jwt-test")
    public String jwtTest() {
        return "jwtTest 요청 성공";
    }

    @GetMapping("/oauth2/loginSuccess")
    public Object loginSuccess(@RequestParam String accessToken, @RequestParam String refreshToken, @RequestParam String role){
        Map<String, Object> tokenMap = new HashMap<>();

        if(accessToken == null || refreshToken == null){
            throw new CustomException(ErrorCode.LOGIN_FAILED_COMMON);
        }

        tokenMap.put("accessToken", accessToken);
        tokenMap.put("refreshToken", refreshToken);
        tokenMap.put("role", role);

        return ApiResponse.success(tokenMap);
    }

    @GetMapping("/oauth2/loginFail")
    public Object loginFail(@RequestParam String socialType){
        log.info("socialType : " + socialType);

        if(socialType != null && !EMPTY.equals(socialType)){
            throw new CustomException(ErrorCode.LOGIN_FAILED_HAS_EMAIL, socialType + "를 통해 동일한 이메일이 가입되어 있습니다.");
        }else{
            throw new CustomException(ErrorCode.LOGIN_FAILED_COMMON);
        }
    }
}
