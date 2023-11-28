package com.xmonster.howtaxing.controller.tax;

import com.xmonster.howtaxing.service.tax.CalculateTaxService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CalculateTaxController {

    private final CalculateTaxService calculateTaxService;

    public CalculateTaxController(CalculateTaxService calculateTaxService){
        this.calculateTaxService = calculateTaxService;
    }

    @PostMapping("/calculate/buyTax")
    public Map<String, Object> calculateBuyTax(@RequestBody Map<String, Object> requestMap){
        return calculateTaxService.calculateBuyTax(requestMap);
    }

    @PostMapping("/calculate/sellTax")
    public Map<String, Object> calculateSellTax(@RequestBody Map<String, Object> requestMap){
        return calculateTaxService.calculateSellTax(requestMap);
    }
}
