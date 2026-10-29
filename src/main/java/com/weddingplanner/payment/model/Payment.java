package com.weddingplanner.payment.model;
import com.weddingplanner.common.model.BaseEntity;
public class Payment extends BaseEntity
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
    private String bookingId="";
    public String getBookingId()
    {
        return bookingId;
    }
    public void setBookingId(String v)
    {
        this.bookingId=v==null?"":v;
    }
    private String amount="";
    public String getAmount()
    {
        return amount;
    }
    public void setAmount(String v)
    {
        this.amount=v==null?"":v;
    }
    private String status="";
    public String getStatus()
    {
        return status;
    }
    public void setStatus(String v)
    {
        this.status=v==null?"":v;
    }
    @Override public String[] toFields()
    {
        return new String[]
                {
                        customerId,bookingId,amount,status
                }
                ;
    }
}

