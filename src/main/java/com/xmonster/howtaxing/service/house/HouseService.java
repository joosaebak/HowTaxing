package com.xmonster.howtaxing.service.house;

import com.xmonster.howtaxing.dto.house.HouseDetailResponse;
import com.xmonster.howtaxing.dto.house.HouseListResponse;
import com.xmonster.howtaxing.dto.tax.CalculateBuyTaxResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class HouseService {

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
