package com.xmonster.howtaxing.service.house;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.xmonster.howtaxing.CustomException;
import com.xmonster.howtaxing.dto.jusogov.JusoGovRoadAdrResponse;
import com.xmonster.howtaxing.feign.jusogov.JusoGovRoadAdrApi;
import com.xmonster.howtaxing.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.xmonster.howtaxing.constant.CommonConstant.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class JusoGovService {
    private final JusoGovRoadAdrApi jusoGovRoadAdrApi;

    @Value("${jusogov.confirm-key}")
    private String confmKey;

    public JusoGovRoadAdrResponse getRoadAdrInfo(String keyword){

        ResponseEntity<?> response = jusoGovRoadAdrApi.getRoadAdrInfo(
                confmKey,
                "1",
                "10",
                keyword,
                "json",
                "Y",
                "none",
                "N"
        );

        log.info("jusogov road address response");
        log.info(response.toString());

        String jsonString = EMPTY;
        if(response.getBody() != null) jsonString = response.getBody().toString();

        return convertJsonToHouseData(jsonString);
    }

    private static JusoGovRoadAdrResponse convertJsonToHouseData(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonString, JusoGovRoadAdrResponse.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.HOUSE_HYPHEN_OUTPUT_ERROR);
        }
    }
}