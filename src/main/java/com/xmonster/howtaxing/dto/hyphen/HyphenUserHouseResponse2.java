package com.xmonster.howtaxing.dto.hyphen;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HyphenUserHouseResponse2 {
    @Builder.Default
    private HyphenUserHouseCommon hyphenUserHouseCommon = HyphenUserHouseCommon.builder().build();

    @Builder.Default
    private HyphenUserHouseData hyphenUserHouseData = HyphenUserHouseData.builder().build();

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HyphenUserHouseCommon {
        private String userTrNo;
        private String hyphenTrNo;
        private String errYn;
        private String errMsg;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HyphenUserHouseData {
        private String listMsg1;

        @Builder.Default
        private List<HyphenUserHouseDataDetail1> list1 = new ArrayList<>();
        private String listMsg2;
        @Builder.Default
        private List<HyphenUserHouseDataDetail2> list2 = new ArrayList<>();
        private String listMsg3;
        @Builder.Default
        private List<HyphenUserHouseDataDetail3> list3 = new ArrayList<>();

        @Builder
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        /* 건축물대장정보 */
        public static class HyphenUserHouseDataDetail1 {
            private String name;                        // 성명
            private String address;                     // 주소
            private String area;                        // 면적(m^2)
            private String approvalDate;                // 사용승인일
            private String reasonChangeOwnership;       // 소유권변동사유
            private String ownershipChangeDate;         // 소유권변동일
            private String publicationBaseDate;         // 공시기준일
            private String publishedPrice;              // 공시가격(천원)
            private String baseDate;                    // 기준일자
        }

        @Builder
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        /* 부동산거래내역(주택분) */
        public static class HyphenUserHouseDataDetail2 {
            private String name;                        // 성명
            private String address;                     // 주소
            private String sellBuyClassification;       // 매도/매수구분
            private String area;                        // 전용면적(m^2)
            private String tradingPrice;                // 매매가
            private String balancePaymentDate;          // 잔금지급일
            private String contractDate;                // 계약일자
            private String startDate;                   // 신고분기준 시작일
            private String endDate;                     // 신고분기준 종료일
        }

        @Builder
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        /* 재산세정보(주택분) */
        public static class HyphenUserHouseDataDetail3 {
            private String name;                        // 성명
            private String address;                     // 주소
            private String area;                        // 전용면적(m^2)
            private String acquisitionDate;             // 취득일자
            private String baseDate;                    // 기준일자
        }
    }
}