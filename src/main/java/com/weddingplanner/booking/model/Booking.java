package com.weddingplanner.booking.model;

public class Booking {package com.weddingplanner.booking.model;
import com.weddingplanner.common.model.BaseEntity;
    public class Booking extends BaseEntity
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
        private String vendorId="";
        public String getVendorId()
        {
            return vendorId;
        }
        public void setVendorId(String v)
        {
            this.vendorId=v==null?"":v;
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
                            customerId,vendorId,eventDate,status
                    }
                    ;
        }
    }

}
