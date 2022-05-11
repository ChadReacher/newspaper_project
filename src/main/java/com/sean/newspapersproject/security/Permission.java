package com.sean.newspapersproject.security;

public enum Permission {
    USER_WRITE("user:write"),
    USER_READ("user:read"),
    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write");


    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
