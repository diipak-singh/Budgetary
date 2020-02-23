package com.tecnosols.budgetary;

public class ExpenseDetail {
    String expName,expDesc,expCurr,expAmount,expId;

    public ExpenseDetail() {
    }

    public ExpenseDetail(String expName, String expDesc, String expCurr, String expAmount, String expId) {
        this.expName = expName;
        this.expDesc = expDesc;
        this.expCurr = expCurr;
        this.expAmount = expAmount;
        this.expId = expId;
    }

    public String getExpName() {
        return expName;
    }

    public void setExpName(String expName) {
        this.expName = expName;
    }

    public String getExpDesc() {
        return expDesc;
    }

    public void setExpDesc(String expDesc) {
        this.expDesc = expDesc;
    }

    public String getExpCurr() {
        return expCurr;
    }

    public void setExpCurr(String expCurr) {
        this.expCurr = expCurr;
    }

    public String getExpAmount() {
        return expAmount;
    }

    public void setExpAmount(String expAmount) {
        this.expAmount = expAmount;
    }

    public String getExpId() {
        return expId;
    }

    public void setExpId(String expId) {
        this.expId = expId;
    }
}
