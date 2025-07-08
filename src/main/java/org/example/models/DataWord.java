package org.example.models;

public class DataWord {
    String date;
    String invoices;
    String hours;
    String amount;
    String cost;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInvoices() {
        return invoices;
    }

    public void setInvoices(String invoices) {
        this.invoices = invoices;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public DataWord(String date, String invoices, String hours, String amount, String cost){
        this.cost=cost;
        this.date=date;
        this.hours=hours;
        this.amount=amount;
        this.invoices=invoices;
    }
}
