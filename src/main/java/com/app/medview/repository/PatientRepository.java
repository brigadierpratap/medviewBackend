package com.app.medview.repository;

import com.app.medview.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
    Patient getPatientByUid(String uid);
    Patient getPatientByPhoneNum(String phoneNum);

}
