package com.college.pharmacy;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

public class DataStore {
    private final Path dataFile = Paths.get("pharmacy-data.ser").toAbsolutePath();
    private PharmacyData data;
    public DataStore() { load(); }
    private synchronized void load() {
        if (Files.exists(dataFile)) {
            try (ObjectInputStream in=new ObjectInputStream(Files.newInputStream(dataFile))) {
                Object obj=in.readObject(); if(obj instanceof PharmacyData loaded) data=loaded;
            } catch(Exception ex) { throw new IllegalStateException("Could not read " + dataFile + ": " + ex.getMessage(),ex); }
        }
        if(data==null) data=new PharmacyData();
    }
    private synchronized void save() {
        try (ObjectOutputStream out=new ObjectOutputStream(Files.newOutputStream(dataFile))) { out.writeObject(data); }
        catch(IOException ex) { throw new IllegalStateException("Could not save data: " + ex.getMessage(),ex); }
    }
    public synchronized List<Medicine> getMedicines(){return new ArrayList<>(data.medicines);}
    public synchronized List<Supplier> getSuppliers(){return new ArrayList<>(data.suppliers);}
    public synchronized List<Sale> getSales(){return new ArrayList<>(data.sales);}
    public synchronized Medicine findMedicine(int id){return data.medicines.stream().filter(m->m.getId()==id).findFirst().orElse(null);}
    public synchronized int addMedicine(String category,String name,int supplierId,String batch,int qty,double price,LocalDate expiry,int reorder){
        int id=data.nextMedicineId++;
        Medicine m=switch(category){
            case "Tablet" -> new Tablet(id,name,supplierId,batch,qty,price,expiry,reorder);
            case "Syrup" -> new Syrup(id,name,supplierId,batch,qty,price,expiry,reorder);
            case "Injection" -> new Injection(id,name,supplierId,batch,qty,price,expiry,reorder);
            default -> throw new IllegalArgumentException("Choose Tablet, Syrup or Injection.");
        };
        data.medicines.add(m);save();return id;
    }
    public synchronized void updateMedicine(int id,String name,int supplierId,String batch,int qty,double price,LocalDate expiry,int reorder){
        Medicine m=findMedicine(id);if(m==null)throw new IllegalArgumentException("Medicine not found.");
        m.updateDetails(name,supplierId,batch,qty,price,expiry,reorder);save();
    }
    public synchronized void deleteMedicine(int id){
        boolean used=data.sales.stream().anyMatch(s->s.getMedicineId()==id);
        if(used)throw new IllegalArgumentException("This medicine has sales records and cannot be deleted.");
        data.medicines.removeIf(m->m.getId()==id);save();
    }
    public synchronized int addSupplier(String name,String phone,String email,String address){
        int id=data.nextSupplierId++;data.suppliers.add(new Supplier(id,name,phone,email,address));save();return id;
    }
    public synchronized void createSale(int medicineId,int qty,String customer) throws ExpiredMedicineException {
        if(qty<=0)throw new IllegalArgumentException("Quantity must be greater than zero.");
        Medicine m=findMedicine(medicineId);if(m==null)throw new IllegalArgumentException("Medicine ID not found.");
        m.validateForSale();if(qty>m.getQuantity())throw new IllegalArgumentException("Insufficient stock. Available: "+m.getQuantity());
        Sale sale=new Sale(data.nextSaleId++,medicineId,qty,customer,m.getName(),m.getPrice());
        m.setQuantity(m.getQuantity()-qty);data.sales.add(sale);save();
    }
    public synchronized List<Medicine> getExpiryItems(){
        LocalDate today=LocalDate.now();List<Medicine> result=new ArrayList<>();
        for(Medicine m:data.medicines) if(!m.getExpiryDate().isAfter(today.plusDays(30)))result.add(m);
        result.sort(Comparator.comparing(Medicine::getExpiryDate));return result;
    }
    public synchronized Path getDataFile(){return dataFile;}
}
