package com.app.medview.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "doctor")
public class Doctor implements Serializable {
    @Id
    @GenericGenerator(name = "doc_uuid",strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "doc_uuid")
    @Type(type = "uuid-char")
    @Column(name = "id")
    private UUID id;

    @Column(unique = true,nullable = false)
    private String licenseId;
    @Column(nullable = false)
    private String currentHospital;
    @Column(nullable = false)
    private String specialisation;

    private final GrantedAuthority role = () -> "DOCTOR";


    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", licenseId='" + licenseId + '\'' +
                ", currentHospital='" + currentHospital + '\'' +
                ", specialisation='" + specialisation + '\'' +
                ", role=" + role +
                ", password='" + password + '\'' +
                '}';
    }

    private String password;

    public Doctor() {
    }

    public Doctor(UUID id, String licenseId, String currentHospital, String specialisation, String password) {
        this.id = id;
        this.licenseId = licenseId;
        this.currentHospital = currentHospital;
        this.specialisation = specialisation;
        this.password = password;
    }

    public GrantedAuthority getRole() {
        return role;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public String getCurrentHospital() {
        return currentHospital;
    }

    public void setCurrentHospital(String currentHospital) {
        this.currentHospital = currentHospital;
    }

    public String getSpecialisation() {
        return specialisation;
    }

    public void setSpecialisation(String specialisation) {
        this.specialisation = specialisation;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
