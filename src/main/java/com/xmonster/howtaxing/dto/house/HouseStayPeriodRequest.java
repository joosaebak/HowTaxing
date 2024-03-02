package com.xmonster.howtaxing.dto.house;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HouseStayPeriodRequest {
    private String certOrg;         // 인증기관(공통 - pass:PASS인증, kakao:카카오톡, payco:페이코, kica:삼성패스, kb:KB스타뱅킹)
    private String userNm;          // 이름
    private String mobileNo;        // 휴대폰번호
    private String rlno;            // 주민등록번호
    private String mobileCo;        // 통신사(PASS - SKT:S KT:K LGU+:L) *앋뜰통신사구분없음)
    private String sido;            // 시도
    private String sigungu;         // 시군구
    private String step;            // 로그인 단계(간편인증시 1단계:init, 2단계:sign)
}
