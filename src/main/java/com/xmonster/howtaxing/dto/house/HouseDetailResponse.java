package com.xmonster.howtaxing.dto.house;

import lombok.Getter;

@Getter
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

    private final String ONE = "1";
    private final String TWO = "2";
    private final String THREE = "3";

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
        if(ONE.equals(this.houseId)){
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
        }else if(TWO.equals(this.houseId)){
            this.houseType = "2";
            this.houseName = "당동다가구주택";
            this.houseDetailName = "당동 878-3";
            this.legalDstCode = "4141010100";
            this.roadnmAdr = "경기 군포시 용호2로20번길 15-5";
            this.detailAdr = "당동다가구주택";
            this.dong = "";
            this.hosu = "";
            this.pubLandPrice = "700000000";
            this.kbMktPrice = "750000000";
            this.areaMeter = "103.234";
            this.areaPyung = "43";
            this.ownerCnt = "1";
            this.userProportion = "100";
            this.owner1Proportion = "0";
            this.isMovingInRight = "false";
        }else if(THREE.equals(this.houseId)){
            this.houseType = "4";
            this.houseName = "대경빌라";
            this.houseDetailName = "101호";
            this.legalDstCode = "4143010300";
            this.roadnmAdr = "경기 의왕시 부곡중앙북5길 5";
            this.detailAdr = "대경빌라";
            this.dong = "";
            this.hosu = "101호";
            this.pubLandPrice = "330000000";
            this.kbMktPrice = "400000000";
            this.areaMeter = "59.384";
            this.areaPyung = "25";
            this.ownerCnt = "1";
            this.userProportion = "100";
            this.owner1Proportion = "0";
            this.isMovingInRight = "false";
        }else{
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
}
