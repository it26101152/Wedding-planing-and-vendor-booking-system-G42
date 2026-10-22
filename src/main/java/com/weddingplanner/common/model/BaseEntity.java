package com.weddingplanner.common.model;

import java.time.LocalDateTime;

/**
 * Parent of every entity (User, Vendor, Wedding, Booking, Payment, Guest).
 * Demonstrates ENCAPSULATION (private fields), INHERITANCE (subclasses extend it)
 * and POLYMORPHISM (each subclass overrides toFields()).
 *
 * id and createdAt are written/read by FileRepository, so subclasses only
 * handle their OWN fields.
 */
public abstract class BaseEntity {

    private String id;
    private LocalDateTime createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Convert this object's own fields (NOT id/createdAt) to Strings, in the exact
     * order your repository's fromFields() reads them back.
     */
    public abstract String[] toFields();
}
