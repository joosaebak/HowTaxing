package com.xmonster.howtaxing.model;

import com.xmonster.howtaxing.type.ErrorCode;
import static com.xmonster.howtaxing.constant.CommonConstant.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ErrorResponseEntity {
    private int status;
    private String name;
    private String errYn;
    private String errCode;
    private String errMsg;
    private String errMsgDtl;

    public static ResponseEntity<ErrorResponseEntity> toResponseEntity(ErrorCode e){
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ErrorResponseEntity.builder()
                        .status(e.getHttpStatus().value())
                        .name(e.name())
                        .errYn(YES)
                        .errCode(e.getCode())
                        .errMsg(e.getMessage())
                        .build());
    }

    public static ResponseEntity<ErrorResponseEntity> toResponseEntity(ErrorCode e, String errMsgDtl){
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ErrorResponseEntity.builder()
                        .status(e.getHttpStatus().value())
                        .name(e.name())
                        .errYn(YES)
                        .errCode(e.getCode())
                        .errMsg(e.getMessage())
                        .errMsgDtl(errMsgDtl)
                        .build());
    }

    public static ResponseEntity<ErrorResponseEntity> toResponseEntity(ErrorCode e, String errMsg, String errMsgDtl){
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ErrorResponseEntity.builder()
                        .status(e.getHttpStatus().value())
                        .name(e.name())
                        .errYn(YES)
                        .errCode(e.getCode())
                        .errMsg(errMsg)
                        .errMsgDtl(errMsgDtl)
                        .build());
    }
}