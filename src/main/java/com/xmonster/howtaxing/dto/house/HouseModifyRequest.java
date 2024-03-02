package com.xmonster.howtaxing.dto.house;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HouseModifyRequest {
    private Long houseId;               // 주택ID
    private String houseName;           // 주택명
    private String detailAdr;           // 상세주소
    private String jibunAddr;           // 지번주소
    private String roadAddr;            // 도로명주소
    private String roadAddrRef;         // 도로명주소참고항목
    private String bdMgtSn;             // 건물관리번호
    private String admCd;               // 행정구역코드
    private String rnMgtSn;             // 도로명코드
    private LocalDate buyDate;          // 취득일자
    private Long buyPrice;              // 취득금액
    private Long pubLandPrice;          // 공시지가
    private Long kbMktPrice;            // KB시세
    private BigDecimal area;            // 전용면적
    private boolean isDestruction;      // 멸실여부
    private Integer ownerCnt;           // 소유자수
    private Integer userProportion;     // 본인지분비율
    private boolean isMoveInRight;      // 입주권여부
}
