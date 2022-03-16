package com.app.medview.service;


import com.app.medview.model.Doctor;

public interface DoctorService {
    Doctor saveDoctor(Doctor doctor);
    Doctor getDoctorByLicenseId(String licenseId);

}
