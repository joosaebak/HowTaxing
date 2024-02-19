package com.xmonster.howtaxing.dto.hyphen;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HyphenRequestUserHouseDto {
    private String loginMethod;
    private String loginOrgCd;
    private String bizNo;
    private String userId;
    private String userPw;
}
