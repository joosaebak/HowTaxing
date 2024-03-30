package com.xmonster.howtaxing.service.house;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.xmonster.howtaxing.CustomException;
import com.xmonster.howtaxing.dto.house.HouseListSearchRequest;
import com.xmonster.howtaxing.dto.hyphen.*;
import com.xmonster.howtaxing.feign.hyphen.HyphenAuthApi;
import com.xmonster.howtaxing.feign.hyphen.HyphenUserHouseApi;

import com.xmonster.howtaxing.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.xmonster.howtaxing.constant.CommonConstant.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class HyphenUserHouseService {
    private final HyphenAuthApi hyphenAuthApi;
    private final HyphenUserHouseApi hyphenUserHouseApi;

    @Value("${hyphen.user_id}")
    private String userId;
    @Value("${hyphen.hkey}")
    private String hKey;

    public Optional<HyphenAuthResponse> getAccessToken(){
        ResponseEntity<?> response = hyphenAuthApi.getAccessToken(
            HyphenRequestAccessTokenDto.builder()
                .user_id(userId)
                .hkey(hKey)
                .build()
        );

        log.info("hyphen auth info");
        log.info(response.toString());

        return Optional.ofNullable(new Gson()
                .fromJson(
                        response.getBody().toString(),
                        HyphenAuthResponse.class
                )
        );
    }

    public Optional<HyphenUserHouseListResponse> getUserHouseInfo(String accessToken, HouseListSearchRequest houseListSearchRequest){
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("authorization", "Bearer " + accessToken);

        if(houseListSearchRequest == null) throw new CustomException(ErrorCode.HOUSE_HYPHEN_INPUT_ERROR);

        ResponseEntity<?> response = hyphenUserHouseApi.getUserHouseInfo(
            headerMap,
            HyphenUserHouseListRequest.builder()
                    .loginMethod("EASY")
                    .loginOrgCd(houseListSearchRequest.getCertOrg())
                    .bizNo(houseListSearchRequest.getRlno())
                    .userId(houseListSearchRequest.getUserId())
                    .userPw(houseListSearchRequest.getUserPw())
                    .mobileNo(houseListSearchRequest.getMobileNo())
                    .userNm(houseListSearchRequest.getUserNm())
                    .build()
        );

        log.info("hyphen user house response");
        log.info(response.toString());

        String jsonString = EMPTY;
        if(response.getBody() != null)  jsonString = response.getBody().toString();
        System.out.println("jsonString : " + jsonString);

        HyphenUserHouseListResponse hyphenUserHouseListResponse = convertJsonToHouseData(jsonString);
        System.out.println("hyphenUserHouseListResponse : " + hyphenUserHouseListResponse);

        return Optional.ofNullable(hyphenUserHouseListResponse);
    }

    private static HyphenUserHouseListResponse convertJsonToHouseData(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonString, HyphenUserHouseListResponse.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.HOUSE_HYPHEN_OUTPUT_ERROR);
        }
    }
}
