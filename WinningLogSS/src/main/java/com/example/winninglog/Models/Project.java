package com.example.winninglog.Models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customer;
    private String projectName;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "project_user",
        joinColumns = {@JoinColumn(name = "project_id")},
        inverseJoinColumns = { @JoinColumn(name = "user_id")})
    private Set<User> users = new HashSet<>();

    public Project() {
    }

    public Project(Long id, String customer, String projectName) {
        this.id = id;
        this.customer = customer;
        this.projectName = projectName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }



    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(User user) {
        this.users.add(user);
    }

    public String getCustomerPlusName(){
        return this.customer + " - " + this.projectName;
    }


    @PreRemove
    private void removeProjectFromUser(){
        /*Set<User> usersOfProject = this.getUsers();
        for (User user : usersOfProject) {
            user.getProjects().remove(this);
        }*/
        this.getUsers().clear();
    }

}
