package com.app.medview.repository;

import com.app.medview.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
    Doctor getDoctorByLicenseId(String licenseId);
}
