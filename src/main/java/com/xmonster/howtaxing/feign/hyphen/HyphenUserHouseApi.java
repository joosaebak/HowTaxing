package com.xmonster.howtaxing.feign.hyphen;

import com.xmonster.howtaxing.config.FeignConfiguration;
import com.xmonster.howtaxing.dto.hyphen.HyphenRequestUserHouseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(value = "hyphenUserHouse", url="https://api.hyphen.im", configuration = {FeignConfiguration.class})
public interface HyphenUserHouseApi {
    @PostMapping("/in0148001055")
    ResponseEntity<String> getUserHouseInfo(@RequestHeader Map<String, Object> requestHeader, @RequestBody HyphenRequestUserHouseDto requestBody);
}
