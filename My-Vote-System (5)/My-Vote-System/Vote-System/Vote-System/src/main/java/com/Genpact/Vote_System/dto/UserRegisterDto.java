package com.Genpact.Vote_System.dto;


import com.Genpact.Vote_System.entity.UserRole;

import javax.validation.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserRegisterDto {
    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String password;

    @NotEmpty
    @Pattern(regexp = "^[0-9]{12}$", message = "Aadhaar number must be exactly 12 digits")
    private String aadharNumber;

    @NotEmpty
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 12 digits")
    private String phoneNumber;

    @NotEmpty
    private String nationality;

    @NotEmpty
    private String dateOfBirth;

    @NotNull
    private UserRole role;



    public UserRegisterDto() {
    }



    public UserRegisterDto(String firstName, String lastName, String password, String aadharNumber, String phoneNumber, String nationality, String dateOfBirth, UserRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.aadharNumber = aadharNumber;
        this.phoneNumber = phoneNumber;
        this.nationality = nationality;
        this.dateOfBirth = dateOfBirth;
        this.role = role;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }


}
