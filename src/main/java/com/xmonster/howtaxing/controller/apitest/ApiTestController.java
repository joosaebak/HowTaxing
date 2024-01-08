package com.xmonster.howtaxing.controller.apitest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ApiTestController {

    @GetMapping("/test/getAddrApi")
    public Map<String, Object> getAddrApi(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Map<String, Object> resultMap = new HashMap<String, Object>();

        // 요청변수 설정
        String admCd = "4117310400";
        String rnMgtSn = "411733183001";
        String udrtYn = "0";
        String buldMnnm = "15";
        String buldSlno = "0";
        String confmKey = "devU01TX0FVVEgyMDI0MDEwMTAzNDMwOTExNDM5MzU=";
        String resultType = "json";

        // API 호출 URL 정보 설정
        String apiUrl = "https://business.juso.go.kr/addrlink/addrCoordApi.do?admCd="
                +admCd+"&rnMgtSn="+rnMgtSn+"&udrtYn="+udrtYn+"&buldMnnm="+buldMnnm
                +"&buldSlno="+buldSlno+"&confmKey="+confmKey+"&resultType="+resultType;

        URL url = new URL(apiUrl);
        BufferedReader br = new BufferedReader(
                            new InputStreamReader(
                            url.openStream(),"UTF-8"));

        StringBuffer sb = new StringBuffer();
        String tempStr = null;
        while(true){
            tempStr = br.readLine();
            if(tempStr == null) break;
            sb.append(tempStr); // 응답결과 JSON 저장
        }

        br.close();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/xml");
        response.getWriter().write(sb.toString()); // 응답결과 반환

        System.out.println("sb.toString() : " + sb.toString());
        System.out.println("response : " + response);

        resultMap.put("response", response);

        return resultMap;
    }
}
