package com.college.pharmacy;
import java.io.Serializable;
import java.time.LocalDateTime;
public class Sale implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int id, medicineId, quantity; private final String customerName, medicineName;
    private final double unitPrice,total; private final LocalDateTime date;
    public Sale(int id,int medicineId,int quantity,String customerName,String medicineName,double unitPrice){
        this.id=id;this.medicineId=medicineId;this.quantity=quantity;this.customerName=customerName;this.medicineName=medicineName;
        this.unitPrice=unitPrice;this.total=Math.round(unitPrice*quantity*100.0)/100.0;this.date=LocalDateTime.now();
    }
    public int getId(){return id;} public int getMedicineId(){return medicineId;} public int getQuantity(){return quantity;}
    public String getCustomerName(){return customerName;} public String getMedicineName(){return medicineName;}
    public double getUnitPrice(){return unitPrice;} public double getTotal(){return total;} public LocalDateTime getDate(){return date;}
}
