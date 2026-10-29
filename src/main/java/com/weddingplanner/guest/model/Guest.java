package com.weddingplanner.guest.model;
import com.weddingplanner.common.model.BaseEntity;
public class Guest extends BaseEntity
{
    private String customerId="";
    public String getCustomerId()
    {
        return customerId;
    }
    public void setCustomerId(String v)
    {
        this.customerId=v==null?"":v;
    }
    private String name="";
    public String getName()
    {
        return name;
    }
    public void setName(String v)
    {
        this.name=v==null?"":v;
    }
    private String email="";
    public String getEmail()
    {
        return email;
    }
    public void setEmail(String v)
    {
        this.email=v==null?"":v;
    }
    private String rsvp="";
    public String getRsvp()
    {
        return rsvp;
    }
    public void setRsvp(String v)
    {
        this.rsvp=v==null?"":v;
    }
    @Override public String[] toFields()
    {
        return new String[]
        {
            customerId,name,email,rsvp
        }
        ;
    }
}
