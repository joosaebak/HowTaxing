package com.xmonster.howtaxing.controller.house;

import com.xmonster.howtaxing.dto.house.*;
import com.xmonster.howtaxing.service.house.HouseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HouseController {
    private final HouseService houseService;

    // 보유주택 조회(하이픈-청약홈-주택소유확인)
    @PostMapping("/house/search")
    public Object getHouseListSearch(@RequestBody HouseListSearchRequest houseListSearchRequest) throws Exception {
        log.info(">> [Controller]HouseController getHouseListSearch - 보유주택 조회(하이픈-청약홈-주택소유확인)");
        return houseService.getHouseListSearch(houseListSearchRequest);
    }

    // 보유주택 조회(하이픈-청약홈-주택소유확인) 테스트
    @PostMapping("/house/searchTest")
    public Object getHouseListSearchTest(@RequestBody HouseListSearchRequest houseListSearchRequest) throws Exception {
        log.info(">> [Controller]HouseController getHouseListSearchTest - 보유주택 조회(하이픈-청약홈-주택소유확인) 테스트");
        return houseService.getHouseListSearchTest(houseListSearchRequest);
    }

    // 보유주택 목록 조회
    @GetMapping("/house/list")
    public Object getHouseList() throws Exception {
        log.info(">> [Controller]HouseController getHouseList - 보유주택 목록 조회");
        return houseService.getHouseList();
    }

    // 보유주택 상세 조회
    @GetMapping("/house/detail")
    public Object getHouseDetail(@RequestParam Long houseId)  throws Exception {
        log.info(">> [Controller]HouseController getHouseDetail - 보유주택 상세 조회");
        return houseService.getHouseDetail(houseId);
    }

    // 보유주택 (직접)등록
    @PostMapping("/house/regist")
    public Object registHouseInfo(@RequestBody HouseRegistRequest houseRegistRequest) throws Exception {
        log.info(">> [Controller]HouseController registHouseInfo - 보유주택 (직접)등록");
        return houseService.registHouseInfo(houseRegistRequest);
    }

    // 보유주택 (정보)수정
    @PutMapping("/house/modify")
    public Object modifyHouseInfo(@RequestBody HouseModifyRequest houseModifyRequest) throws Exception {
        log.info(">> [Controller]HouseController modifyHouseInfo - 보유주택 (정보)수정");
        return houseService.modifyHouseInfo(houseModifyRequest);
    }

    // 보유주택 삭제
    @DeleteMapping("/house/delete")
    public Object deleteHouse(@RequestBody HouseListDeleteRequest houseListDeleteRequest) throws Exception {
        log.info(">> [Controller]HouseController deleteHouse - 보유주택 삭제");
        return houseService.deleteHouse(houseListDeleteRequest);
    }

    // 보유주택 전체 삭제
    @DeleteMapping("/house/deleteAll")
    public Object deleteHouseAll() throws Exception {
        log.info(">> [Controller]HouseController deleteHouseAll - 보유주택 전체 삭제");
        return houseService.deleteHouseAll();
    }

    // TODO. 거주기간 조회(하이픈-정부24-주민등록초본)
    /*@PostMapping("/house/stayPeriod")
    public Object searchHouseStayPeriod(@RequestBody HouseStayPeriodRequest houseStayPeriodRequest){
        return houseService.getHouseStayPeriod(houseStayPeriodRequest);
    }*/

}
