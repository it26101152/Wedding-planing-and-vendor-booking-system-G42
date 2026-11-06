package com.weddingplanner.vendor.model;
import com.weddingplanner.common.model.BaseEntity;
public class Vendor extends BaseEntity
{
    private String name="";
    public String getName()
    {
        return name;
    }
    public void setName(String v)
    {
        this.name=v==null?"":v;
    }
    private String category="";
    public String getCategory()
    {
        return category;
    }
    public void setCategory(String v)
    {
        this.category=v==null?"":v;
    }
    private String price="";
    public String getPrice()
    {
        return price;
    }
    public void setPrice(String v)
    {
        this.price=v==null?"":v;
    }
    private String description="";
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String v)
    {
        this.description=v==null?"":v;
    }
    @Override public String[] toFields()
    {
        return new String[]
        {
            name,category,price,description
        }
        ;
    }
}
