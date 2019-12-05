package com.nailesh.flocknsave.model;

public class Product {
    private String name;
    private String decription;
    private String unit;
    private String savings;
    private String region;
    private String industry;
    private String supplierId;
    private String imageLocation;

    public Product(String name, String decription, String unit, String savings,
                   String region, String industry, String supplierId, String imageLocation) {
        this.name = name;
        this.decription = decription;
        this.unit = unit;
        this.savings = savings;
        this.region = region;
        this.industry = industry;
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

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
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

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
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
