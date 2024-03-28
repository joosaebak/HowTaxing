package com.xmonster.howtaxing.service.calculation;

import com.xmonster.howtaxing.CustomException;
import com.xmonster.howtaxing.dto.calculation.CalculationBuyResultRequest;
import com.xmonster.howtaxing.dto.calculation.CalculationBuyResultResponse;
import com.xmonster.howtaxing.model.CalculationProcess;
import com.xmonster.howtaxing.repository.calculation.CalculationProcessRepository;
import com.xmonster.howtaxing.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Method;
import java.util.List;

import static com.xmonster.howtaxing.constant.CommonConstant.*;

@Transactional
@RequiredArgsConstructor
@Slf4j
public class CalculationBuyBranchService {
    private final CalculationProcessRepository calculationProcessRepository;

    // 취득세 분기번호 001
    public CalculationBuyResultResponse branchNo001(CalculationBuyResultRequest calculationBuyResultRequest, Object bean, Method method){
        log.info(">>> CalculationBuyService branchNo001 - 취득세 분기번호 001 : 취득하려는 주택의 유형");

        int selectNo = 0;
        String houseType = calculationBuyResultRequest.getHouseType();
        List<CalculationProcess> list = calculationProcessRepository.findByCalcTypeAndBranchNo("01", "001")
                .orElseThrow(() -> new CustomException(ErrorCode.ETC_ERROR));

        if(list == null || list.isEmpty()) throw new CustomException(ErrorCode.ETC_ERROR);

        if(FIVE.equals(houseType)){
            selectNo = 2;   // 준공 분양권(선택번호:2)
        } else if(THREE.equals(houseType)){
            selectNo = 3;   // 입주권(선택번호:3)
        } else{
            selectNo = 1;   // 주택(선택번호:1)
        }

        log.info("---------- 분기정보 START ----------");
        for(CalculationProcess calculationProcess : list){
            int index = 1;
            log.info("----- " + index + "-----");
            log.info("계산구분 : " + calculationProcess.getCalculationProcessId().getCalcType());
            log.info("계산명 : " + calculationProcess.getCalcName());
            log.info("분기번호 : " + calculationProcess.getCalculationProcessId().getBranchNo());
            log.info("분기명 : " + calculationProcess.getBranchName());
            log.info("선택번호 : " + calculationProcess.getCalculationProcessId().getSelectNo());
            log.info("선택내용 : " + calculationProcess.getSelectContent());
            log.info("가변데이터 : " + calculationProcess.getVariableData());
            log.info("데이터함수 : " + calculationProcess.getDataMethod());
            log.info("다음분기존재여부 : " + calculationProcess.isHasNextBranch());
            log.info("다음분기번호 : " + calculationProcess.getNextBranchNo());
            log.info("세율코드 : " + calculationProcess.getTaxRateCode());
            log.info("공제코드 : " + calculationProcess.getDedCode());
        }
        log.info("---------- 분기정보 END ----------");

        /*switch (selectNo){
            case 1:
            case 2:
            case 3:
            default:
                return this.branchNo002(calculationBuyResultRequest);
        }*/

        CalculationBuyResultResponse calculationBuyResultResponse = null;
        try{
            // 메소드 호출
            calculationBuyResultResponse = (CalculationBuyResultResponse) method.invoke(bean, calculationBuyResultRequest, method);
        }catch(Exception e){
            throw new CustomException(ErrorCode.ETC_ERROR);
        }

        return calculationBuyResultResponse;
    }

    // 취득세 분기번호 002
    public CalculationBuyResultResponse branchNo002(CalculationBuyResultRequest calculationBuyResultRequest, Method method){
        log.info(">>> CalculationBuyService branchNo001 - 취득세 분기번호 002");

        return null;
    }
}