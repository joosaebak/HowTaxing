package com.xmonster.howtaxing.dto.house;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class HouseListResponse {

    Map<String, Object> data;

    public HouseListResponse() {
        data = new HashMap<String, Object>();
        List<HouseInfoResponse> list = new ArrayList<HouseInfoResponse>();

        HouseInfoResponse response1 = new HouseInfoResponse("1", "1", "1", "반포래미안원베일리아파트", "118동 1403호");
        HouseInfoResponse response2 = new HouseInfoResponse("2", "1", "2", "당동다가구주택", "당동 878-3");
        HouseInfoResponse response3 = new HouseInfoResponse("3", "1", "4", "대경빌라", "101호");

        list.add(response1);
        list.add(response2);
        list.add(response3);

        data.put("list", list);
        data.put("listCnt", list.size());
    }

    public Map<String, Object> getData() {
        return data;
    }
}
