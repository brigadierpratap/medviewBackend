package com.app.medview.model;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Entity
@Table(name = "patients")@Slf4j
public class Patient implements Serializable {
    @Id
    @GenericGenerator(name="uuid",strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "uuid")
    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;
    private String patientFirstName;
    private String patientLastName;
    @Column(unique = true,nullable = false)
    private String uid;
    @Column(unique = true,nullable = false)
    private String phoneNum;



    @OneToMany(cascade = CascadeType.ALL,targetEntity = Illness.class,fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Set<Illness> pastIllnesses=new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL,targetEntity = Document.class,fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Set<Document> documents=new HashSet<>();
    private String password;
    private Date dateOfBirth;
    private int age;
    private char sex = 'M';
    private float height;
    private final GrantedAuthority role = () -> "PATIENT";

    public GrantedAuthority getRole() {
        return role;
    }



    public Patient() {
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", patientFirstName='" + patientFirstName + '\'' +
                ", patientLastName='" + patientLastName + '\'' +
                ", uid='" + uid + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", pastIllnesses=" + pastIllnesses +
                ", documents=" + documents +
                ", password='" + password + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", age=" + age +
                ", sex=" + sex +
                ", height=" + height +
                '}';
    }

    public Patient(UUID id, String patientFirstName, String patientLastName, String uid, String phoneNum, Set<Illness> pastIllnesses, Set<Document> documents, String password, String dateOfBirth, int age, char sex, float height) throws ParseException {
        this.id = id;
        this.patientFirstName = patientFirstName;
        this.patientLastName = patientLastName;
        this.uid = uid;
        this.phoneNum = phoneNum;
        this.pastIllnesses = pastIllnesses;
        this.documents = documents;
        this.password = password;

        this.dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse(dateOfBirth);
        this.age = age;
        this.sex = sex;
        this.height = height;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
