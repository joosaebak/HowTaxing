package com.xmonster.howtaxing.controller.house;

import com.xmonster.howtaxing.service.house.HouseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HouseController {

    private final HouseService houseService;

    public HouseController(HouseService houseService){
        this.houseService = houseService;
    }

    @PostMapping("/house/list")
    public Map<String, Object> getHouseList(@RequestBody Map<String, Object> requestMap){
        return houseService.getHouseList(requestMap);
    }

    // 주택 상세정보 조회
    @GetMapping("/house/detail")
    public Map<String, Object> getHouseDetail(String houseId){
        return houseService.getHouseDetail(houseId);
    }
}
