package com.xmonster.howtaxing.service.house;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.xmonster.howtaxing.dto.jusogov.JusoGovRoadAdrRequestDto;
import com.xmonster.howtaxing.dto.jusogov.JusoGovRoadAdrResponse;
import com.xmonster.howtaxing.feign.jusogov.JusoGovRoadAdrApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class JusoGovService {
    Map<String, Object> resultMap = new HashMap<String, Object>();

    private final JusoGovRoadAdrApi jusoGovRoadAdrApi;

    @Value("${jusogov.confirm-key}")
    private String confmKey;

    public JusoGovRoadAdrResponse getRoadAdrInfo(String keyword){

        //JusoGovRoadAdrRequestDto jusoGovRoadAdrRequestDto = new JusoGovRoadAdrRequestDto(confmKey, "1", "10", keyword, "json", "Y", "none", "N");

        //ResponseEntity<?> response = jusoGovRoadAdrApi.getRoadAdrInfo(jusoGovRoadAdrRequestDto);
        /*ResponseEntity<?> response = jusoGovRoadAdrApi.getRoadAdrInfo(
                jusoGovRoadAdrRequestDto.getConfmKey(),
                jusoGovRoadAdrRequestDto.getCurrentPage(),
                jusoGovRoadAdrRequestDto.getCountPerPage(),
                jusoGovRoadAdrRequestDto.getKeyword(),
                jusoGovRoadAdrRequestDto.getResultType(),
                jusoGovRoadAdrRequestDto.getHstryYn(),
                jusoGovRoadAdrRequestDto.getFirstSort(),
                jusoGovRoadAdrRequestDto.getAddInfoYn()
        );*/

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
        /*ResponseEntity<?> response = jusoGovRoadAdrApi.getRoadAdrInfo(
                JusoGovRoadAdrRequestDto.builder()
                        .confmKey(confmKey)
                        .currentPage("1")
                        .countPerPage("10")
                        .keyword(keyword)
                        .resultType("json")
                        .hstryYn("Y")
                        .firstSort("none")
                        .addInfoYn("N")
                        .build()
        );*/

        log.info("jusogov road address response");
        log.info(response.toString());

        String jsonString = response.getBody().toString();

        JusoGovRoadAdrResponse jusoGovRoadAdrResponse = convertJsonToRealEstateData(jsonString);

        return jusoGovRoadAdrResponse;
    }

    private static JusoGovRoadAdrResponse convertJsonToRealEstateData(String jsonString) {
        try {
            // Jackson ObjectMapper를 사용하여 JSON을 자바 객체로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonString, JusoGovRoadAdrResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
