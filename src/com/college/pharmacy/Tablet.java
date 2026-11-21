package com.college.pharmacy;
import java.time.LocalDate;
public class Tablet extends Medicine {
    private static final long serialVersionUID = 1L;
    public Tablet(int id,String name,int supplierId,String batchNo,int quantity,double price,LocalDate expiryDate,int reorderLevel){
        super(id,name,supplierId,batchNo,quantity,price,expiryDate,reorderLevel); category="Tablet";
    }
    @Override public String getDosageInformation(){return "Oral solid dosage";}
}
