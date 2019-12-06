package com.nailesh.flocknsave.model;

public class Product {
    private String name;
    private String description;
    private String unit;
    private String savings;
    private String region;
    private String category;
    private String supplierId;
    private String imageLocation;

    public Product(String name, String description, String unit, String savings,
                   String region, String category, String supplierId, String imageLocation) {
        this.name = name;
        this.description = description;
        this.unit = unit;
        this.savings = savings;
        this.region = region;
        this.category = category;
        this.supplierId = supplierId;
        this.imageLocation = imageLocation;
    }

    public Product() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSavings() {
        return savings;
    }

    public void setSavings(String saveings) {
        this.savings = saveings;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getcategory() {
        return category;
    }

    public void setcategory(String category) {
        this.category = category;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }
}
