package com.xmonster.howtaxing.dto.house;

public class HouseDetailResponse {

    private final String houseId;
    private final String houseType;
    private final String houseName;
    private final String houseDetailName;
    private final String legalDstCode;
    private final String roadnmAdr;
    private final String detailAdr;
    private final String dong;
    private final String hosu;
    private final String pubLandPrice;
    private final String kbMktPrice;
    private final String areaMeter;
    private final String areaPyung;
    private final String ownerCnt;
    private final String userProportion;
    private final String owner1Proportion;
    private final String isMovingInRight;

    public HouseDetailResponse(){
        this.houseId = "";
        this.houseType = "";
        this.houseName = "";
        this.houseDetailName = "";
        this.legalDstCode = "";
        this.roadnmAdr = "";
        this.detailAdr = "";
        this.dong = "";
        this.hosu = "";
        this.pubLandPrice = "";
        this.kbMktPrice = "";
        this.areaMeter = "";
        this.areaPyung = "";
        this.ownerCnt = "";
        this.userProportion = "";
        this.owner1Proportion = "";
        this.isMovingInRight = "";
    }

    // Test Constructor(GGMANYAR)
    public HouseDetailResponse(String houseId){
        this.houseId = houseId;
        this.houseType = "1";
        this.houseName = "반포센트럴자이아파트";
        this.houseDetailName = "105동 1701호";
        this.legalDstCode = "1165010600";
        this.roadnmAdr = "서울특별시 서초구 반포대로 310-6";
        this.detailAdr = "반포센트럴자이아파트";
        this.dong = "101";
        this.hosu = "501";
        this.pubLandPrice = "2800000000";
        this.kbMktPrice = "2700000000";
        this.areaMeter = "84.893";
        this.areaPyung = "33";
        this.ownerCnt = "2";
        this.userProportion = "50";
        this.owner1Proportion = "50";
        this.isMovingInRight = "false";
    }

    public HouseDetailResponse(String houseId, String houseType, String houseName, String houseDetailName, String legalDstCode,String roadnmAdr, String detailAdr, String dong, String hosu, String pubLandPrice, String kbMktPrice, String areaMeter, String areaPyung, String ownerCnt, String userProportion, String owner1Proportion, String isMovingInRight) {
        this.houseId = houseId;
        this.houseType = houseType;
        this.houseName = houseName;
        this.houseDetailName = houseDetailName;
        this.legalDstCode = legalDstCode;
        this.roadnmAdr = roadnmAdr;
        this.detailAdr = detailAdr;
        this.dong = dong;
        this.hosu = hosu;
        this.pubLandPrice = pubLandPrice;
        this.kbMktPrice = kbMktPrice;
        this.areaMeter = areaMeter;
        this.areaPyung = areaPyung;
        this.ownerCnt = ownerCnt;
        this.userProportion = userProportion;
        this.owner1Proportion = owner1Proportion;
        this.isMovingInRight = isMovingInRight;
    }

    public String getHouseId() {
        return houseId;
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

    public String getLegalDstCode() {
        return legalDstCode;
    }

    public String getRoadnmAdr() {
        return roadnmAdr;
    }

    public String getDetailAdr() {
        return detailAdr;
    }

    public String getDong() {
        return dong;
    }

    public String getHosu() {
        return hosu;
    }

    public String getPubLandPrice() {
        return pubLandPrice;
    }

    public String getKbMktPrice() {
        return kbMktPrice;
    }

    public String getAreaMeter() {
        return areaMeter;
    }

    public String getAreaPyung() {
        return areaPyung;
    }

    public String getOwnerCnt() {
        return ownerCnt;
    }

    public String getUserProportion() {
        return userProportion;
    }

    public String getOwner1Proportion() {
        return owner1Proportion;
    }

    public String getIsMovingInRight() {
        return isMovingInRight;
    }
}
