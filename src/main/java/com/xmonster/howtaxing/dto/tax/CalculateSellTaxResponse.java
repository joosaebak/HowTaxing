package com.xmonster.howtaxing.dto.tax;

public class CalculateSellTaxResponse {

    private String calcResultId;
    private String totTaxPrice;
    private String sellTaxPrice;
    private String lclTaxPrice;
    private String sellPrice;
    private String buyPrice;
    private String necExpense;
    private String sellDiffPrice;
    private String nonTaxPrice;
    private String taxPrice;
    private String longTermPrice;
    private String sellGainPrice;
    private String basicDeducPrice;
    private String taxBasePrice;
    private String taxRate;
    private String progPrice;

    // Test Constructor(GGMANYAR)
    public CalculateSellTaxResponse(){
        this.calcResultId = "31";
        this.totTaxPrice = "425188500";
        this.sellTaxPrice = "386535000";
        this.lclTaxPrice = "38653500";
        this.sellPrice = "1800000000";
        this.buyPrice = "500000000";
        this.necExpense = "100000000";
        this.sellDiffPrice = "1200000000";
        this.nonTaxPrice = "";
        this.taxPrice = "1200000000";
        this.longTermPrice = "192000000";
        this.sellGainPrice = "1008000000";
        this.basicDeducPrice = "2500000";
        this.taxBasePrice = "1005500000";
        this.taxRate = "45";
        this.progPrice = "65940000";
    }

    public CalculateSellTaxResponse(String calcResultId, String totTaxPrice, String sellTaxPrice, String lclTaxPrice, String sellPrice, String buyPrice, String necExpense, String sellDiffPrice, String nonTaxPrice, String taxPrice, String longTermPrice, String sellGainPrice, String basicDeducPrice, String taxBasePrice, String taxRate, String progPrice) {
        this.calcResultId = calcResultId;
        this.totTaxPrice = totTaxPrice;
        this.sellTaxPrice = sellTaxPrice;
        this.lclTaxPrice = lclTaxPrice;
        this.sellPrice = sellPrice;
        this.buyPrice = buyPrice;
        this.necExpense = necExpense;
        this.sellDiffPrice = sellDiffPrice;
        this.nonTaxPrice = nonTaxPrice;
        this.taxPrice = taxPrice;
        this.longTermPrice = longTermPrice;
        this.sellGainPrice = sellGainPrice;
        this.basicDeducPrice = basicDeducPrice;
        this.taxBasePrice = taxBasePrice;
        this.taxRate = taxRate;
        this.progPrice = progPrice;
    }

    public String getCalcResultId() {
        return calcResultId;
    }

    public String getTotTaxPrice() {
        return totTaxPrice;
    }

    public String getSellTaxPrice() {
        return sellTaxPrice;
    }

    public String getLclTaxPrice() {
        return lclTaxPrice;
    }

    public String getSellPrice() {
        return sellPrice;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public String getNecExpense() {
        return necExpense;
    }

    public String getSellDiffPrice() {
        return sellDiffPrice;
    }

    public String getNonTaxPrice() {
        return nonTaxPrice;
    }

    public String getTaxPrice() {
        return taxPrice;
    }

    public String getLongTermPrice() {
        return longTermPrice;
    }

    public String getSellGainPrice() {
        return sellGainPrice;
    }

    public String getBasicDeducPrice() {
        return basicDeducPrice;
    }

    public String getTaxBasePrice() {
        return taxBasePrice;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public String getProgPrice() {
        return progPrice;
    }
}
