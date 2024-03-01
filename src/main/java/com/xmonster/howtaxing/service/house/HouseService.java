package com.xmonster.howtaxing.service.house;

import com.xmonster.howtaxing.CustomException;
import com.xmonster.howtaxing.dto.common.ApiResponse;
import com.xmonster.howtaxing.dto.house.HouseAddressDto;
import com.xmonster.howtaxing.dto.house.HouseDetailResponse;
import com.xmonster.howtaxing.dto.house.HouseListResponse;
import com.xmonster.howtaxing.dto.hyphen.HyphenAuthResponse;
import com.xmonster.howtaxing.dto.hyphen.HyphenUserHouseResponse;
import com.xmonster.howtaxing.dto.hyphen.HyphenUserHouseResultInfo;
import com.xmonster.howtaxing.dto.jusogov.JusoGovRoadAdrResponse;
import com.xmonster.howtaxing.dto.jusogov.JusoGovRoadAdrResponse.Results.Common;
import com.xmonster.howtaxing.dto.jusogov.JusoGovRoadAdrResponse.Results.JusoDetail;
import com.xmonster.howtaxing.dto.hyphen.HyphenUserHouseResponse.HyphenCommon;
import com.xmonster.howtaxing.dto.hyphen.HyphenUserHouseResponse.HyphenData.*;
import com.xmonster.howtaxing.model.House;
import com.xmonster.howtaxing.type.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.xmonster.howtaxing.constant.CommonConstant.*;
import static com.xmonster.howtaxing.constant.CommonConstant.TWO;

@Slf4j
@Service
public class HouseService {

    private final HyphenUserHouseService hyphenUserHouseService;
    private final JusoGovService jusoGovService;
    private final HouseAddressService houseAddressService;

    private static final int MAX_JUSO_CALL_CNT = 3; // 주소 한건 당 주소기반산업지원서비스 도로명주소 재조회 호출 건수 최대값

    @Autowired
    public HouseService(HyphenUserHouseService hyphenUserHouseService, JusoGovService jusoGovService, HouseAddressService houseAddressService){
        this.hyphenUserHouseService = hyphenUserHouseService;
        this.jusoGovService = jusoGovService;
        this.houseAddressService = houseAddressService;
    }

    // 보유 주택 정보 조회
    public Object getHouseList(Map<String, Object> requestMap){

        Map<String, Object> resultMap = new HashMap<String, Object>();
        HouseListResponse response = null;

        if(requestMap != null && !requestMap.isEmpty()){
            response = new HouseListResponse();
        }

        if(response != null){
            //resultMap.put("isError", "false");
            //resultMap.put("data", response);
            return ApiResponse.success(response);
        }else{
            //resultMap.put("isError", "true");
            //resultMap.put("errCode", "1001");
            //resultMap.put("errMsg", "보유 주택 정보를 조회할 수 없습니다.");
            return ApiResponse.error("보유 주택 정보를 조회할 수 없습니다.");
        }
    }

