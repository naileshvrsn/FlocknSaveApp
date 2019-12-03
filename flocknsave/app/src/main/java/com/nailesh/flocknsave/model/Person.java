package com.nailesh.flocknsave.model;

import java.util.PriorityQueue;

public class Person {
    private String firstName;
    private String lastName;
    private String businessName;
    private String phoneNumber;
    private String streetAddress;
    private String suburb;
    private String postCode;
    private String region;
    private String industry;
    private String personType;

    public Person() {
    }

    public Person(String firstName, String lastName, String businessName, String phoneNumber, String streetAddress,
                  String suburb, String postCode, String region, String industry, String personType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.businessName = businessName;
        this.phoneNumber = phoneNumber;
        this.streetAddress = streetAddress;
        this.suburb = suburb;
        this.postCode = postCode;
        this.region = region;
        this.industry = industry;

        this.personType = personType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
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

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }
}
