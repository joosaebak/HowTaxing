package com.xmonster.howtaxing.dto.tax;

public class CalculateBuyTaxResponse {

    private final String calcResultId;
    private final String totTaxPrice;
    private final String buyTaxPrice;
    private final String buyPrice;
    private final String buyTaxRate;
    private final String eduTaxPrice;
    private final String deuTaxRate;
    private final String agrTaxPrice;
    private final String agrTaxRate;

    // Test Constructor(GGMANYAR)
    public CalculateBuyTaxResponse(){
        this.calcResultId = "28";
        this.totTaxPrice = "35000000";
        this.buyTaxPrice = "30000000";
        this.buyPrice = "1000000000";
        this.buyTaxRate = "3.00";
        this.eduTaxPrice = "3000000";
        this.deuTaxRate = "0.30";
        this.agrTaxPrice = "2000000";
        this.agrTaxRate = "0.20";
    }

    public CalculateBuyTaxResponse(String calcResultId, String totTaxPrice, String buyTaxPrice, String buyPrice, String buyTaxRate, String eduTaxPrice, String deuTaxRate, String agrTaxPrice, String agrTaxRate) {
        this.calcResultId = calcResultId;
        this.totTaxPrice = totTaxPrice;
        this.buyTaxPrice = buyTaxPrice;
        this.buyPrice = buyPrice;
        this.buyTaxRate = buyTaxRate;
        this.eduTaxPrice = eduTaxPrice;
        this.deuTaxRate = deuTaxRate;
        this.agrTaxPrice = agrTaxPrice;
        this.agrTaxRate = agrTaxRate;
    }

    public String getCalcResultId() {
        return calcResultId;
    }

    public String getTotTaxPrice() {
        return totTaxPrice;
    }

    public String getBuyTaxPrice() {
        return buyTaxPrice;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public String getBuyTaxRate() {
        return buyTaxRate;
    }

    public String getEduTaxPrice() {
        return eduTaxPrice;
    }

    public String getDeuTaxRate() {
        return deuTaxRate;
    }

    public String getAgrTaxPrice() {
        return agrTaxPrice;
    }

    public String getAgrTaxRate() {
        return agrTaxRate;
    }
}
