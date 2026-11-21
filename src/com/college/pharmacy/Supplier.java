package com.college.pharmacy;
import java.io.Serializable;
public class Supplier implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int id; private final String name,phone,email,address;
    public Supplier(int id,String name,String phone,String email,String address){this.id=id;this.name=name;this.phone=phone;this.email=email;this.address=address;}
    public int getId(){return id;} public String getName(){return name;} public String getPhone(){return phone;}
    public String getEmail(){return email;} public String getAddress(){return address;}
}
