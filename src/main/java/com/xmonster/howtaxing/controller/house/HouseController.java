package com.xmonster.howtaxing.controller.house;

import com.xmonster.howtaxing.dto.house.*;
import com.xmonster.howtaxing.service.house.HouseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HouseController {
    private final HouseService houseService;

    // 보유주택 조회(하이픈-청약홈-주택소유확인)
    @PostMapping("/house/search")
    public Object getHouseListSearch(Authentication authentication, @RequestBody HouseListSearchRequest houseListSearchRequest) throws Exception {
        return houseService.getHouseListSearch(authentication, houseListSearchRequest);
    }

    // 보유주택 목록 조회
    @GetMapping("/house/list")
    public Object getHouseList(Authentication authentication) throws Exception {
        return houseService.getHouseList(authentication);
    }

    // 보유주택 상세 조회
    @GetMapping("/house/detail")
    public Object getHouseDetail(@RequestParam String houseId)  throws Exception {
        return houseService.getHouseDetail(houseId);
    }

    // 보유주택 (직접)등록
    @PostMapping("/house/regist")
    public Object registHouseInfo(Authentication authentication, @RequestBody HouseRegistRequest houseRegistRequest) throws Exception {
        return houseService.registHouseInfo(authentication, houseRegistRequest);
    }

    // 보유주택 수정
    @PutMapping("/house/modify")
    public Object modifyHouseInfo(@RequestBody HouseModifyRequest houseModifyRequest) throws Exception {
        return houseService.modifyHouseInfo(houseModifyRequest);
    }

    // 보유주택 삭제
    @DeleteMapping("/house/delete")
    public Object deleteHouse(Authentication authentication, @RequestBody HouseListDeleteRequest houseListDeleteRequest) throws Exception {
        return houseService.deleteHouse(authentication, houseListDeleteRequest);
    }

    // 보유주택 전체 삭제
    @DeleteMapping("/house/deleteAll")
    public Object deleteHouseAll(Authentication authentication) throws Exception {
        return houseService.deleteHouseAll(authentication);
    }

    // TODO. 거주기간 조회(하이픈-정부24-주민등록초본)
    /*@PostMapping("/house/stayPeriod")
    public Object searchHouseStayPeriod(Authentication authentication, @RequestBody HouseStayPeriodRequest houseStayPeriodRequest){
        return houseService.getHouseStayPeriod(authentication, houseStayPeriodRequest);
    }*/

}
