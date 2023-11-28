package com.xmonster.howtaxing.service.tax;


import com.xmonster.howtaxing.dto.tax.CalculateBuyTaxResponse;
import com.xmonster.howtaxing.dto.tax.CalculateSellTaxResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CalculateTaxService {

    public Map<String, Object> calculateBuyTax(Map<String, Object> requestMap){

        Map<String, Object> resultMap = new HashMap<String, Object>();
        CalculateBuyTaxResponse response = null;

        if(requestMap != null && !requestMap.isEmpty()){
            response = new CalculateBuyTaxResponse();
        }

        if(response != null){
            resultMap.put("isError", "false");
            resultMap.put("data", response);
        }else{
            resultMap.put("isError", "true");
            resultMap.put("errCode", "2001");
            resultMap.put("errMsg", "취등록세 계산 중 오류가 발생했습니다.");
        }

        return resultMap;
    }

    public Map<String, Object> calculateSellTax(Map<String, Object> requestMap){

        Map<String, Object> resultMap = new HashMap<String, Object>();
        CalculateSellTaxResponse response = null;

        if(requestMap != null && !requestMap.isEmpty()){
            response = new CalculateSellTaxResponse();
        }

        if(response != null){
            resultMap.put("isError", "false");
            resultMap.put("data", response);
        }else{
            resultMap.put("isError", "true");
            resultMap.put("errCode", "3001");
            resultMap.put("errMsg", "양도소득세 계산 중 오류가 발생했습니다.");
        }

        return resultMap;
    }
}
