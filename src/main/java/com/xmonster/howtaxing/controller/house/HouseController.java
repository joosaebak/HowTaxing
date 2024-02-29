package com.xmonster.howtaxing.controller.house;

import com.xmonster.howtaxing.CustomException;
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

import com.xmonster.howtaxing.type.ErrorCode;
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
    public Object getHouseList(@RequestBody Map<String, Object> requestMap){

        return houseService.getHouseList(requestMap);
        //return houseService.getUserHouseList(requestMap);
    }

    // 주택 상세정보 조회
    @GetMapping("/house/detail")
    public Map<String, Object> getHouseDetail(String houseId){
        return houseService.getHouseDetail(houseId);
    }
}
