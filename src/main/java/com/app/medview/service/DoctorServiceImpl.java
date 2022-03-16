package com.app.medview.service;

import com.app.medview.model.Doctor;
import com.app.medview.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service@Transactional@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService{
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    @Override
    public Doctor saveDoctor(Doctor doctor) {
        String encodedPassword = bCryptPasswordEncoder.encode(doctor.getPassword());
        doctor.setPassword(encodedPassword);
        return doctorRepository.save(doctor);
    }

    @Override
    public Doctor getDoctorByLicenseId(String licenseId) {
        return doctorRepository.getDoctorByLicenseId(licenseId);
    }
    public boolean checkIfPresent(Doctor doctor){
        Optional<Doctor> doctor1 = Optional.ofNullable(doctorRepository.getDoctorByLicenseId(doctor.getLicenseId()));
        return doctor1.isEmpty();
    }
}
