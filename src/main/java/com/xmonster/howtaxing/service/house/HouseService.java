package com.xmonster.howtaxing.service.house;

import com.xmonster.howtaxing.dto.house.HouseDetailResponse;
import com.xmonster.howtaxing.dto.house.HouseListResponse;
import com.xmonster.howtaxing.dto.hyphen.HyphenAuthResponse;
import com.xmonster.howtaxing.dto.hyphen.HyphenUserHouseResponse;
import com.xmonster.howtaxing.dto.hyphen.HyphenUserHouseResultInfo;
import com.xmonster.howtaxing.dto.jusogov.JusoGovRoadAdrResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xmonster.howtaxing.constant.CommonConstant.SPACE;

@Slf4j
@Service
public class HouseService {

    private final HyphenUserHouseService hyphenUserHouseService;
    private final JusoGovService jusoGovService;

    @Autowired
    public HouseService(HyphenUserHouseService hyphenUserHouseService, JusoGovService jusoGovService){
        this.hyphenUserHouseService = hyphenUserHouseService;
        this.jusoGovService = jusoGovService;
    }

    // 보유 주택 정보 조회
    public Map<String, Object> getHouseList(Map<String, Object> requestMap){

        Map<String, Object> resultMap = new HashMap<String, Object>();
        HouseListResponse response = null;

        if(requestMap != null && !requestMap.isEmpty()){
            response = new HouseListResponse();
        }

        if(response != null){
            resultMap.put("isError", "false");
            resultMap.put("data", response);
        }else{
            resultMap.put("isError", "true");
            resultMap.put("errCode", "1001");
            resultMap.put("errMsg", "보유 주택 정보를 조회할 수 없습니다.");
        }

        return resultMap;
    }

    public Map<String, Object> getUserHouseList(Map<String, Object> requestMap){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        HouseListResponse response = null;

        // 1. 하이픈 Access Token 가져오기
        HyphenAuthResponse hyphenAuthResponse = hyphenUserHouseService.getAccessToken();
        String accessToken = hyphenAuthResponse.getAccess_token();

        // 2. 하이픈 주택소유정보 조회 호출
        HyphenUserHouseResponse hyphenUserHouseResponse = hyphenUserHouseService.getUserHouseInfo(accessToken);

        // 3. 조회된 결과를 정리하여 별도 Dto에 저장(HyphenUserHouseResultInfo)
        List<HyphenUserHouseResultInfo> hyphenUserHouseResultInfoList = hyphenUserHouseService.setResultDataToHyphenUserHouseResultInfo(hyphenUserHouseResponse);

        // 4. Dto List 내의 주소를 분류하여 주소기반산업지원서비스에서 조회 호출

        // 5. 주소기반산업지원서비스 조회 결과를 Dto List에 세팅


        //StringBuffer testAddr = new StringBuffer("경기도 수원영통구 매탄동 1217");
        StringBuffer testAddr = new StringBuffer("강원특별자치도 원주시 명륜동 산31");
        List<JusoGovRoadAdrResponse.Results.JusoDetail> jusoList = null;
        JusoGovRoadAdrResponse jusoGovRoadAdrResponse = jusoGovService.getRoadAdrInfo(testAddr.toString());

        if(jusoGovRoadAdrResponse != null && jusoGovRoadAdrResponse.getResults() != null){
            jusoList = jusoGovRoadAdrResponse.getResults().getJuso();

            if(jusoList != null && jusoList.size() > 0){
                // 아파트명을 추가하여 검색하는 로직 개발 대기 중(GGMANYAR_WAIT)
                testAddr.append(SPACE);
                testAddr.append("더샵 센트럴파크 2단지");

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

        resultMap.put("errYn", false);

        return resultMap;
    }

    // 주택 상세정보 조회
    public Map<String, Object> getHouseDetail(String houseId){

        Map<String, Object> resultMap = new HashMap<String, Object>();
        HouseDetailResponse response = null;

        if(houseId != null && !houseId.isBlank()){
            response = new HouseDetailResponse(houseId);
        }

        if(response != null){
            resultMap.put("isError", "false");
            resultMap.put("data", response);
        }else{
            resultMap.put("isError", "true");
            resultMap.put("errCode", "1002");
            resultMap.put("errMsg", "주택 상세정보를 조회할 수 없습니다.");
        }

        return resultMap;
    }
}
