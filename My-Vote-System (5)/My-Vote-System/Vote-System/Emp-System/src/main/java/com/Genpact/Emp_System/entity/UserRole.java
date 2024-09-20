package com.Genpact.Emp_System.entity;

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
