package com.xmonster.howtaxing.feign.jusogov;

import com.xmonster.howtaxing.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jusoGovRoadAdr2", url="https://business.juso.go.kr", configuration = {FeignConfiguration.class})
public interface JusoGovRoadAdrApi {
    @GetMapping("/addrlink/addrLinkApi.do")
    ResponseEntity<String> getRoadAdrInfo(
            @RequestParam("confmKey") String confmKey,
            @RequestParam("currentPage") String currentPage,
            @RequestParam("countPerPage") String countPerPage,
            @RequestParam("keyword") String keyword,
            @RequestParam("resultType") String resultType,
            @RequestParam("hstryYn") String hstryYn,
            @RequestParam("firstSort") String firstSort,
            @RequestParam("addInfoYn") String addInfoYn
    );
}