    public Map<String, Object> getUserHouseList(Map<String, Object> requestMap){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        HouseListResponse response = null;
        boolean errFlag = false;

        // 1. 하이픈 Access Token 가져오기
        HyphenAuthResponse hyphenAuthResponse = hyphenUserHouseService.getAccessToken()
                .orElseThrow(() -> new CustomException(ErrorCode.HOUSE_FAILED_HYPHEN_TOKEN, "하이픈에서 AccessToken을 가져오는데 실패했습니다."));
        String accessToken = hyphenAuthResponse.getAccess_token();

        // 2. 하이픈 주택소유정보 조회 호출
        HyphenUserHouseResponse hyphenUserHouseResponse = hyphenUserHouseService.getUserHouseInfo(accessToken)
                .orElseThrow(() -> new CustomException(ErrorCode.HOUSE_FAILED_HYPHEN_LIST, "하이픈에서 보유주택정보 데이터를 가져오는데 실패했습니다."));

        // 3. 하이픈 보유주택조회 결과가 정상인지 체크하여, 정상인 경우 조회 결과를 정리하여 별도 DTO에 저장
        //List<HyphenUserHouseResultInfo> hyphenUserHouseResultInfoList = null;
        List<House> houseList = new ArrayList<House>();
        HyphenCommon hyphenCommon = hyphenUserHouseResponse.getHyphenCommon();
        List<DataDetail1> list1 = hyphenUserHouseResponse.getHyphenData().getList1();
        List<DataDetail2> list2 = hyphenUserHouseResponse.getHyphenData().getList2();
        List<DataDetail3> list3 = hyphenUserHouseResponse.getHyphenData().getList3();

        // 3 -> 1 -> 2 순서로 호출
        this.setList3ToHouseEntity(list3, houseList);
        this.setList1ToHouseEntity(list1, houseList);
        this.setList2ToHouseEntity(list2, houseList);

        /*if(this.isSuccessHyphenUserHouseResponse(hyphenCommon)){
            hyphenUserHouseResultInfoList = this.setResultDataToHyphenUserHouseResultInfo(hyphenUserHouseResponse);
        }else{
            throw new CustomException(ErrorCode.HOUSE_FAILED_HYPHEN_COMMON, "하이픈에서 보유주택정보 조회 중 오류가 발생했습니다.");
        }*/


        // 3. 조회된 결과를 정리하여 별도 Dto에 저장(HyphenUserHouseResultInfo)
        //List<HyphenUserHouseResultInfo> hyphenUserHouseResultInfoList = hyphenUserHouseService.setResultDataToHyphenUserHouseResultInfo(hyphenUserHouseResponse);
        //List<HyphenUserHouseResultInfo> hyphenUserHouseResultInfoList = this.setResultDataToHyphenUserHouseResultInfo(hyphenUserHouseResponse);

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

    private boolean isSuccessHyphenUserHouseResponse(HyphenCommon hyphenCommon){
        if(hyphenCommon != null){
            if(NO.equals(hyphenCommon.getErrYn())){
                return true;
            }else{
                log.error("하이픈 오류 발생 : " + hyphenCommon.getErrMsg());
                return false;
            }
        }else{
            return false;
        }
    }

    private void setList1ToHouseEntity(List<DataDetail1> list, List<House> houseList){

        List<HyphenUserHouseResultInfo> hyphenUserHouseResultInfoList = new ArrayList<>();

        if(list != null && !list.isEmpty()){
            for (DataDetail1 dataDetail1 : list) {
                HouseAddressDto houseAddressDto = houseAddressService.separateAddress(dataDetail1.getAddress());

                hyphenUserHouseResultInfoList.add(
                        HyphenUserHouseResultInfo.builder()
                                .resultListNo(ONE)
                                .tradeType(ZERO)
                                .orgAdr(houseAddressDto.getAddress())
                                .searchAdr(houseAddressDto.getSearchAddress())
                                .houseType(SIX)
                                .pubLandPrice(Long.parseLong(StringUtils.defaultString(dataDetail1.getPublishedPrice(), ZERO)))
                                .area(new BigDecimal(StringUtils.defaultString(dataDetail1.getArea(), DEFAULT_DECIMAL)))
                                .build());
            }
        }

        if(!hyphenUserHouseResultInfoList.isEmpty()){
            for (HyphenUserHouseResultInfo hyphenUserHouseResultInfo : hyphenUserHouseResultInfoList){
                JusoDetail jusoDetail = this.searchJusoDetail(hyphenUserHouseResultInfo);
                this.setHouseList(hyphenUserHouseResultInfo, jusoDetail, houseList);
            }
        }
    }

    private void setList2ToHouseEntity(List<DataDetail2> list, List<House> houseList){

        List<HyphenUserHouseResultInfo> tempHyphenUserHouseResultInfoList = new ArrayList<>();
        List<HyphenUserHouseResultInfo> hyphenUserHouseResultInfoList = new ArrayList<>();

        if(list != null && !list.isEmpty()){
            for (DataDetail2 dataDetail2 : list) {
                HouseAddressDto houseAddressDto = houseAddressService.separateAddress(dataDetail2.getAddress());

                String tradeType = this.getTradeTypeFromSellBuyClassification(StringUtils.defaultString(dataDetail2.getSellBuyClassification()));
                String houseType = this.getHouseTypeFromSellBuyClassification(StringUtils.defaultString(dataDetail2.getSellBuyClassification()));

                String buyPrice = ZERO;
                String sellPrice = ZERO;

                // 매수
                if(ONE.equals(tradeType)){
                    buyPrice = StringUtils.defaultString(dataDetail2.getTradingPrice(), ZERO);
                }
                // 매도
                else if(TWO.equals(tradeType)){
                    sellPrice = StringUtils.defaultString(dataDetail2.getTradingPrice(), ZERO);
                }

                tempHyphenUserHouseResultInfoList.add(
                        HyphenUserHouseResultInfo.builder()
                                .resultListNo(TWO)
                                .tradeType(tradeType)
                                .orgAdr(houseAddressDto.getAddress())
                                .searchAdr(houseAddressDto.getSearchAddress())
                                .houseType(houseType)
                                .contractDate(LocalDate.parse(dataDetail2.getContractDate(), DateTimeFormatter.ofPattern("yyyyMMdd")))
                                .balanceDate(LocalDate.parse(dataDetail2.getBalancePaymentDate(), DateTimeFormatter.ofPattern("yyyyMMdd")))
                                .buyPrice(Long.parseLong(buyPrice))
                                .sellPrice(Long.parseLong(sellPrice))
                                .area(new BigDecimal(StringUtils.defaultString(dataDetail2.getArea(), DEFAULT_DECIMAL)))
                                .build());
            }
            
            // 거래내역 주택 필터링 작업

            // hyphenUserHouseResultInfoList에 필터링 결과 세팅

            // 도로명주소 검색 API 호출(주소기반산업지원서비스)
            
            // House Entity에 데이터 세팅
        }
    }

    private void setList3ToHouseEntity(List<DataDetail3> list, List<House> houseList){

        List<HyphenUserHouseResultInfo> hyphenUserHouseResultInfoList = new ArrayList<>();

        if(list != null && !list.isEmpty()){
            for (DataDetail3 dataDetail3 : list) {
                HouseAddressDto houseAddressDto = houseAddressService.separateAddress(dataDetail3.getAddress());

                hyphenUserHouseResultInfoList.add(
                        HyphenUserHouseResultInfo.builder()
                                .resultListNo(THREE)
                                .tradeType(ZERO)
                                .orgAdr(houseAddressDto.getAddress())
                                .searchAdr(houseAddressDto.getSearchAddress())
                                .detailAdr(houseAddressDto.getDetailAddress())
                                .houseType(SIX)
                                .buyDate(LocalDate.parse(dataDetail3.getAcquisitionDate(), DateTimeFormatter.ofPattern("yyyyMMdd")))
                                .area(new BigDecimal(StringUtils.defaultString(dataDetail3.getArea(), DEFAULT_DECIMAL)))
                                .build());
            }
        }

        if(!hyphenUserHouseResultInfoList.isEmpty()){
            for (HyphenUserHouseResultInfo hyphenUserHouseResultInfo : hyphenUserHouseResultInfoList){
                JusoDetail jusoDetail = this.searchJusoDetail(hyphenUserHouseResultInfo);
                this.setHouseList(hyphenUserHouseResultInfo, jusoDetail, houseList);
            }
        }

        /*if(!hyphenUserHouseResultInfoList.isEmpty()){
            for (HyphenUserHouseResultInfo hyphenUserHouseResultInfo : hyphenUserHouseResultInfoList){

                if(hyphenUserHouseResultInfo.getSearchAdr() != null){
                    // 주소기반산업지원서비스 도로명주소 검색은 한 건당 최대 (3)회 까지로 제한
                    for(int i=0; i<MAX_JUSO_CALL_CNT; i++){
                        if(hyphenUserHouseResultInfo.getSearchAdr().get(i) != null){
                            StringBuilder searchAddr = new StringBuilder(EMPTY);
                            if(!EMPTY.contentEquals(searchAddr)){
                                searchAddr.append(SPACE);
                            }
                            
                            // hyphenUserHouseResultInfo에서 검색주소 추출(조회 결과가 2건 이상인 경우 파라미터 하나씩 추가하여 재조회)
                            searchAddr.append(hyphenUserHouseResultInfo.getSearchAdr().get(i));

                            // 도로명주소 검색 API 호출(주소기반산업지원서비스)
                            JusoGovRoadAdrResponse jusoGovRoadAdrResponse = jusoGovService.getRoadAdrInfo(searchAddr.toString());

                            if(jusoGovRoadAdrResponse != null && jusoGovRoadAdrResponse.getResults() != null && jusoGovRoadAdrResponse.getResults().getCommon() != null){
                                String totalCount = jusoGovRoadAdrResponse.getResults().getCommon().getTotalCount();
                                String errorCode = jusoGovRoadAdrResponse.getResults().getCommon().getErrorCode();
                                String errorMessage = jusoGovRoadAdrResponse.getResults().getCommon().getErrorMessage();

                                // 정상
                                if(ZERO.equals(errorCode)){
                                    try{
                                        int ttcn = Integer.parseInt(totalCount);

                                        // 검색 결과가 1건인 경우 : List<House>에 데이터 세팅
                                        if(ttcn == 1){
                                            this.setHouseList(hyphenUserHouseResultInfo, jusoGovRoadAdrResponse.getResults().getJuso().get(0), houseList);
                                            break;
                                        }
                                        // 검색 결과가 없는 경우 : PASS
                                        else if(ttcn == 0){
                                            log.info(searchAddr + "주소의 검색 결과가 없습니다.(주소기반산업지원서비스)");
                                            break;
                                        }
                                    }catch(NumberFormatException e){
                                        // TODO. 오류 처리 로직 추가
                                        log.error("검색 결과 Count 형변환(Integer) 실패");
                                    }
                                }
                                // 오류
                                else{
                                    // TODO. 오류 처리 로직 추가
                                    log.error("도로명주소 검색 중 오류 발생\n" + errorCode + " : " + errorMessage);
                                }
                            }
                            else{
                                // TODO. 오류 처리 로직 추가
                                log.error("도로명주소 검색 API 응답 없음");
                            }
                        }
                    }
                }

            }
        }*/
    }

    private void filteringTradeHouseList(List<HyphenUserHouseResultInfo> list){
        List<HyphenUserHouseResultInfo> filterList = new ArrayList<>();
        List<HyphenUserHouseResultInfo> removeQueueList = new ArrayList<>();
        List<HyphenUserHouseResultInfo> doubleBuyList = new ArrayList<>();
        List<HyphenUserHouseResultInfo> resultList = new ArrayList<>();

        ArrayList<String> removeAddressList = new ArrayList<>();

        if(list != null){
            for(HyphenUserHouseResultInfo hyphenUserHouseResultInfo : list){
                filterList.add(hyphenUserHouseResultInfo.clone());
            }
        }

        /*
          1. 동일한 주소의 매수 건이 2개 이상 존재하는지 체크(거래유형이 '매수'인 건끼리 비교)
           1) 동일한 주소의 매수 건이 2개 이상 존재하면 doubleBuyList, removeQueueList 에 추가(doubleBuyList에는 clone 추가)
           2) 반복부 1 roop가 완료되면, removeQueueList에 있는 객체 remove
           3) 반복부 전체 roop가 완료되면, doubleBuyList에 있는 객체를 resultList에 복사
         */
        if(!filterList.isEmpty()){
            for(int i=0; i<filterList.size(); i++){
                // 거래유형 : 매수
                if(ONE.equals(filterList.get(i).getTradeType())){
                    String address = StringUtils.defaultString(filterList.get(i).getOrgAdr());
                    boolean isAdd = false;

                    if(removeAddressList.contains(address)) continue;

                    for(int j=i+1; j<filterList.size(); j++){
                        if(ONE.equals(filterList.get(j).getTradeType())){
                            // resultList에 추가
                            if(address.equals(filterList.get(j).getOrgAdr())){
                                if(!isAdd){
                                    doubleBuyList.add(filterList.get(i).clone());
                                    removeAddressList.add(filterList.get(i).getOrgAdr());
                                    isAdd = true;
                                }
                                doubleBuyList.add(filterList.get(j).clone());
                            }
                        }
                    }
                }
            }

            for(String compareAddress : removeAddressList){
                filterList.removeIf(filterInfo -> ONE.equals(filterInfo.getTradeType()) && compareAddress.equals(filterInfo.getOrgAdr()));
            }

        }


        /*
          1. 동일한 주소의 매수, 매도 건이 쌍으로 존재하는지 체크(거래유형이 '매수'인 건을 기준으로 '매도'인 건들과 비교)
           1) '매수'건 기준으로 동일한 주소의 '매도'건이 존재하면 removeQueueList 에 추가
           2) 반복부 1 roop가 완료되면, removeQueueList에 있는 객체 remove
           3) 반복부 전체 roop가 완료되면, doubleBuyList에 있는 객체를 resultList에 복사
         */

        if(!filterList.isEmpty()){
            for(int i=0; i<filterList.size(); i++){
                // 거래유형 : 매수
                if(ONE.equals(filterList.get(i).getTradeType())){
                    String address = StringUtils.defaultString(filterList.get(i).getOrgAdr());

                    for(int j=i+1; j<filterList.size(); j++){
                        if(ONE.equals(filterList.get(j).getTradeType())){
                            // resultList에 추가
                            if(address.equals(filterList.get(j).getOrgAdr())){
                                resultList.add(filterList.get(j).clone());
                            }
                        }
                    }
                }
            }

            // resultList에 추가된 데이터가 있다면 동일한 주소가 2개 이상 존재하는 경우이므로, 해당 주소와 동일한 데이터는 filterList에서 삭제
            /*if(!resultList.isEmpty()){
                for(HyphenUserHouseResultInfo resultInfo : resultList){
                    String compareAddress = StringUtils.defaultString(resultInfo.getOrgAdr());
                    filterList.removeIf(filterInfo -> ONE.equals(filterInfo.getTradeType()) && compareAddress.equals(filterInfo.getOrgAdr()));
                }
            }*/
        }

        if(!filterList.isEmpty()){
            List<HyphenUserHouseResultInfo> removeWaitingList = new ArrayList<>();


        }

    }

    // (매도/매수구분에서) 거래유형 가져오기
    private String getTradeTypeFromSellBuyClassification(String sellBuyClassification){
        String tradeType = ZERO;

        if(sellBuyClassification.contains("매수")){
            tradeType = ONE;
        }else if(sellBuyClassification.contains("매도")){
            tradeType = TWO;
        }

        return tradeType;
    }

    // (매도/매수구분에서) 주택유형 가져오기
    private String getHouseTypeFromSellBuyClassification(String sellBuyClassification){
        String houseType = SIX;

        if(sellBuyClassification.contains("분양권")){
            houseType = FIVE;
        }

        return houseType;
    }

    // (주소기반산업지원서비스 - 도로명주소 조회) 상세 주소정보 조회
    private JusoDetail searchJusoDetail(HyphenUserHouseResultInfo hyphenUserHouseResultInfo){
        JusoDetail jusoDetail = null;

        if(hyphenUserHouseResultInfo.getSearchAdr() != null){
            // 주소기반산업지원서비스 도로명주소 검색은 한 건당 최대 (3)회 까지로 제한
            for(int i=0; i<MAX_JUSO_CALL_CNT; i++){
                if(hyphenUserHouseResultInfo.getSearchAdr().get(i) != null){
                    StringBuilder searchAddr = new StringBuilder(EMPTY);
                    if(!EMPTY.contentEquals(searchAddr)){
                        searchAddr.append(SPACE);
                    }

                    // hyphenUserHouseResultInfo에서 검색주소 추출(조회 결과가 2건 이상인 경우 파라미터 하나씩 추가하여 재조회)
                    searchAddr.append(hyphenUserHouseResultInfo.getSearchAdr().get(i));

                    // 도로명주소 검색 API 호출(주소기반산업지원서비스)
                    JusoGovRoadAdrResponse jusoGovRoadAdrResponse = jusoGovService.getRoadAdrInfo(searchAddr.toString());

                    if(jusoGovRoadAdrResponse != null && jusoGovRoadAdrResponse.getResults() != null && jusoGovRoadAdrResponse.getResults().getCommon() != null){
                        String totalCount = jusoGovRoadAdrResponse.getResults().getCommon().getTotalCount();
                        String errorCode = jusoGovRoadAdrResponse.getResults().getCommon().getErrorCode();
                        String errorMessage = jusoGovRoadAdrResponse.getResults().getCommon().getErrorMessage();

                        // 정상
                        if(ZERO.equals(errorCode)){
                            try{
                                int ttcn = Integer.parseInt(totalCount);

                                // 검색 결과가 1건인 경우 : List<House>에 데이터 세팅
                                if(ttcn == 1){
                                    //this.setHouseList(hyphenUserHouseResultInfo, jusoGovRoadAdrResponse.getResults().getJuso().get(0), houseList);
                                    jusoDetail = jusoGovRoadAdrResponse.getResults().getJuso().get(0);
                                    break;
                                }
                                // 검색 결과가 없는 경우 : PASS
                                else if(ttcn == 0){
                                    log.info(searchAddr + "주소의 검색 결과가 없습니다.(주소기반산업지원서비스)");
                                    break;
                                }
                            }catch(NumberFormatException e){
                                // TODO. 오류 처리 로직 추가
                                log.error("검색 결과 Count 형변환(Integer) 실패");
                            }
                        }
                        // 오류
                        else{
                            // TODO. 오류 처리 로직 추가
                            log.error("도로명주소 검색 중 오류 발생\n" + errorCode + " : " + errorMessage);
                        }
                    }
                    else{
                        // TODO. 오류 처리 로직 추가
                        log.error("도로명주소 검색 API 응답 없음");
                    }
                }
            }
        }

        return jusoDetail;
    }

    // House Entity List 세팅
    private void setHouseList(HyphenUserHouseResultInfo hyphenUserHouseResultInfo, JusoDetail jusoDetail, List<House> houseList){
        if(hyphenUserHouseResultInfo != null && jusoDetail != null && houseList != null){
            // houseList에 동일한 건물관리번호를 가진 House가 존재하는지 체크하여 존재하면 update, 존재하지 않으면 create
            String bdMgtSn = jusoDetail.getBdMgtSn();
            String resultListNo = hyphenUserHouseResultInfo.getResultListNo();
            boolean isUpdate = false;

            for (House house : houseList){
                // update(동일한 건물관리번호를 가진 House 존재)
                if(bdMgtSn != null && bdMgtSn.equals(house.getBdMgtSn())){
                    // List1 : 건축물대장정보
                    if(ONE.equals(resultListNo)){
                        if(house.getPubLandPrice() == null || house.getPubLandPrice() == 0) house.setPubLandPrice(hyphenUserHouseResultInfo.getPubLandPrice());
                        if(house.getArea() == null) house.setArea(hyphenUserHouseResultInfo.getArea());
                    }
                    // List2 : 부동산거래내역
                    else if(TWO.equals(resultListNo)){
                        // TODO. 작성예정
                    }
                    // List3 : 재산세정보
                    else if(THREE.equals(resultListNo)){
                        if(house.getDetailAdr().isBlank()) house.setDetailAdr(hyphenUserHouseResultInfo.getDetailAdr());
                        if(house.getBuyDate() == null) house.setBuyDate(hyphenUserHouseResultInfo.getBuyDate());
                        if(house.getArea() == null) house.setArea(hyphenUserHouseResultInfo.getArea());
                    }

                    // 기존 데이터의 주택유형이 '6'(주택)인 경우에는 업데이트
                    if(house.getHouseType().isBlank() || (SIX.equals(house.getHouseType()) && !SIX.equals(hyphenUserHouseResultInfo.getHouseType()))){
                        house.setHouseType(hyphenUserHouseResultInfo.getHouseType());
                    }

                    if(house.getHouseName().isBlank()) house.setHouseName(jusoDetail.getBdNm());
                    if(house.getJibunAddr().isBlank()) house.setJibunAddr(jusoDetail.getJibunAddr());
                    if(house.getRoadAddr().isBlank()) house.setRoadAddr(jusoDetail.getRoadAddrPart1());
                    if(house.getRoadAddrRef().isBlank()) house.setRoadAddrRef(jusoDetail.getRoadAddrPart2());
                    if(house.getBdMgtSn().isBlank()) house.setBdMgtSn(jusoDetail.getBdMgtSn());
                    if(house.getAdmCd().isBlank()) house.setAdmCd(jusoDetail.getAdmCd());
                    if(house.getRnMgtSn().isBlank()) house.setRnMgtSn(jusoDetail.getRnMgtSn());

                    house.setDestruction(false);
                    house.setOwnerCnt(1);
                    house.setUser_proportion(100);
                    house.setMoveInRight(false);
                    house.setSourceType(ONE);

                    isUpdate = true;
                    log.info("House Update : " + jusoDetail.getBdMgtSn() + " : " + jusoDetail.getBdNm());
                    break;
                }
            }

            // create(동일한 건물관리번호를 가진 House가 존재하지 않음)
            if(!isUpdate){
                if(ONE.equals(resultListNo)){
                    houseList.add(
                            House.builder()
                                    .houseType(hyphenUserHouseResultInfo.getHouseType())
                                    .houseName(jusoDetail.getBdNm())
                                    .pubLandPrice(hyphenUserHouseResultInfo.getPubLandPrice())
                                    .jibunAddr(jusoDetail.getJibunAddr())
                                    .roadAddr(jusoDetail.getRoadAddrPart1())
                                    .roadAddrRef(jusoDetail.getRoadAddrPart2())
                                    .bdMgtSn(jusoDetail.getBdMgtSn())
                                    .admCd(jusoDetail.getAdmCd())
                                    .rnMgtSn(jusoDetail.getRnMgtSn())
                                    .area(hyphenUserHouseResultInfo.getArea())
                                    .isDestruction(false)
                                    .ownerCnt(1)
                                    .user_proportion(100)
                                    .isMoveInRight(false)
                                    .sourceType(ONE)
                                    .build());
                }else if(TWO.equals(resultListNo)){

                }else if(THREE.equals(resultListNo)){
                    houseList.add(
                            House.builder()
                                    .houseType(hyphenUserHouseResultInfo.getHouseType())
                                    .houseName(jusoDetail.getBdNm())
                                    .detailAdr(hyphenUserHouseResultInfo.getDetailAdr())
                                    .buyDate(hyphenUserHouseResultInfo.getBuyDate())
                                    .jibunAddr(jusoDetail.getJibunAddr())
                                    .roadAddr(jusoDetail.getRoadAddrPart1())
                                    .roadAddrRef(jusoDetail.getRoadAddrPart2())
                                    .bdMgtSn(jusoDetail.getBdMgtSn())
                                    .admCd(jusoDetail.getAdmCd())
                                    .rnMgtSn(jusoDetail.getRnMgtSn())
                                    .area(hyphenUserHouseResultInfo.getArea())
                                    .isDestruction(false)
                                    .ownerCnt(1)
                                    .user_proportion(100)
                                    .isMoveInRight(false)
                                    .sourceType(ONE)
                                    .build());
                }

                log.info("House Create : " + jusoDetail.getBdMgtSn() + " : " + jusoDetail.getBdNm());
            }
        }else{
            // TODO. 오류 처리 로직 추가
            log.error("House 정보 세팅 실패하였습니다.");
        }
    }

    // 미사용 메소드
    /*private List<HyphenUserHouseResultInfo> setResultDataToHyphenUserHouseResultInfo(HyphenUserHouseResponse responseData){

        List<HyphenUserHouseResultInfo> houseList = new ArrayList<HyphenUserHouseResultInfo>();

        if(responseData != null && responseData.getHyphenData() != null){
            List<DataDetail1> list1 = responseData.getHyphenData().getList1();
            List<DataDetail2> list2 = responseData.getHyphenData().getList2();
            List<DataDetail3> list3 = responseData.getHyphenData().getList3();

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
                for(HyphenUserHouseResponse.HyphenData.DataDetail2 dataDetail2 : list2){
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
                for(HyphenUserHouseResponse.HyphenData.DataDetail3 dataDetail3 : list3){
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
    }*/
}
