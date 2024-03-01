package com.xmonster.howtaxing.controller.house;

import com.xmonster.howtaxing.dto.house.HouseListDeleteRequest;
import com.xmonster.howtaxing.dto.house.HouseListSearchRequest;
import com.xmonster.howtaxing.service.house.HouseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequiredArgsConstructor
@Slf4j
public class HouseController {

    private final HouseService houseService;

    // 보유주택 목록 조회
    @PostMapping("/house/list")
    public Object getHouseList(Authentication authentication, @RequestBody HouseListSearchRequest houseListSearchRequest) throws Exception {
        return houseService.getUserHouseList(authentication, houseListSearchRequest);
    }

    // 보유주택 상세 조회
    @GetMapping("/house/detail")
    public Map<String, Object> getHouseDetail(String houseId)  throws Exception {
        return houseService.getHouseDetail(houseId);
    }

    @DeleteMapping("/house/delete")
    public Object deleteHouse(Authentication authentication, @RequestBody HouseListDeleteRequest houseListDeleteRequest) throws Exception {
        return houseService.deleteHouse(authentication, houseListDeleteRequest);
    }

    // 보유주택 전체 삭제
    @DeleteMapping("/house/deleteAll")
    public Object deleteHouseAll(Authentication authentication) throws Exception {
        return houseService.deleteHouseAll(authentication);
    }
}
