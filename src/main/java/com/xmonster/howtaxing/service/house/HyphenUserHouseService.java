package com.xmonster.howtaxing.service.house;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.xmonster.howtaxing.CustomException;
import com.xmonster.howtaxing.dto.house.HouseListSearchRequest;
import com.xmonster.howtaxing.dto.hyphen.*;
import com.xmonster.howtaxing.dto.hyphen.HyphenUserHouseListResponse.HyphenCommon;
import com.xmonster.howtaxing.dto.hyphen.HyphenUserHouseListResponse.HyphenData.*;
import com.xmonster.howtaxing.feign.hyphen.HyphenAuthApi;
import com.xmonster.howtaxing.feign.hyphen.HyphenUserHouseApi;
import static com.xmonster.howtaxing.constant.CommonConstant.*;

import com.xmonster.howtaxing.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class HyphenUserHouseService {
    Map<String, Object> resultMap = new HashMap<String, Object>();

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

        if(houseListSearchRequest == null) throw new CustomException(ErrorCode.HOUSE_FAILED_HYPHEN_INPUT);

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

        String jsonString = response.getBody().toString();

        System.out.println("[GGMANYAR]jsonString : " + jsonString);

        HyphenUserHouseListResponse hyphenUserHouseListResponse = convertJsonToRealEstateData(jsonString);

        System.out.println("[GGMANYAR]hyphenUserHouseListResponse : " + hyphenUserHouseListResponse);

        return Optional.ofNullable(hyphenUserHouseListResponse);
    }

    // 비용 절감을 위한 하드코딩 테스트 SET
    public Optional<HyphenUserHouseListResponse> getUserHouseInfoTest(String accessToken){
        Map<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put("authorization", "Bearer " + accessToken);

        // TEST DATA SET
        String jsonString = "{\n" +
                "  \"common\": {\n" +
                "    \"userTrNo\": \"\",\n" +
                "    \"hyphenTrNo\": \"10202310120000039597\",\n" +
                "    \"errYn\": \"N\",\n" +
                "    \"errMsg\": \"\"\n" +
                "  },\n" +
                "  \"data\": {\n" +
                "    \"listMsg1\": \"건축물대장정보 내역 없음\",\n" +
                "    \"list1\": [],\n" +
                "    \"listMsg2\": \"\",\n" +
                "    \"list2\": [\n" +
                "      {\n" +
                "        \"name\": \"김**\",\n" +
                "        \"address\": \"경기도 의왕시 삼동 192-2 대경빌라-****호\",\n" +
                "        \"sellBuyClassification\": \"매수(일반)\",\n" +
                "        \"area\": \"60.84\",\n" +
                "        \"tradingPrice\": \"270000000\",\n" +
                "        \"balancePaymentDate\": \"20220519\",\n" +
                "        \"contractDate\": \"20220423\",\n" +
                "        \"startDate\": \"20060101\",\n" +
                "        \"endDate\": \"20231012\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"김**\",\n" +
                "        \"address\": \"경기도 수원영통구 매탄동 1217 한국아파트 ****동-****호\",\n" +
                "        \"sellBuyClassification\": \"매도(일반)\",\n" +
                "        \"area\": \"72.84\",\n" +
                "        \"tradingPrice\": \"660000000\",\n" +
                "        \"balancePaymentDate\": \"20220519\",\n" +
                "        \"contractDate\": \"20220405\",\n" +
                "        \"startDate\": \"20060101\",\n" +
                "        \"endDate\": \"20231012\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"name\": \"김**\",\n" +
                "        \"address\": \"경기도 수원영통구 매탄동 1217 한국아파트 ****동-****호\",\n" +
                "        \"sellBuyClassification\": \"매수(일반)\",\n" +
                "        \"area\": \"72.84\",\n" +
                "        \"tradingPrice\": \"295000000\",\n" +
                "        \"balancePaymentDate\": \"20191223\",\n" +
                "        \"contractDate\": \"20191030\",\n" +
                "        \"startDate\": \"20060101\",\n" +
                "        \"endDate\": \"20231012\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"listMsg3\": \"\",\n" +
                "    \"list3\": [\n" +
                "      {\n" +
                "        \"name\": \"김************************************\",\n" +
                "        \"address\": \"경기도 의왕시 삼동 192-2 대경아임미                                         0001동 08102호\",\n" +
                "        \"area\": \"60.84\",\n" +
                "        \"acquisitionDate\": \"20220519\",\n" +
                "        \"baseDate\": \"20230321\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        System.out.println("[GGMANYAR]jsonString : " + jsonString);

        HyphenUserHouseListResponse hyphenUserHouseListResponse = convertJsonToRealEstateData(jsonString);

        System.out.println("[GGMANYAR]hyphenUserHouseListResponse : " + hyphenUserHouseListResponse);

        return Optional.ofNullable(hyphenUserHouseListResponse);
    }

    public List<HyphenUserHouseResultInfo> setResultDataToHyphenUserHouseResultInfo(HyphenUserHouseListResponse responseData){

        List<HyphenUserHouseResultInfo> houseList = new ArrayList<HyphenUserHouseResultInfo>();

        if(responseData != null && responseData.getHyphenCommon() != null && responseData.getHyphenData() != null){
            HyphenCommon hyphenCommon = responseData.getHyphenCommon();
            List<DataDetail1> list1 = responseData.getHyphenData().getList1();
            List<DataDetail2> list2 = responseData.getHyphenData().getList2();
            List<DataDetail3> list3 = responseData.getHyphenData().getList3();

            String errYn = hyphenCommon.getErrYn();
            String errMsg = hyphenCommon.getErrMsg();

            // 조회 결과 : 정상
            if(NO.equals(errYn)){
                // List1 - 건축물대장정보목록
                if(list1 != null && !list1.isEmpty()) {
                    for(DataDetail1 dataDetail1 : list1) {
                        houseList.add(HyphenUserHouseResultInfo.builder()
                                .resultListNo(ONE)
                                .tradeType(ZERO)
                                .orgAdr(dataDetail1.getAddress())
                                .area(new BigDecimal(dataDetail1.getArea()))
                                .buyDate(LocalDate.parse(dataDetail1.getOwnershipChangeDate(), DateTimeFormatter.ofPattern("yyyyMMdd")))
                                .pubLandPrice(Long.parseLong(dataDetail1.getPublishedPrice()))
                                .build()
                        );
                    }
                }

                // List2 - 부동산 거래내역(주택분)
                if(list2 != null && !list2.isEmpty()){
                    for(DataDetail2 dataDetail2 : list2){
                        String tradeTypeNm = dataDetail2.getSellBuyClassification();
                        String tradeType = EMPTY;
                        String houseType = EMPTY;

                        if(tradeTypeNm != null){
                            if(tradeTypeNm.contains("분양권")){
                                houseType = FIVE;   // 분양권
                            }else{
                                houseType = SIX;    // 주택
                            }

                            if(tradeTypeNm.contains("매수")){
                                tradeType = ONE;
                            }else if(tradeTypeNm.contains("매도")){
                                tradeType = TWO;
                            }
                        }
                        houseList.add(HyphenUserHouseResultInfo.builder()
                                .resultListNo(TWO)
                                .tradeType(tradeType)
                                .orgAdr(dataDetail2.getAddress())
                                .houseType(houseType)
                                .area(new BigDecimal(dataDetail2.getArea()))
                                .sellPrice(Long.parseLong(dataDetail2.getTradingPrice()))
                                .buyDate(LocalDate.parse(dataDetail2.getBalancePaymentDate(), DateTimeFormatter.ofPattern("yyyyMMdd")))
                                .contractDate(LocalDate.parse(dataDetail2.getContractDate(), DateTimeFormatter.ofPattern("yyyyMMdd")))
                                .build()
                        );
                    }
                }

                // List3 - 재산세정보(주택분)
                if(list3 != null && !list3.isEmpty()){
                    for(DataDetail3 dataDetail3 : list3){
                        houseList.add(HyphenUserHouseResultInfo.builder()
                                .resultListNo(THREE)
                                .tradeType(ZERO)
                                .orgAdr(dataDetail3.getAddress())
                                .area(new BigDecimal(dataDetail3.getArea()))
                                .buyDate(LocalDate.parse(dataDetail3.getAcquisitionDate(), DateTimeFormatter.ofPattern("yyyyMMdd")))
                                .build()
                        );
                    }
                }

                // 매수 - 매도(매수에 동일한 주소가 2개 이상이면 해당 주소의 모든 매수는 alive)
                List<HyphenUserHouseResultInfo> tmpHouseBuyList = new ArrayList<HyphenUserHouseResultInfo>();
                List<HyphenUserHouseResultInfo> tmpHouseSellList = new ArrayList<HyphenUserHouseResultInfo>();
                List<HyphenUserHouseResultInfo> tmpHouseList = new ArrayList<HyphenUserHouseResultInfo>();

                if(!houseList.isEmpty()){
                    for(HyphenUserHouseResultInfo houseInfo : houseList){
                        // 부동산 거래내역(List2)
                        if(TWO.equals(houseInfo.getResultListNo())){
                            // 매수 리스트
                            if(ONE.equals(houseInfo.getTradeType())){
                                tmpHouseBuyList.add(houseInfo.clone());
                            }
                            // 매도 리스트
                            else if(TWO.equals(houseInfo.getTradeType())){
                                tmpHouseSellList.add(houseInfo.clone());
                            }
                        }
                    }
                }

                if(!tmpHouseBuyList.isEmpty()){
                    for(HyphenUserHouseResultInfo houseBuyInfo : tmpHouseBuyList){
                        int count = 0;
                        boolean isEqual = false;
                        String keyAdr = houseBuyInfo.getOrgAdr();

                        for(HyphenUserHouseResultInfo compareBuyInfo : tmpHouseBuyList){
                            String compAdr = compareBuyInfo.getOrgAdr();
                            if(keyAdr.equals(compAdr)) count++;
                        }
                        
                        // 동일한 주소의 매수가 2건 이상이 아닌 경우
                        if(count == 1){
                            if(!tmpHouseSellList.isEmpty()){
                                for(HyphenUserHouseResultInfo houseSellInfo : tmpHouseSellList){
                                    String sellAdr = houseSellInfo.getOrgAdr();

                                    if(keyAdr.equals(sellAdr)){
                                        isEqual = true;
                                        break;
                                    }
                                }

                                // 주소가 같은 매도가 없는 경우
                                if(!isEqual){
                                    tmpHouseList.add(houseBuyInfo.clone());
                                }
                            }
                        }
                        // 동일한 주소의 매수가 2건 이상 존재하는 경우(일단 등록)
                        else{
                            tmpHouseList.add(houseBuyInfo.clone());
                        }
                    }
                }

                // 기존 부동산 거래내역 삭제
                houseList.removeIf(houseInfo -> TWO.equals(houseInfo.getResultListNo()));

                // 신규 부동산 거래내역(정리된 매수내역) 추가
                for(HyphenUserHouseResultInfo tobeHouseBuyInfo : tmpHouseList){
                    houseList.add(tobeHouseBuyInfo.clone());
                }
            }

            System.out.println("============ HyphenUserHouseResultInfo PRINT START ============");
            int index = 1;
            for(HyphenUserHouseResultInfo resultInfo : houseList){
                System.out.println("---------------------------------");

                //System.out.println("resultListNo : " + resultInfo.getResultListNo());
                //System.out.println("tradeType : " + resultInfo.getTradeType());
                //System.out.println("orgAdr : " + resultInfo.getOrgAdr());
                //System.out.println("houseType : " + resultInfo.getHouseType());
                //System.out.println("houseName : " + resultInfo.getHouseName());
                //System.out.println("houseDetailName : " + resultInfo.getHouseDetailName());
                System.out.println("HyphenUserHouseResultInfo(" + index++ + ") : " + resultInfo.toString());

                System.out.println("---------------------------------");
            }
            System.out.println("============ HyphenUserHouseResultInfo PRINT END ============");
        }

        return houseList;
    }

    private static HyphenUserHouseListResponse convertJsonToRealEstateData(String jsonString) {
        try {
            // Jackson ObjectMapper를 사용하여 JSON을 자바 객체로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonString, HyphenUserHouseListResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
