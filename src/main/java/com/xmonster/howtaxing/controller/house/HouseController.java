package com.xmonster.howtaxing.controller.house;

import com.xmonster.howtaxing.dto.hyphen.HyphenAuthResponse;
import com.xmonster.howtaxing.dto.hyphen.HyphenUserHouseResponse;
import com.xmonster.howtaxing.dto.hyphen.HyphenUserHouseResponse.HyphenCommon;
import com.xmonster.howtaxing.dto.hyphen.HyphenUserHouseResponse.HyphenData;
import com.xmonster.howtaxing.dto.hyphen.HyphenUserHouseResponse.HyphenData.*;
import com.xmonster.howtaxing.dto.hyphen.HyphenUserHouseResultInfo;
import com.xmonster.howtaxing.dto.jusogov.JusoGovRoadAdrResponse;
import com.xmonster.howtaxing.dto.jusogov.JusoGovRoadAdrResponse.Results.JusoDetail;
import com.xmonster.howtaxing.service.house.HouseService;
import com.xmonster.howtaxing.service.house.HyphenUserHouseService;
import com.xmonster.howtaxing.service.house.JusoGovService;
import static com.xmonster.howtaxing.constant.CommonConstant.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class HouseController {

    private final HouseService houseService;
    private final HyphenUserHouseService hyphenUserHouseService;
    private final JusoGovService jusoGovService;

    public HouseController(HouseService houseService, HyphenUserHouseService hyphenUserHouseService, JusoGovService jusoGovService){
        this.houseService = houseService;
        this.hyphenUserHouseService = hyphenUserHouseService;
        this.jusoGovService = jusoGovService;
    }

    @PostMapping("/house/list")
    public Map<String, Object> getHouseList(@RequestBody Map<String, Object> requestMap){
        //return houseService.getHouseList(requestMap);
        return houseService.getUserHouseList(requestMap);
    }

    @PostMapping("/house/list2")
    public Map<String, Object> getUserHouseList(@RequestBody Map<String, Object> requestMap){

        // 1. 하이픈 Access Token 가져오기
        HyphenAuthResponse hyphenAuthResponse = hyphenUserHouseService.getAccessToken();
        String accessToken = hyphenAuthResponse.getAccess_token();

        // 2. 하이픈 주택소유정보 조회 호출
        HyphenUserHouseResponse hyphenUserHouseResponse = hyphenUserHouseService.getUserHouseInfo(accessToken);

        // 3. 조회된 결과를 정리하여 별도 Dto에 저장(HyphenUserHouseResultInfo)
        List<HyphenUserHouseResultInfo> hyphenUserHouseResultInfoList = hyphenUserHouseService.setResultDataToHyphenUserHouseResultInfo(hyphenUserHouseResponse);

        // 4. Dto List 내의 주소를 분류하여 주소기반산업지원서비스에서 조회 호출

        // 5. 주소기반산업지원서비스 조회 결과를 Dto List에 세팅

        List<DataDetail2> list2 = hyphenUserHouseResponse.getHyphenData().getList2();

        if(!list2.isEmpty()){
            for(int i=0; i<list2.size(); i++){
                System.out.println("address(" + i+1 + ")" + hyphenUserHouseResponse.getHyphenData().getList2().get(i).getAddress());
            }
        }

        StringBuffer testAddr = new StringBuffer("경기도 수원영통구 매탄동 1217");
        List<JusoDetail> jusoList = null;
        JusoGovRoadAdrResponse jusoGovRoadAdrResponse = jusoGovService.getRoadAdrInfo(testAddr.toString());

        if(jusoGovRoadAdrResponse != null && jusoGovRoadAdrResponse.getResults() != null){
            jusoList = jusoGovRoadAdrResponse.getResults().getJuso();

            if(jusoList != null && jusoList.size() > 0){
                // 아파트명을 추가하여 검색하는 로직 개발 대기 중(GGMANYAR_WAIT)
                testAddr.append(SPACE);
                testAddr.append("한국아파트");

                log.info("조회 결과가 1건 초과이기 때문에 건물명을 포함하여 재조회");
                jusoGovRoadAdrResponse = jusoGovService.getRoadAdrInfo(testAddr.toString());
                
                jusoList = jusoGovRoadAdrResponse.getResults().getJuso();
                
                if(jusoList != null){
                    if(jusoList.size() == 1){
                        System.out.println("조회된 건물관리번호 : " + jusoList.get(0).getBdMgtSn());
                    }else{
                        log.info("(오류)조회 결과 1건 초과");
                    }
                }else{
                    log.info("조회 결과 없음");
                }
            }else{
                // 조회 결과 없는 경우 로직 처리 대기 중(GGMANYAR_WAIT)
                log.info("조회 결과 없음");
            }
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("errYn", false);

        return resultMap;
    }

    // 주택 상세정보 조회
    @GetMapping("/house/detail")
    public Map<String, Object> getHouseDetail(String houseId){
        return houseService.getHouseDetail(houseId);
    }
}
