package com.xmonster.howtaxing.service.user;

import com.xmonster.howtaxing.NotFoundException;
import com.xmonster.howtaxing.dto.user.*;
import com.xmonster.howtaxing.model.User;
import com.xmonster.howtaxing.repository.user.UserRepository;
import com.xmonster.howtaxing.type.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final List<SocialLoginService> loginServices;
    private final UserRepository userRepository;

    // AccessToken으로 요청하는 소셜로그인
    public LoginResponse doSocialLogin(SocialLoginRequest request) {
        SocialLoginService loginService = this.getLoginService(request.getUserType());

        //SocialAuthResponse socialAuthResponse = loginService.getAccessToken(request.getCode());

        //SocialUserResponse socialUserResponse = loginService.getUserInfo(socialAuthResponse.getAccess_token());
        SocialUserResponse socialUserResponse = loginService.getUserInfo(request.getAccessToken());

        log.info("socialUserResponse {} ", socialUserResponse.toString());

        if (userRepository.findByUserId(socialUserResponse.getId()).isEmpty()) {
            this.joinUser(
                    UserJoinRequest.builder()
                            .userId(socialUserResponse.getId())
                            .userEmail(socialUserResponse.getEmail())
                            .userName(socialUserResponse.getName())
                            .userType(request.getUserType())
                            .build()
            );
        }

        User user = userRepository.findByUserId(socialUserResponse.getId())
                .orElseThrow(() -> new NotFoundException("ERROR_001", "유저 정보를 찾을 수 없습니다."));

        return LoginResponse.builder()
                .id(user.getId())
                .build();
    }

    // 기존 소셜로그인
    /*public LoginResponse doSocialLogin(SocialLoginRequest request) {
        SocialLoginService loginService = this.getLoginService(request.getUserType());

        SocialAuthResponse socialAuthResponse = loginService.getAccessToken(request.getCode());

        SocialUserResponse socialUserResponse = loginService.getUserInfo(socialAuthResponse.getAccess_token());
        log.info("socialUserResponse {} ", socialUserResponse.toString());

        if (userRepository.findByUserId(socialUserResponse.getId()).isEmpty()) {
            this.joinUser(
                    UserJoinRequest.builder()
                            .userId(socialUserResponse.getId())
                            .userEmail(socialUserResponse.getEmail())
                            .userName(socialUserResponse.getName())
                            .userType(request.getUserType())
                            .build()
            );
        }

        User user = userRepository.findByUserId(socialUserResponse.getId())
                .orElseThrow(() -> new NotFoundException("ERROR_001", "유저 정보를 찾을 수 없습니다."));

        return LoginResponse.builder()
                .id(user.getId())
                .build();
    }*/

    private UserJoinResponse joinUser(UserJoinRequest userJoinRequest) {
        User user = userRepository.save(
            User.builder()
                    .userId(userJoinRequest.getUserId())
                    .userType(userJoinRequest.getUserType())
                    .userEmail(userJoinRequest.getUserEmail())
                    .userName(userJoinRequest.getUserName())
                    .build()
        );

        return UserJoinResponse.builder()
                .id(user.getId())
                .build();
    }

    private SocialLoginService getLoginService(UserType userType){
        for (SocialLoginService loginService: loginServices) {
            if (userType.equals(loginService.getServiceName())) {
                log.info("login service name: {}", loginService.getServiceName());
                return loginService;
            }
        }
        return new LoginServiceImpl();
    }

    public Map<String, Object> getUser(Long id) {
        boolean isError = false;
        Map<String, Object> resultMap = new HashMap<String, Object>();

        /*User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("0002", "사용자 정보를 찾을 수 없습니다."));*/

        User user = userRepository.findById(id).orElse(null);

        if(user != null){
            UserResponse response = UserResponse.builder()
                    .id(user.getId())
                    .userId(user.getUserId())
                    .userEmail(user.getUserEmail())
                    .userName(user.getUserName())
                    .build();

            resultMap.put("isError", "false");
            resultMap.put("data", response);
        }else{
            resultMap.put("isError", "true");
            resultMap.put("errCode", "0002");
            resultMap.put("errMsg", "사용자 정보를 조회할 수 없습니다.");
        }

        return resultMap;
    }
}
