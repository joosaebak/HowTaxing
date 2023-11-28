package com.xmonster.howtaxing.dto.house;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HouseListResponse {

    Map<String, Object> data;

    public HouseListResponse() {
        data = new HashMap<String, Object>();
        List<HouseInfoResponse> list = new ArrayList<HouseInfoResponse>();

        HouseInfoResponse response1 = new HouseInfoResponse("27", "382", "아파트", "반포래미안원베일리아파트", "118동 1403호");
        HouseInfoResponse response2 = new HouseInfoResponse("88", "382", "아파트", "인덕원퍼스비엘아파트", "108동 1201호");
        HouseInfoResponse response3 = new HouseInfoResponse("25", "382", "아파트", "반포센트럴자이아파트", "105동 1701호");

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
