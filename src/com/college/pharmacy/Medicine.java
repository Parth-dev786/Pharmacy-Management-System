package com.college.pharmacy;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public abstract class Medicine implements Expirable, Serializable {
    private static final long serialVersionUID = 1L;
    protected int id;
    protected String name;
    protected String category;
    protected int supplierId;
    protected String batchNo;
    protected int quantity;
    protected double price;
    protected LocalDate expiryDate;
    protected int reorderLevel;

    protected Medicine(int id, String name, int supplierId, String batchNo,
                      int quantity, double price, LocalDate expiryDate, int reorderLevel) {
        this.id=id; this.name=name; this.supplierId=supplierId; this.batchNo=batchNo;
        this.quantity=quantity; this.price=price; this.expiryDate=expiryDate; this.reorderLevel=reorderLevel;
    }
    public abstract String getDosageInformation();
    @Override public boolean isExpired(LocalDate today) { return expiryDate.isBefore(today); }
    @Override public long daysUntilExpiry(LocalDate today) { return ChronoUnit.DAYS.between(today, expiryDate); }
    public void validateForSale() throws ExpiredMedicineException {
        if (isExpired(LocalDate.now())) throw new ExpiredMedicineException(name + " has expired and cannot be sold.");
    }
    public double calculateValue() { return Math.round(quantity * price * 100.0) / 100.0; }
    public int getId(){return id;} public String getName(){return name;} public String getCategory(){return category;}
    public int getSupplierId(){return supplierId;} public String getBatchNo(){return batchNo;}
    public int getQuantity(){return quantity;} public double getPrice(){return price;}
    public LocalDate getExpiryDate(){return expiryDate;} public int getReorderLevel(){return reorderLevel;}
    public void updateDetails(String name, int supplierId, String batchNo, int quantity, double price, LocalDate expiryDate, int reorderLevel) {
        this.name=name; this.supplierId=supplierId; this.batchNo=batchNo; this.quantity=quantity;
        this.price=price; this.expiryDate=expiryDate; this.reorderLevel=reorderLevel;
    }
    public void setQuantity(int quantity) { this.quantity=quantity; }
}
