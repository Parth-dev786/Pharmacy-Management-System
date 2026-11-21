package com.college.pharmacy;
import java.time.LocalDate;
public class Injection extends Medicine {
    private static final long serialVersionUID = 1L;
    public Injection(int id,String name,int supplierId,String batchNo,int quantity,double price,LocalDate expiryDate,int reorderLevel){
        super(id,name,supplierId,batchNo,quantity,price,expiryDate,reorderLevel); category="Injection";
    }
    @Override public String getDosageInformation(){return "Parenteral dosage";}
}
