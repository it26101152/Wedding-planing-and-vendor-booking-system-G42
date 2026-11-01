package com.weddingplanner.customer.model;
import com.weddingplanner.common.model.BaseEntity;
public class PendingCustomer extends BaseEntity {
 private String name="",email="",phone="";
 public String getName(){return name;} public void setName(String s){name=s==null?"":s;}
 public String getEmail(){return email;} public void setEmail(String s){email=s==null?"":s;}
 public String getPhone(){return phone;} public void setPhone(String s){phone=s==null?"":s;}
 @Override public String[] toFields(){return new String[]{name,email,phone};}
}
