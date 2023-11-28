package com.xmonster.howtaxing.dto.user;

import com.xmonster.howtaxing.type.UserType;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class SocialLoginRequest {
    @NotNull
    private UserType userType;
    @NotNull
    private String code;
}
