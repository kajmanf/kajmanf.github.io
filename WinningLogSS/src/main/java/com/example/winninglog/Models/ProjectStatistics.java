package com.example.winninglog.Models;

public class ProjectStatistics {

    private String projectName;
    private float projekt;
    private float korrekturIn;
    private float korrekturExtMistake;
    private float korrekturExtClient;
    private float freigabe;
    private float mehrarbeit;
    private float stundenSum;

    public ProjectStatistics() {
    }

    public ProjectStatistics(String projectName, float projekt, float korrekturIn, float korrekturExtMistake, float korrekturExtClient, float freigabe, float mehrarbeit) {
        this.projectName = projectName;
        this.projekt = projekt;
        this.korrekturIn = korrekturIn;
        this.korrekturExtMistake = korrekturExtMistake;
        this.korrekturExtClient = korrekturExtClient;
        this.freigabe = freigabe;
        this.mehrarbeit = mehrarbeit;
        this.stundenSum = korrekturIn + korrekturExtClient + korrekturExtMistake + freigabe + mehrarbeit + projekt;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public float getProjekt() {
        return projekt;
    }

    public void setProjekt(float projekt) {
        this.projekt = projekt;
    }

    public float getKorrekturIn() {
        return korrekturIn;
    }

    public void setKorrekturIn(float korrekturIn) {
        this.korrekturIn = korrekturIn;
    }

    public float getKorrekturExtMistake() {
        return korrekturExtMistake;
    }

    public void setKorrekturExtMistake(float korrekturExtMistake) {
        this.korrekturExtMistake = korrekturExtMistake;
    }

    public float getKorrekturExtClient() {
        return korrekturExtClient;
    }

    public void setKorrekturExtClient(float korrekturExtClient) {
        this.korrekturExtClient = korrekturExtClient;
    }

    public float getFreigabe() {
        return freigabe;
    }

    public void setFreigabe(float freigabe) {
        this.freigabe = freigabe;
    }

    public float getMehrarbeit() {
        return mehrarbeit;
    }

    public void setMehrarbeit(float mehrarbeit) {
        this.mehrarbeit = mehrarbeit;
    }

    public float getStundenSum() {
        return stundenSum;
    }

    public void setStundenSum(float stundenSum) {
        this.stundenSum = stundenSum;
    }
}
