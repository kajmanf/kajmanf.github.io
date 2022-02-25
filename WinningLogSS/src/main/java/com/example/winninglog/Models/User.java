package com.example.winninglog.Models;

import com.example.winninglog.Repositories.ProjectRepository;
import com.example.winninglog.Services.ProjectService;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private String password;

    private String retrieval;

    private String roles;
    private Boolean active;

    private String email;
    private int employee;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "users")
    private Set<Project> projects = new HashSet<>();

    public User() {
    }

    public User(String userName, String password, String roles, int employee) {
        this.userName = userName;
        this.password = password;
        this.roles = roles;
        this.active = true;
        this.email = userName + "@be-winning.com";
        this.employee = employee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getEmployee() {
        return employee;
    }

    public void setEmployee(int employee) {
        this.employee = employee;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public String getRetrieval() {
        return retrieval;
    }

    public void setRetrieval(String retrieval) {
        this.retrieval = retrieval;
    }

    @PreRemove
    private void removeProjectFromUser(){
        this.getProjects().clear();
    }

}
