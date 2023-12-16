package com.xmonster.howtaxing.dto.house;

public class HouseInfoResponse {

    private final String houseId;
    private final String userId;
    private final String houseType;
    private final String houseName;
    private final String houseDetailName;

    public HouseInfoResponse(String houseId, String userId, String houseType, String houseName, String houseDetailName) {
        this.houseId = houseId;
        this.userId = userId;
        this.houseType = houseType;
        this.houseName = houseName;
        this.houseDetailName = houseDetailName;
    }

    public String getHouseId() {
        return houseId;
    }

    public String getUserId() {
        return userId;
    }

    public String getHouseType() {
        return houseType;
    }

    public String getHouseName() {
        return houseName;
    }

    public String getHouseDetailName() {
        return houseDetailName;
    }
}
