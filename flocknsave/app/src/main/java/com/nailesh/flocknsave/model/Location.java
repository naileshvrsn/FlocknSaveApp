package com.nailesh.flocknsave.model;

public class Location {

    private String streetNumber;
    private String addressLine1;
    private String addressLine2;
    private String suburb;
    private String city;
    private String postCode;
    private String Region;


    public Location(String streetNumber, String addressLine1, String addressLine2, String suburb, String city, String postCode, String region) {
        this.streetNumber = streetNumber;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.suburb = suburb;
        this.city = city;
        this.postCode = postCode;
        Region = region;
    }

    public Location(String streetNumber, String addressLine1, String suburb, String city, String postCode, String region) {
        this.streetNumber = streetNumber;
        this.addressLine1 = addressLine1;
        this.suburb = suburb;
        this.city = city;
        this.postCode = postCode;
        Region = region;
    }

    public Location(){}

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getRegion() {
        return Region;
    }

    public void setRegion(String region) {
        Region = region;
    }
}
