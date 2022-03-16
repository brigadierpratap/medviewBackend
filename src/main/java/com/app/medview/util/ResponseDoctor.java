package com.app.medview.util;

import com.app.medview.model.Doctor;

import javax.persistence.Column;
import java.util.UUID;

public class ResponseDoctor {
    private UUID id;

    private String licenseId;
    private String currentHospital;
    private String specialisation;

    public ResponseDoctor(Doctor doctor){
        this.id = doctor.getId();
        this.currentHospital = doctor.getCurrentHospital();
        this.licenseId = doctor.getLicenseId();
        this.specialisation = doctor.getSpecialisation();
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
}
