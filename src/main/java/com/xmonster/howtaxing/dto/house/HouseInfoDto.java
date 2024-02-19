package com.xmonster.howtaxing.dto.house;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HouseInfoDto {
    private Integer resultListNo;   // 조회결과 목록번호(1:건축물대장정보 2:부동산거래내역 3:재산세정보)
    private String tradeType;       // 거래유형(0:없음 1:매수 2:매도)

    private boolean isCurOwn;       // 현재보유여부
    private String houseType;       // 주택유형
    private String houseName;       // 주택명
    private String houseDetailName; // 주택상세명
    private LocalDate contractDate; // 계약일자
    private LocalDate buyDate;      // 취득일자
    private LocalDate moveInDate;   // 전입일자
    private LocalDate moveOutDate;  // 전출일자
    private LocalDate sellDate;     // 양도일자
    private Long buyPrice;          // 취득금액
    private Long sellPrice;         // 양도금액
    private Long pubLandPrice;      // 공시지가
    private Long kbMktPrice;        // KB시세
    private String jibunAdr;        // 지번주소
    private String roadnmAdr;       // 도로명주소
    private String buildingMgmtNo;  // 건물관리번호
    private String detailAdr;       // 상세주소
    private String legalDstCode;    // 법정동코드
    private String dong;            // 동
    private String hosu;            // 호수
    private BigDecimal area;        // 전용면적
    private boolean isDestruction;  // 멸실여부
    private Integer ownerCnt;       // 소유자수
    private Integer proportion;     // 본인지분비율
    private boolean isMoveInRight;  // 입주권여부
    private String sourceType;      // 출처구분
}
