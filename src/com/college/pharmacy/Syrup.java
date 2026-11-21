package com.college.pharmacy;
import java.time.LocalDate;
public class Syrup extends Medicine {
    private static final long serialVersionUID = 1L;
    public Syrup(int id,String name,int supplierId,String batchNo,int quantity,double price,LocalDate expiryDate,int reorderLevel){
        super(id,name,supplierId,batchNo,quantity,price,expiryDate,reorderLevel); category="Syrup";
    }
    @Override public String getDosageInformation(){return "Oral liquid dosage";}
}
