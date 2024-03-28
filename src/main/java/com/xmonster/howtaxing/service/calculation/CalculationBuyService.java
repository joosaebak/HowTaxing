package com.xmonster.howtaxing.service.calculation;

import com.xmonster.howtaxing.CustomException;
import com.xmonster.howtaxing.dto.calculation.CalculationBuyResultRequest;
import com.xmonster.howtaxing.dto.calculation.CalculationBuyResultResponse;
import com.xmonster.howtaxing.dto.common.ApiResponse;

import com.xmonster.howtaxing.model.CalculationProcess;
import com.xmonster.howtaxing.model.DeductionInfo;
import com.xmonster.howtaxing.model.TaxRateInfo;
import com.xmonster.howtaxing.model.User;
import com.xmonster.howtaxing.repository.calculation.CalculationBuyResultRepository;
import com.xmonster.howtaxing.repository.calculation.CalculationProcessRepository;
import com.xmonster.howtaxing.repository.calculation.DeductionInfoRepository;
import com.xmonster.howtaxing.repository.calculation.TaxRateInfoRepository;
import com.xmonster.howtaxing.repository.house.HouseRepository;
import com.xmonster.howtaxing.service.house.HouseAddressService;
import com.xmonster.howtaxing.type.ErrorCode;
import com.xmonster.howtaxing.utils.HouseUtil;
import com.xmonster.howtaxing.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.xmonster.howtaxing.constant.CommonConstant.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CalculationBuyService {
    private final HouseAddressService houseAddressService;

    private final CalculationBuyResultRepository calculationBuyResultRepository;
    private final CalculationProcessRepository calculationProcessRepository;
    private final TaxRateInfoRepository taxRateInfoRepository;
    private final DeductionInfoRepository deductionInfoRepository;
    private final HouseRepository houseRepository;

    private final UserUtil userUtil;
    private final HouseUtil houseUtil;

    private Class<?> classHello;
    private CalculationBranch target;

    // 취득세 계산 결과 조회
    public Object getCalculationBuyResult(CalculationBuyResultRequest calculationBuyResultRequest){
        log.info(">> [Service]CalculationBuyService getCalculationBuyResult - 취득세 계산 결과 조회");

        // 분기 메소드
        try{
            classHello = Class.forName("com.xmonster.howtaxing.service.calculation.CalculationBuyService$CalculationBranch");
            target = new CalculationBranch();

            Method method = classHello.getMethod("calculationStart", CalculationBuyResultRequest.class);

            Object result = method.invoke(target, calculationBuyResultRequest);

            return ApiResponse.success(result);

        }catch(Exception e){
            throw new CustomException(ErrorCode.ETC_ERROR);
        }
    }

    private class CalculationBranch {
        // Start
        public Object calculationStart(CalculationBuyResultRequest calculationBuyResultRequest){
            log.info(">>> CalculationBuyService calculationStart - 계산 시작");
            CalculationBuyResultResponse calculationBuyResultResponse = null;

            try{
                Method method = classHello.getMethod("branchNo001", CalculationBuyResultRequest.class);
                calculationBuyResultResponse = (CalculationBuyResultResponse) method.invoke(target, calculationBuyResultRequest);
            }catch(Exception e){
                throw new CustomException(ErrorCode.ETC_ERROR);
            }

            return calculationBuyResultResponse;
        }

        public CalculationBuyResultResponse branchNo001(CalculationBuyResultRequest calculationBuyResultRequest){
            log.info(">>> CalculationBranch branchNo001 - 취득세 분기번호 001 : 취득하려는 주택의 유형");

            CalculationBuyResultResponse calculationBuyResultResponse;
            boolean hasNext = false;
            String nextBranchNo = EMPTY;
            String taxRateCode = EMPTY;
            String dedCode = EMPTY;
            int selectNo = 0;
            
            // 주택유형(1:아파트 2:연립,다가구 3:입주권 4:단독주택,다세대 5:분양권(주택) 6:주택)
            String houseType = calculationBuyResultRequest.getHouseType();
            
            List<CalculationProcess> list = calculationProcessRepository.findByCalcTypeAndBranchNo(CALC_TYPE_BUY, "001")
                    .orElseThrow(() -> new CustomException(ErrorCode.ETC_ERROR));

            if(list == null || list.isEmpty()) throw new CustomException(ErrorCode.ETC_ERROR);

            if(FIVE.equals(houseType)){
                selectNo = 2;   // 준공 분양권(선택번호:2)
            } else if(THREE.equals(houseType)){
                selectNo = 3;   // 입주권(선택번호:3)
            } else{
                selectNo = 1;   // 주택(선택번호:1)
            }

            for(CalculationProcess calculationProcess : list){
                if(selectNo == calculationProcess.getCalculationProcessId().getSelectNo()){
                    log.info("selectNo : " + selectNo + ", selectContent : " + calculationProcess.getSelectContent());
                    if(calculationProcess.isHasNextBranch()){
                        nextBranchNo = calculationProcess.getNextBranchNo();
                        hasNext = true;
                    }else{
                        taxRateCode = calculationProcess.getTaxRateCode();
                        dedCode = calculationProcess.getDedCode();
                    }
                    break;
                }
            }

            if(hasNext){
                try{
                    Method method = classHello.getMethod("branchNo" + nextBranchNo, CalculationBuyResultRequest.class);
                    calculationBuyResultResponse = (CalculationBuyResultResponse) method.invoke(target, calculationBuyResultRequest);
                }catch(Exception e){
                    throw new CustomException(ErrorCode.ETC_ERROR);
                }
            }else{
                calculationBuyResultResponse = getCalculationBuyResult(calculationBuyResultRequest, taxRateCode, dedCode);
            }

            return calculationBuyResultResponse;
        }

        public CalculationBuyResultResponse branchNo002(CalculationBuyResultRequest calculationBuyResultRequest){
            log.info(">>> CalculationBranch branchNo002 - 취득세 분기번호 002");
            return null;
        }

        public CalculationBuyResultResponse branchNo007(CalculationBuyResultRequest calculationBuyResultRequest){
            log.info(">>> CalculationBranch branchNo007 - 취득세 분기번호 007");
            return null;
        }

        public CalculationBuyResultResponse branchNo008(CalculationBuyResultRequest calculationBuyResultRequest){
            log.info(">>> CalculationBranch branchNo008 - 취득세 분기번호 008");

            CalculationBuyResultResponse calculationBuyResultResponse;
            boolean hasNext = false;
            String nextBranchNo = EMPTY;
            String taxRateCode = EMPTY;
            String dedCode = EMPTY;
            int selectNo = 0;

            List<CalculationProcess> list = calculationProcessRepository.findByCalcTypeAndBranchNo(CALC_TYPE_BUY, "008")
                    .orElseThrow(() -> new CustomException(ErrorCode.ETC_ERROR));

            if(list == null || list.isEmpty()) throw new CustomException(ErrorCode.ETC_ERROR);

            Long pubLandPrice = calculationBuyResultRequest.getPubLandPrice();  // 공시지가

            for(CalculationProcess calculationProcess : list){
                String dataMethod = StringUtils.defaultString(calculationProcess.getDataMethod());
                String variableData = StringUtils.defaultString(calculationProcess.getVariableData(), ZERO);

                if(checkSelectNoCondition(DATA_TYPE_PRICE, Long.toString(pubLandPrice), variableData, dataMethod)){
                    selectNo = calculationProcess.getCalculationProcessId().getSelectNo();
                    log.info("selectNo : " + selectNo + ", selectContent : " + calculationProcess.getSelectContent());

                    if(calculationProcess.isHasNextBranch()){
                        nextBranchNo = calculationProcess.getNextBranchNo();
                        hasNext = true;
                    }else{
                        taxRateCode = calculationProcess.getTaxRateCode();
                        dedCode = calculationProcess.getDedCode();
                    }
                }
            }

            if(hasNext){
                try{
                    Method method = classHello.getMethod("branchNo" + nextBranchNo, CalculationBuyResultRequest.class);
                    calculationBuyResultResponse = (CalculationBuyResultResponse) method.invoke(target, calculationBuyResultRequest);
                }catch(Exception e){
                    throw new CustomException(ErrorCode.ETC_ERROR);
                }
            }else{
                calculationBuyResultResponse = getCalculationBuyResult(calculationBuyResultRequest, taxRateCode, dedCode);
            }

            return calculationBuyResultResponse;
        }

        public CalculationBuyResultResponse branchNo014(CalculationBuyResultRequest calculationBuyResultRequest){
            log.info(">>> CalculationBranch branchNo014 - 취득세 분기번호 014");

            CalculationBuyResultResponse calculationBuyResultResponse;
            boolean hasNext = false;
            String nextBranchNo = EMPTY;
            String taxRateCode = EMPTY;
            String dedCode = EMPTY;
            int selectNo = 0;

            List<CalculationProcess> list = calculationProcessRepository.findByCalcTypeAndBranchNo(CALC_TYPE_BUY, "014")
                    .orElseThrow(() -> new CustomException(ErrorCode.ETC_ERROR));

            if(list == null || list.isEmpty()) throw new CustomException(ErrorCode.ETC_ERROR);

            // (취득주택 포함)보유주택 수
            long ownHouseCount = getOwnHouseCount();

            if(ownHouseCount == 1){
                selectNo = 1;
            }else if(ownHouseCount == 2){
                selectNo = 2;
            }else if(ownHouseCount == 3){
                selectNo = 3;
            }else if(ownHouseCount >= 4){
                selectNo = 4;
            }

            for(CalculationProcess calculationProcess : list){
                if(selectNo == calculationProcess.getCalculationProcessId().getSelectNo()){
                    log.info("selectNo : " + selectNo + ", selectContent : " + calculationProcess.getSelectContent());
                    if(calculationProcess.isHasNextBranch()){
                        nextBranchNo = calculationProcess.getNextBranchNo();
                        hasNext = true;
                    }else{
                        taxRateCode = calculationProcess.getTaxRateCode();
                        dedCode = calculationProcess.getDedCode();
                    }
                    break;
                }
            }

            if(hasNext){
                try{
                    Method method = classHello.getMethod("branchNo" + nextBranchNo, CalculationBuyResultRequest.class);
                    calculationBuyResultResponse = (CalculationBuyResultResponse) method.invoke(target, calculationBuyResultRequest);
                }catch(Exception e){
                    throw new CustomException(ErrorCode.ETC_ERROR);
                }
            }else{
                calculationBuyResultResponse = getCalculationBuyResult(calculationBuyResultRequest, taxRateCode, dedCode);
            }

            return calculationBuyResultResponse;
        }

        public CalculationBuyResultResponse branchNo015(CalculationBuyResultRequest calculationBuyResultRequest){
            log.info(">>> CalculationBranch branchNo015 - 취득세 분기번호 015");

            CalculationBuyResultResponse calculationBuyResultResponse;
            boolean hasNext = false;
            String nextBranchNo = EMPTY;
            String taxRateCode = EMPTY;
            String dedCode = EMPTY;
            int selectNo = 0;

            List<CalculationProcess> list = calculationProcessRepository.findByCalcTypeAndBranchNo(CALC_TYPE_BUY, "015")
                    .orElseThrow(() -> new CustomException(ErrorCode.ETC_ERROR));

            if(list == null || list.isEmpty()) throw new CustomException(ErrorCode.ETC_ERROR);

            // 종전주택이 2020.08.12 이후 취득한 분양권이나 지방세법상 입주권인 경우(재개발, 재건축, 소규모재건축)
            // TODO : 종전주택 정보를 가져오기
            // TODO : 종전주택의 주택유형이 일반주택이 아닌지 체크
            // TODO : 종전주택의 취득일자가 20200812 이후인지 체크
            // TODO : 위 조건을 모두 만족하면 017로 이동, 아니면 016으로 이동

            // 일단 무조건 017로 보낼 예정(추후 개선)
            nextBranchNo = "017";

            try{
                Method method = classHello.getMethod("branchNo" + nextBranchNo, CalculationBuyResultRequest.class);
                calculationBuyResultResponse = (CalculationBuyResultResponse) method.invoke(target, calculationBuyResultRequest);
            }catch(Exception e){
                throw new CustomException(ErrorCode.ETC_ERROR);
            }

            return calculationBuyResultResponse;
        }

        public CalculationBuyResultResponse branchNo017(CalculationBuyResultRequest calculationBuyResultRequest){
            log.info(">>> CalculationBranch branchNo017 - 취득세 분기번호 017");

            CalculationBuyResultResponse calculationBuyResultResponse;
            boolean hasNext = false;
            String nextBranchNo = EMPTY;
            String taxRateCode = EMPTY;
            String dedCode = EMPTY;
            int selectNo = 0;

            List<CalculationProcess> list = calculationProcessRepository.findByCalcTypeAndBranchNo(CALC_TYPE_BUY, "017")
                    .orElseThrow(() -> new CustomException(ErrorCode.ETC_ERROR));

            if(list == null || list.isEmpty()) throw new CustomException(ErrorCode.ETC_ERROR);

            // 조정대상지역여부
            boolean isAdjustmentTargetArea = checkAdjustmentTargetArea(StringUtils.defaultString(calculationBuyResultRequest.getRoadAddr()));

            if(isAdjustmentTargetArea){
                selectNo = 1;
            }else{
                selectNo = 2;
            }

            for(CalculationProcess calculationProcess : list){
                if(selectNo == calculationProcess.getCalculationProcessId().getSelectNo()){
                    log.info("selectNo : " + selectNo + ", selectContent : " + calculationProcess.getSelectContent());
                    if(calculationProcess.isHasNextBranch()){
                        nextBranchNo = calculationProcess.getNextBranchNo();
                        hasNext = true;
                    }else{
                        taxRateCode = calculationProcess.getTaxRateCode();
                        dedCode = calculationProcess.getDedCode();
                    }
                    break;
                }
            }

            if(hasNext){
                try{
                    Method method = classHello.getMethod("branchNo" + nextBranchNo, CalculationBuyResultRequest.class);
                    calculationBuyResultResponse = (CalculationBuyResultResponse) method.invoke(target, calculationBuyResultRequest);
                }catch(Exception e){
                    throw new CustomException(ErrorCode.ETC_ERROR);
                }
            }else{
                calculationBuyResultResponse = getCalculationBuyResult(calculationBuyResultRequest, taxRateCode, dedCode);
            }

            return calculationBuyResultResponse;
        }

        public CalculationBuyResultResponse branchNo019(CalculationBuyResultRequest calculationBuyResultRequest){
            log.info(">>> CalculationBranch branchNo019 - 취득세 분기번호 019");

            CalculationBuyResultResponse calculationBuyResultResponse;
            boolean hasNext = false;
            String nextBranchNo = EMPTY;
            String taxRateCode = EMPTY;
            String dedCode = EMPTY;
            int selectNo = 0;

            List<CalculationProcess> list = calculationProcessRepository.findByCalcTypeAndBranchNo(CALC_TYPE_BUY, "019")
                    .orElseThrow(() -> new CustomException(ErrorCode.ETC_ERROR));

            if(list == null || list.isEmpty()) throw new CustomException(ErrorCode.ETC_ERROR);

            // 조정대상지역여부
            boolean hasSellPlan = calculationBuyResultRequest.isHasSellPlan();

            if(hasSellPlan){
                selectNo = 1;
            }else{
                selectNo = 2;
            }

            for(CalculationProcess calculationProcess : list){
                if(selectNo == calculationProcess.getCalculationProcessId().getSelectNo()){
                    log.info("selectNo : " + selectNo + ", selectContent : " + calculationProcess.getSelectContent());
                    if(calculationProcess.isHasNextBranch()){
                        nextBranchNo = calculationProcess.getNextBranchNo();
                        hasNext = true;
                    }else{
                        taxRateCode = calculationProcess.getTaxRateCode();
                        dedCode = calculationProcess.getDedCode();
                    }
                    break;
                }
            }

            if(hasNext){
                try{
                    Method method = classHello.getMethod("branchNo" + nextBranchNo, CalculationBuyResultRequest.class);
                    calculationBuyResultResponse = (CalculationBuyResultResponse) method.invoke(target, calculationBuyResultRequest);
                }catch(Exception e){
                    throw new CustomException(ErrorCode.ETC_ERROR);
                }
            }else{
                calculationBuyResultResponse = getCalculationBuyResult(calculationBuyResultRequest, taxRateCode, dedCode);
            }

            return calculationBuyResultResponse;
        }

        public CalculationBuyResultResponse branchNo020(CalculationBuyResultRequest calculationBuyResultRequest){
            log.info(">>> CalculationBranch branchNo020 - 취득세 분기번호 020");

            CalculationBuyResultResponse calculationBuyResultResponse;
            boolean hasNext = false;
            String nextBranchNo = EMPTY;
            String taxRateCode = EMPTY;
            String dedCode = EMPTY;
            int selectNo = 0;

            List<CalculationProcess> list = calculationProcessRepository.findByCalcTypeAndBranchNo(CALC_TYPE_BUY, "020")
                    .orElseThrow(() -> new CustomException(ErrorCode.ETC_ERROR));

            if(list == null || list.isEmpty()) throw new CustomException(ErrorCode.ETC_ERROR);

            // 조정대상지역여부
            boolean isAdjustmentTargetArea = checkAdjustmentTargetArea(StringUtils.defaultString(calculationBuyResultRequest.getRoadAddr()));

            if(isAdjustmentTargetArea){
                selectNo = 1;
            }else{
                selectNo = 2;
            }

            for(CalculationProcess calculationProcess : list){
                if(selectNo == calculationProcess.getCalculationProcessId().getSelectNo()){
                    log.info("selectNo : " + selectNo + ", selectContent : " + calculationProcess.getSelectContent());
                    if(calculationProcess.isHasNextBranch()){
                        nextBranchNo = calculationProcess.getNextBranchNo();
                        hasNext = true;
                    }else{
                        taxRateCode = calculationProcess.getTaxRateCode();
                        dedCode = calculationProcess.getDedCode();
                    }
                    break;
                }
            }

            if(hasNext){
                try{
                    Method method = classHello.getMethod("branchNo" + nextBranchNo, CalculationBuyResultRequest.class);
                    calculationBuyResultResponse = (CalculationBuyResultResponse) method.invoke(target, calculationBuyResultRequest);
                }catch(Exception e){
                    throw new CustomException(ErrorCode.ETC_ERROR);
                }
            }else{
                calculationBuyResultResponse = getCalculationBuyResult(calculationBuyResultRequest, taxRateCode, dedCode);
            }

            return calculationBuyResultResponse;
        }


        // 취득세 계산 결과 조회
        public CalculationBuyResultResponse getCalculationBuyResult(CalculationBuyResultRequest calculationBuyResultRequest, String taxRateCode, String dedCode){
            log.info(">>> CalculationBranch getCalculationBuyResult - 취득세 계산 수행");

            float buyTaxRate = 0;       // 취득세율
            float taxRate1 = 0;         // 세율1
            float taxRate2 = 0;         // 세율2
            float addTaxRate1 = 0;      // 추가세율1
            float addTaxRate2 = 0;      // 추가세율2
            float finalTaxRate1 = 0;    // 최종세율1
            float finalTaxRate2 = 0;    // 최종세율2
            float agrTaxRate = 0;       // 농어촌특별세
            float eduTaxRate = 0;       // 지방교육세

            long buyTaxPrice = 0;       // 취득세액
            long agrTaxPrice = 0;       // 농어촌특별세액
            long eduTaxPrice = 0;       // 지방교육세액
            long totalTaxPrice = 0;     // 총납부세액

            // (취득주택 포함)보유주택 수
            long ownHouseCount = getOwnHouseCount();

            // 취득가액
            long buyPrice = calculationBuyResultRequest.getBuyPrice();

            // (취득주택)조정지역여부
            boolean isAdjustmentTargetArea = checkAdjustmentTargetArea(StringUtils.defaultString(calculationBuyResultRequest.getRoadAddr()));

            log.info("- 취득주택 포함 보유주택 수 : " + ownHouseCount);
            log.info("- 취득가액 : " + buyPrice);
            log.info("- 취득주택 조정지역여부 : " + isAdjustmentTargetArea);
            
            // 세율정보
            TaxRateInfo taxRateInfo = null;
            if(taxRateCode != null && !taxRateCode.isBlank()){
                log.info("세율정보 조회");
                taxRateInfo = taxRateInfoRepository.findByTaxRateCode(taxRateCode)
                        .orElseThrow(() -> new CustomException(ErrorCode.ETC_ERROR));
            }

            // 공제정보
            DeductionInfo deductionInfo = null;
            if(dedCode != null && !dedCode.isBlank()){
                log.info("공제정보 조회");
                deductionInfo = deductionInfoRepository.findByDedCode(dedCode)
                        .orElseThrow(() -> new CustomException(ErrorCode.ETC_ERROR));
            }

            /* 1. 취득세 계산 */
            log.info("1. 취득세 계산");
            if(taxRateInfo != null){
                // 세율이 상수인 경우
                if(YES.equals(taxRateInfo.getConstYn())){
                    buyTaxRate = Float.parseFloat(StringUtils.defaultString(taxRateInfo.getTaxRate1(), ZERO));
                }
                // 세율이 상수가 아닌 경우(변수)
                else{
                    if(taxRateInfo.getTaxRate1() != null && !taxRateInfo.getTaxRate1().isBlank()){
                        // 세율이 2개인 경우
                        if(taxRateInfo.getTaxRate2() != null && !taxRateInfo.getTaxRate2().isBlank()){
                            // 세율1
                            if(GENERAL_TAX_RATE.equals(taxRateInfo.getTaxRate1())){
                                taxRate1 = calculateGeneralTaxRate(calculationBuyResultRequest, ownHouseCount, buyPrice, isAdjustmentTargetArea);
                            }else if(NON_TAX_RATE.equals(taxRateInfo.getTaxRate1())){
                                taxRate1 = 0;
                            }else{
                                taxRate1 = Float.parseFloat(StringUtils.defaultString(taxRateInfo.getTaxRate1(), ZERO));
                            }

                            // 세율2
                            if(GENERAL_TAX_RATE.equals(taxRateInfo.getTaxRate2())){
                                taxRate2 = calculateGeneralTaxRate(calculationBuyResultRequest, ownHouseCount, buyPrice, isAdjustmentTargetArea);
                            }else if(NON_TAX_RATE.equals(taxRateInfo.getTaxRate2())){
                                taxRate2 = 0;
                            }else{
                                taxRate2 = Float.parseFloat(StringUtils.defaultString(taxRateInfo.getTaxRate2(), ZERO));
                            }

                            // 추가세율1
                            if(taxRateInfo.getAddTaxRate1() != null && !taxRateInfo.getAddTaxRate1().isBlank()){
                                addTaxRate1 = Float.parseFloat(StringUtils.defaultString(taxRateInfo.getAddTaxRate1(), ZERO));
                            }

                            // 추가세율2
                            if(taxRateInfo.getAddTaxRate2() != null && !taxRateInfo.getAddTaxRate2().isBlank()){
                                addTaxRate2 = Float.parseFloat(StringUtils.defaultString(taxRateInfo.getAddTaxRate2(), ZERO));
                            }

                            finalTaxRate1 = taxRate1 + addTaxRate1;
                            finalTaxRate2 = taxRate2 + addTaxRate2;

                            // 사용함수
                            String usedFunc = StringUtils.defaultString(taxRateInfo.getUsedFunc());

                            // MAX : 세율1과 세율2 중 최대값 사용
                            if(MAX.equals(usedFunc)){
                                buyTaxRate = Math.max(finalTaxRate1, finalTaxRate2);
                                buyTaxPrice = (long) (buyPrice * buyTaxRate);
                            }
                            // OR_LESS_MORE : 기준금액 이하 세율1, 기준금액 초과 세율2
                            else if(OR_LESS_MORE.equals(usedFunc)){
                                long basePrice = taxRateInfo.getBasePrice();

                                if(buyPrice > basePrice){
                                    buyTaxPrice = (long)((basePrice * finalTaxRate1) + ((buyPrice - basePrice) * finalTaxRate2));
                                }else{
                                    buyTaxPrice = (long)(buyPrice * finalTaxRate1);
                                }

                                buyTaxRate = (float)(buyTaxPrice / buyPrice); // 취득세액으로 취득세율을 계산
                            }
                        }
                        // 세율이 1개인 경우
                        else{
                            // 일반과세(일반세율)
                            if(GENERAL_TAX_RATE.equals(taxRateInfo.getTaxRate1())){
                                taxRate1 = calculateGeneralTaxRate(calculationBuyResultRequest, ownHouseCount, buyPrice, isAdjustmentTargetArea);
                            }
                            // 비과세
                            else if(NON_TAX_RATE.equals(taxRateInfo.getTaxRate1())){
                                taxRate1 = 0;
                            }

                            // 추가세율
                            if(taxRateInfo.getAddTaxRate1() != null && !taxRateInfo.getAddTaxRate1().isBlank()){
                                addTaxRate1 = Float.parseFloat(StringUtils.defaultString(taxRateInfo.getAddTaxRate1(), ZERO));
                            }

                            finalTaxRate1 = taxRate1 + addTaxRate1;
                            buyTaxRate = finalTaxRate1;
                            buyTaxPrice = (long) (buyPrice * buyTaxRate);
                        }
                    }
                }
            }

            /* 2. 농어촌특별세 계산 */
            log.info("2. 농어촌특별세 계산");
            // 전용면적 85제곱미터 초과만 농어촌특별세 대상
            float area = calculationBuyResultRequest.getArea();
            if(area > AREA_85){
                // 1주택
                if(ownHouseCount == 1){
                    agrTaxRate = 0.002f;        // 농어촌특별세율 : 0.2%
                }
                // 2주택
                else if(ownHouseCount == 2){
                    // 조정대상지역
                    if(isAdjustmentTargetArea){
                        agrTaxRate = 0.006f;    // 농어촌특별세율 : 0.6%
                    }else{
                        agrTaxRate = 0.002f;    // 농어촌특별세율 : 0.2%
                    }
                }
                // 3주택
                else if(ownHouseCount == 3){
                    // 조정대상지역
                    if(isAdjustmentTargetArea){
                        agrTaxRate = 0.01f;     // 농어촌특별세율 : 1%
                    }else{
                        agrTaxRate = 0.006f;    // 농어촌특별세율 : 0.6%
                    }
                }
                // 4주택 이상
                else if(ownHouseCount >= 4){
                    // 조정대상지역
                    if(isAdjustmentTargetArea){
                        agrTaxRate = 0.01f;     // 농어촌특별세율 : 1%
                    }else{
                        agrTaxRate = 0.006f;    // 농어촌특별세율 : 0.6%
                    }
                }
            }

            // 농어촌특별세액
            agrTaxPrice = (long)(buyPrice * agrTaxRate);

            /* 3. 지방교육세 계산 */
            log.info("3. 지방교육세 계산");
            // 1주택
            if(ownHouseCount == 1){
                // 6억 이하
                if(buyPrice <= SIX_HND_MIL){
                    eduTaxRate = 0.001f;                // 지방교육세율 : 0.1%
                }
                // 6억 초과, 9억 이하
                else if(buyPrice > SIX_HND_MIL && buyPrice <= NINE_HND_MIL){
                    eduTaxRate = buyTaxRate / 10;       // 지방교육세율 : 취득세율의 1/10
                }
                // 9억 초과
                else{
                    eduTaxRate = 0.003f;                // 지방교육세율 : 0.3%
                }
            }
            // 2주택
            else if(ownHouseCount == 2){
                // 조정대상지역
                if(isAdjustmentTargetArea){
                    eduTaxRate = 0.004f;                // 지방교육세율 : 0.4%
                }else{
                    // 6억 이하
                    if(buyPrice <= SIX_HND_MIL){
                        eduTaxRate = 0.001f;            // 지방교육세율 : 0.1%
                    }
                    // 6억 초과, 9억 이하
                    else if(buyPrice > SIX_HND_MIL && buyPrice <= NINE_HND_MIL){
                        eduTaxRate = buyTaxRate / 10;   // 지방교육세율 : 취득세율의 1/10
                    }
                    // 9억 초과
                    else{
                        eduTaxRate = 0.003f;            // 지방교육세율 : 0.3%
                    }
                }
            }
            // 3주택
            else if(ownHouseCount == 3){
                // 조정대상지역
                if(isAdjustmentTargetArea){
                    eduTaxRate = 0.004f;                // 지방교육세율 : 0.4%
                }else{
                    eduTaxRate = 0.004f;                // 지방교육세율 : 0.4%
                }
            }
            // 4주택 이상
            else if(ownHouseCount >= 4){
                // 조정대상지역
                if(isAdjustmentTargetArea){
                    eduTaxRate = 0.004f;                // 지방교육세율 : 0.4%
                }else{
                    eduTaxRate = 0.004f;                // 지방교육세율 : 0.4%
                }
            }

            // 지방교육세액
            eduTaxPrice = (long)(buyPrice * eduTaxRate);

            /* 4. 총납부세액 계산 */
            log.info("4. 총납부세액 계산");
            totalTaxPrice = buyTaxPrice + eduTaxPrice + agrTaxPrice;

            return CalculationBuyResultResponse.builder()
                    .buyPrice(buyPrice)
                    .buyTaxRate(buyTaxRate)
                    .buyTaxPrice(buyTaxPrice)
                    .eduTaxRate(eduTaxRate)
                    .eduTaxPrice(eduTaxPrice)
                    .agrTaxRate(agrTaxRate)
                    .agrTaxPrice(agrTaxPrice)
                    .totalTaxPrice(totalTaxPrice)
                    .build();
        }

        // (취득주택 포함)보유주택 수 가져오기
        private long getOwnHouseCount(){
            // 호출 사용자 조회
            User findUser = userUtil.findCurrentUser();
            return houseRepository.countByUserId(findUser.getId()) + 1;
        }

        // (취득세)일반과세(일반세율) 계산
        private float calculateGeneralTaxRate(CalculationBuyResultRequest calculationBuyResultRequest, long ownHouseCount, long buyPrice, boolean isAdjustmentTargetArea){
            float taxRate = 0;

            // 1주택
            if(ownHouseCount == 1){
                // 6억 이하
                if(buyPrice <= SIX_HND_MIL){
                    // 취득세율 : 1%
                    taxRate = 0.01f;
                }
                // 6억 초과, 9억 이하
                else if(buyPrice > SIX_HND_MIL && buyPrice <= NINE_HND_MIL){
                    // 취득세율 : (((취득가액 / 1억) x 2 / 3 - 3) x 1)%
                    taxRate = (float)((buyPrice / ONE_HND_MIL) * 2 / 3 - 3) / 100;
                }
                // 9억 초과
                else{
                    // 취득세율 : 3%
                    taxRate = 0.03f;
                }
            }
            // 2주택
            else if(ownHouseCount == 2){
                // 조정대상지역 외(조정대상지역에 대한 취득세율은 다른 프로세스로 접근)
                if(!isAdjustmentTargetArea){
                    // 6억 이하
                    if(buyPrice <= SIX_HND_MIL){
                        // 취득세율 : 1%
                        taxRate = 0.01f;
                    }
                    // 6억 초과, 9억 이하
                    else if(buyPrice > SIX_HND_MIL && buyPrice <= NINE_HND_MIL){
                        // 취득세율 : (((취득가액 / 1억) x 2 / 3 - 3) x 1)%
                        taxRate = (float)((buyPrice / ONE_HND_MIL) * 2 / 3 - 3) / 100;
                    }
                    // 9억 초과
                    else{
                        // 취득세율 : 3%
                        taxRate = 0.03f;
                    }
                }
            }

            return taxRate;
        }

        // 조정대상지역 체크
        private boolean checkAdjustmentTargetArea(String address){
            String siGunGu = houseAddressService.separateAddress(address).getSiGunGu();

            // 조정대상지역(용산구, 서초구, 강남구, 송파구)
            return ADJUSTMENT_TARGET_AREA1.equals(siGunGu) || ADJUSTMENT_TARGET_AREA2.equals(siGunGu) || ADJUSTMENT_TARGET_AREA3.equals(siGunGu) || ADJUSTMENT_TARGET_AREA4.equals(siGunGu);
        }

        // selectNo 조건 부합 여부 체크
        private boolean checkSelectNoCondition(int dataType, String inputData, String variableData, String dataMethod){
            boolean result = false;
            // 금액
            if(DATA_TYPE_PRICE == dataType){
                if(inputData == null || variableData == null || dataMethod == null){
                    throw new CustomException(ErrorCode.ETC_ERROR);
                }
                long inputPrice = Long.parseLong(StringUtils.defaultString(inputData, ZERO));
                long variablePrice = Long.parseLong(StringUtils.defaultString(variableData, ZERO));

                // 미만
                if(LESS.equals(dataMethod)){
                    if(inputPrice < variablePrice){
                        result = true;
                    }
                }
                // 이하
                else if(OR_LESS.equals(dataMethod)){
                    if(inputPrice <= variablePrice){
                        result = true;
                    }
                }
                // 초과
                else if(MORE.equals(dataMethod)){
                    if(inputPrice > variablePrice){
                        result = true;
                    }
                }
                // 이상
                else if(OR_MORE.equals(dataMethod)){
                    if(inputPrice >= variablePrice){
                        result = true;
                    }
                }
            }
            // 날짜
            else if(DATA_TYPE_DATE == dataType){
                if(inputData == null || variableData == null || dataMethod == null){
                    throw new CustomException(ErrorCode.ETC_ERROR);
                }

                LocalDate inputDate = LocalDate.parse(inputData, DateTimeFormatter.ofPattern("yyyyMMdd"));
                String[] variableDataArr = variableData.split(",");
                LocalDate variableDate1 = null;
                LocalDate variableDate2 = null;

                variableDate1 = LocalDate.parse(variableDataArr[0], DateTimeFormatter.ofPattern("yyyyMMdd"));
                if(variableDataArr.length > 1){
                    variableDate2 = LocalDate.parse(variableDataArr[1], DateTimeFormatter.ofPattern("yyyyMMdd"));
                }

                //LocalDate variableDate = LocalDate.parse(variableData, DateTimeFormatter.ofPattern("yyyyMMdd"));

                // YYYYMMDD일 이전
                if(BEFORE.equals(dataMethod)){
                    if(inputDate.isBefore(variableDate1)){
                        result = true;
                    }
                }
                // YYYYYMMDD일 포함 이전
                else if(OR_BEFORE.equals(dataMethod)){
                    if(inputDate.isBefore(variableDate1) || inputDate.isEqual(variableDate1)){
                        result = true;
                    }
                }
                // YYYYMMDD일 이후
                else if(AFTER.equals(dataMethod)){
                    if(inputDate.isAfter(variableDate1)){
                        result = true;
                    }
                }
                // YYYYMMDD일 포함 이후
                else if(OR_AFTER.equals(dataMethod)){
                    if(inputDate.isAfter(variableDate1) || inputDate.isEqual(variableDate1)){
                        result = true;
                    }
                }
                // YYYYMMDD일 부터 YYYYMMDD일 까지
                else if(FROM_TO.equals(dataMethod)){
                    if(variableDate2 != null){
                        if((inputDate.isAfter(variableDate1) || inputDate.isEqual(variableDate1)) && (inputDate.isBefore(variableDate2) || inputDate.isEqual(variableDate2))){
                            result = true;
                        }
                    }
                }
            }

            return result;
        }
    }
}
