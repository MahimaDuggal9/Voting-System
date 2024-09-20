package com.Genpact.Emp_System.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
@Entity
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")

    private LocalDate dateOfBirth;
    private String nationality;
    private String partyName;
    private String partyLogo;
    private int numberOfVotes;

    public Candidate() {}

    public Candidate(String fullName, LocalDate dateOfBirth, String nationality, String partyName, String partyLogo) {
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.partyName = partyName;
        this.partyLogo = partyLogo;
        this.numberOfVotes = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getPartyLogo() {
        return partyLogo;
    }

    public void setPartyLogo(String partyLogo) {
        this.partyLogo = partyLogo;
    }

    public int getNumberOfVotes() {
        return numberOfVotes;
    }

    public void setNumberOfVotes(int numberOfVotes) {
        this.numberOfVotes = numberOfVotes;
    }

    @Override
    public String toString() {
        return "Candidate [id=" + id + ", fullName=" + fullName + ", dateOfBirth=" + dateOfBirth + ", nationality="
                + nationality + ", partyName=" + partyName + ", partyLogo=" + partyLogo + ", numberOfVotes="
                + numberOfVotes + "]";
    }
}
