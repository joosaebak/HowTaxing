package com.xmonster.howtaxing.utils;

import com.xmonster.howtaxing.CustomException;
import com.xmonster.howtaxing.type.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    private SecurityUtil() {}

    public static String getCurrentMemberEmail(){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || authentication.getName() == null){
            throw new CustomException(ErrorCode.ETC_ERROR, "사용자 인증에 실패하였습니다.");
        }

        return authentication.getName();
    }
}
