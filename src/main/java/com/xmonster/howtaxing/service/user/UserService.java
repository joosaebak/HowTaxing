package com.xmonster.howtaxing.service.user;

import com.xmonster.howtaxing.CustomException;
import com.xmonster.howtaxing.dto.common.ApiResponse;
import com.xmonster.howtaxing.dto.user.UserSignUpDto;
import com.xmonster.howtaxing.model.User;
import com.xmonster.howtaxing.repository.user.UserRepository;
import com.xmonster.howtaxing.type.ErrorCode;
import com.xmonster.howtaxing.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserUtil userUtil;

    // 회원가입
    public Object signUp(UserSignUpDto userSignUpDto) throws Exception {
        log.info(">> [Service]UserService signUp - 회원가입");

        Map<String, Object> resultMap = new HashMap<>();

        try{
            User findUser = userUtil.findCurrentUser();
            
            findUser.authorizeUser(); // 유저 권한 세팅(GUEST -> USER)
            findUser.setMktAgr(userSignUpDto.isMktAgr()); // 마케팅동의여부 세팅

            resultMap.put("role", findUser.getRole());


        }catch(Exception e){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        return ApiResponse.success(resultMap);
    }

    // 회원탈퇴
    public Object withdraw() throws Exception {
        log.info(">> [Service]UserService withdraw - 회원탈퇴");

        try{
            userRepository.deleteByEmail(userUtil.findCurrentUser().getEmail());
        }catch(Exception e){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        return ApiResponse.success(Map.of("result", "회원탈퇴가 완료되었습니다."));
    }
}