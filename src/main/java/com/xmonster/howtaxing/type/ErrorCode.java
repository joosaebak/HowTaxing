package com.xmonster.howtaxing.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "ACCOUNT-001", "사용자를 찾을 수 없습니다."),
    HAS_EMAIL(HttpStatus.BAD_REQUEST, "ACCOUNT-002", "존재하는 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "ACCOUNT-003", "비밀번호가 일치하지 않습니다."),

    LOGIN_FAILED_COMMON(HttpStatus.NOT_FOUND, "LOGIN-001", "로그인에 실패하였습니다."),
    LOGIN_FAILED_HAS_EMAIL(HttpStatus.NOT_FOUND, "LOGIN-002", "이미 동일한 이메일 계정으로 가입되어 있습니다."),

    HOUSE_FAILED_HYPHEN_TOKEN(HttpStatus.NOT_FOUND, "HOUSE-001", "공공기관에서 보유 주택 정보를 가져오지 못했습니다."),
    HOUSE_FAILED_HYPHEN_INPUT(HttpStatus.NOT_FOUND, "HOUSE-001", "보유 주택정보 조회 간편인증 입력값이 올바르지 않습니다."),
    HOUSE_FAILED_HYPHEN_LIST(HttpStatus.NOT_FOUND, "HOUSE-002", "공공기관에서 보유 주택 정보를 가져오지 못했습니다."),
    HOUSE_FAILED_HYPHEN_COMMON(HttpStatus.NOT_FOUND, "HOUSE-003", "공공기관에서 보유 주택 정보를 가져오지 못했습니다."),
    HOUSE_FAILED_HYPHEN_DATA(HttpStatus.NOT_FOUND, "HOUSE-004", "공공기관에서 보유 주택 정보를 가져오지 못했습니다."),

    ETC_ERROR(HttpStatus.NOT_FOUND, "ETC-001", "오류가 발생했습니다.");

    private final HttpStatus httpStatus;	// HttpStatus
    private final String code;				// ACCOUNT-001
    private final String message;			// 설명
}