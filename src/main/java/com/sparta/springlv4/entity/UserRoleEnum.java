package com.sparta.springlv4.entity;

public enum UserRoleEnum {
    USER("USER"),
    ADMIN("ADMIN");

    private final String role;

    private UserRoleEnum(String role) {
        this.role = role;
    }

    public String getRole() {
        return this.role;
    }
}
