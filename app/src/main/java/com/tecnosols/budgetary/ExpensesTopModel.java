package com.tecnosols.budgetary;

public class ExpensesTopModel {
    int date;
    String month;

    public ExpensesTopModel(int date, String month) {
        this.date = date;
        this.month = month;
    }

    public ExpensesTopModel() {
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
