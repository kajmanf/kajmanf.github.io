package com.example.winninglog.Models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "entitys")
public class LogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private Project project;
    @ManyToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;

    private String date;
    private String gebaude;
    private String niveau;
    private String planType;
    private String planName;
    private String planIndex;
    private String arbeitsart;
    private float stunden;

    public LogEntity() {
    }

    public LogEntity(Long id, Project project, User user, String date, String gebaude, String niveau, String planType, String planName, String planIndex, String arbeitsart, float stunden) {
        this.id = id;
        this.project = project;
        this.user = user;
        this.date = date;
        this.gebaude = gebaude;
        this.niveau = niveau;
        this.planType = planType;
        this.planName = planName;
        this.planIndex = planIndex;
        this.arbeitsart = arbeitsart;
        this.stunden = stunden;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGebaude() {
        return gebaude;
    }

    public void setGebaude(String gebaude) {
        this.gebaude = gebaude;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getPlanIndex() {
        return planIndex;
    }

    public void setPlanIndex(String planIndex) {
        this.planIndex = planIndex;
    }

    public String getArbeitsart() {
        return arbeitsart;
    }

    public void setArbeitsart(String arbeitsart) {
        this.arbeitsart = arbeitsart;
    }

    public float getStunden() {
        return stunden;
    }

    public void setStunden(float stunden) {
        this.stunden = stunden;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        String jsonString = "{id:"+ this.getId() +
                            ", project:" + this.getProject().getCustomerPlusName() +
                            ", user:" + this.getUser().getUserName() +
                            ", date:" + this.getDate() +
                            ", gebaude:" + this.getGebaude() +
                            ", niveau:" + this.getNiveau() +
                            ", plantype:" + this.getPlanType() +
                            ", planname:" + this.getPlanName() +
                            ", planindex:" + this.getPlanIndex() +
                            ", arbeitsart:" + this.getArbeitsart() +
                            ", stunden:" + this.getStunden() + "}";
        return jsonString;
    }

    @PreRemove
    private void removeProjectFromUser(){
        this.user = null;
        this.project = null;
    }
}


