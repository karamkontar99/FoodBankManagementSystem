package com.mobile.foodbank.models;

public class User {
    private String name;
    private String dob;
    private String email;
    private String username;
    private String password;
    private String role;
    private String securityAns;

    public User() {
    }

    public User(String name, String dob, String email, String username, String password, String role, String securityAns) {
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.securityAns = securityAns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSecurityAns() {
        return securityAns;
    }

    public void setSecurityAns(String securityAns) {
        this.securityAns = securityAns;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", dob='" + dob + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", securityAns='" + securityAns + '\'' +
                '}';
    }
}
