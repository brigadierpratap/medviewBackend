package com.app.medview.util;

import com.app.medview.model.Document;
import com.app.medview.model.Illness;
import com.app.medview.model.Patient;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ResponsePatient {

    private UUID id;
    private String patientFirstName;
    private String patientLastName;
    private String uid;

    private String phoneNum;



    private Set<Illness> pastIllnesses=new HashSet<>();

    private Set<Document> documents=new HashSet<>();
    private Date dateOfBirth;
    private int age;
    private char sex = 'M';
    private float height;

    public ResponsePatient(Patient patient){
        this.age=patient.getAge();
        this.dateOfBirth=patient.getDateOfBirth();
        this.documents = patient.getDocuments();
        this.height = patient.getHeight();
        this.pastIllnesses = patient.getPastIllnesses();
        this.id=patient.getId();
        this.patientFirstName=patient.getPatientFirstName();
        this.patientLastName = patient.getPatientLastName();
        this.phoneNum=patient.getPhoneNum();
        this.sex = patient.getSex();
        this.uid = patient.getUid();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPatientFirstName() {
        return patientFirstName;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    public String getPatientLastName() {
        return patientLastName;
    }

    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Set<Illness> getPastIllnesses() {
        return pastIllnesses;
    }

    public void setPastIllnesses(Set<Illness> pastIllnesses) {
        this.pastIllnesses = pastIllnesses;
    }

    public Set<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
