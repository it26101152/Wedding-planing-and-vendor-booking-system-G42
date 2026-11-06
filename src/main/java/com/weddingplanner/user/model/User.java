package com.weddingplanner.user.model;
import com.weddingplanner.common.model.BaseEntity;
public class User extends BaseEntity
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
    private String email="";
    public String getEmail()
    {
        return email;
    }
    public void setEmail(String v)
    {
        this.email=v==null?"":v;
    }
    private String passwordHash="";
    public String getPasswordHash()
    {
        return passwordHash;
    }
    public void setPasswordHash(String v)
    {
        this.passwordHash=v==null?"":v;
    }
    private String role="";
    public String getRole()
    {
        return role;
    }
    public void setRole(String v)
    {
        this.role=v==null?"":v;
    }
    @Override public String[] toFields()
    {
        return new String[]
        {
            name,email,passwordHash,role
        }
        ;
    }
}
