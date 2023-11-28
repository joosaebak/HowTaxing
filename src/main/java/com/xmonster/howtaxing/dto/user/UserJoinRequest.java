package com.xmonster.howtaxing.dto.user;

import com.xmonster.howtaxing.type.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserJoinRequest {
    private String userId;
    private String userName;
    private UserType userType;
    private String userEmail;
}
