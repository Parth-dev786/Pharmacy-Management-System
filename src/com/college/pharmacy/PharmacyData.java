package com.college.pharmacy;
import java.io.Serializable;
import java.util.ArrayList;
public class PharmacyData implements Serializable {
    private static final long serialVersionUID = 1L;
    final ArrayList<Medicine> medicines = new ArrayList<>();
    final ArrayList<Supplier> suppliers = new ArrayList<>();
    final ArrayList<Sale> sales = new ArrayList<>();
    int nextMedicineId=1, nextSupplierId=1, nextSaleId=1;
}
