package com.xmonster.howtaxing.service.user;

import com.xmonster.howtaxing.dto.user.SocialAuthResponse;
import com.xmonster.howtaxing.dto.user.SocialUserResponse;
import com.xmonster.howtaxing.type.UserType;
import org.springframework.stereotype.Service;

@Service
public interface SocialLoginService {
    UserType getServiceName();
    SocialAuthResponse getAccessToken(String authorizationCode);
    SocialUserResponse getUserInfo(String accessToken);
}
