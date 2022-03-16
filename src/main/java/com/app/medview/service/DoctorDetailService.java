package com.app.medview.service;

import com.app.medview.model.Doctor;
import com.app.medview.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Service @RequiredArgsConstructor
public class DoctorDetailService implements UserDetailsService {
    private final DoctorRepository doctorRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Doctor> doctorOptional = Optional.ofNullable(doctorRepository.getDoctorByLicenseId(username));
        if(doctorOptional.isPresent()){
            Doctor doctor =doctorOptional.get();
            return new User(doctor.getLicenseId(),doctor.getPassword(),new ArrayList<>(Arrays.asList(doctor.getRole())));
        }else{
            throw new UsernameNotFoundException("Doctor not found!");
        }

    }
}
