package com.weddingplanner.wedding.model;
import com.weddingplanner.common.model.BaseEntity;
public class Wedding extends BaseEntity
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
    private String title="";
    public String getTitle()
    {
        return title;
    }
    public void setTitle(String v)
    {
        this.title=v==null?"":v;
    }
    private String eventDate="";
    public String getEventDate()
    {
        return eventDate;
    }
    public void setEventDate(String v)
    {
        this.eventDate=v==null?"":v;
    }
    private String venue="";
    public String getVenue()
    {
        return venue;
    }
    public void setVenue(String v)
    {
        this.venue=v==null?"":v;
    }
    @Override public String[] toFields()
    {
        return new String[]
        {
            customerId,title,eventDate,venue
        }
        ;
    }
}
