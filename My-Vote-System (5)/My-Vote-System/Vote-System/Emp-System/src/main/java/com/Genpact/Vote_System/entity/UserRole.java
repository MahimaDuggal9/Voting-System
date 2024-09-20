package com.Genpact.Vote_System.entity;

public enum UserRole {
    USER, ADMIN;

    @Override
    public String toString() {
        return name();
    }

    public String toUpperCase() {
        return this.name();
    }
}
