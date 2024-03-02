package com.xmonster.howtaxing.service.user;

import com.xmonster.howtaxing.CustomException;
import com.xmonster.howtaxing.dto.common.ApiResponse;
import com.xmonster.howtaxing.dto.user.UserSignUpDto;
import com.xmonster.howtaxing.model.User;
import com.xmonster.howtaxing.repository.user.UserRepository;
import com.xmonster.howtaxing.type.ErrorCode;
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

    // 회원가입
    public Object signUp(Authentication authentication, UserSignUpDto userSignUpDto) throws Exception {

        Map<String, Object> resultMap = new HashMap<>();

        try{
            log.info("[GGMANYAR]회원가입");
            log.info("이메일 : " + authentication.getName());
            log.info("[GGMANYAR]email : " + authentication.getName());
            log.info("[GGMANYAR]isMktAgr : " + userSignUpDto.isMktAgr());

            User findUser = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            log.info("[GGMANYAR]AS-IS role : " + findUser.getRole());
            log.info("[GGMANYAR]AS-IS isMktAgr : " + findUser.isMktAgr());
            
            findUser.authorizeUser(); // 유저 권한 세팅(GUEST -> USER)
            findUser.setMktAgr(userSignUpDto.isMktAgr()); // 마케팅동의여부 세팅

            resultMap.put("role", findUser.getRole());
            log.info("[GGMANYAR]TO-BE role : " + findUser.getRole());
            log.info("[GGMANYAR]TO-BE isMktAgr : " + findUser.isMktAgr());


        }catch(Exception e){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        if(resultMap.isEmpty()) throw new CustomException(ErrorCode.USER_NOT_FOUND);

        return ApiResponse.success(resultMap);
    }

    // 회원탈퇴(미사용)
    public Object withdraw(Authentication authentication) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        try{
            log.info("[GGMANYAR]회원탈퇴");
            log.info("[GGMANYAR]email : " + authentication.getName());

            userRepository.deleteByEmail(authentication.getName());

            resultMap.put("result", "회원탈퇴가 완료되었습니다.");

        }catch(Exception e){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        return ApiResponse.success(resultMap);
    }

    // 회원탈퇴
    public Object deleteUser(Authentication authentication) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        try{
            log.info("회원탈퇴");

            userRepository.deleteByEmail(authentication.getName());

            resultMap.put("result", "회원탈퇴가 완료되었습니다.");

        }catch(Exception e){
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        return ApiResponse.success(resultMap);
    }


    /*public void signUp(UserSignUpDto userSignUpDto) throws Exception {

        if (userRepository.findByEmail(userSignUpDto.getEmail()).isPresent()) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        if (userRepository.findByNickname(userSignUpDto.getNickname()).isPresent()) {
            throw new Exception("이미 존재하는 닉네임입니다.");
        }

        User user = User.builder()
                .email(userSignUpDto.getEmail())
                .password(userSignUpDto.getPassword())
                .nickname(userSignUpDto.getNickname())
                .age(userSignUpDto.getAge())
                .city(userSignUpDto.getCity())
                .role(Role.USER)
                .build();

        user.passwordEncode(passwordEncoder);
        userRepository.save(user);
    }*/
}