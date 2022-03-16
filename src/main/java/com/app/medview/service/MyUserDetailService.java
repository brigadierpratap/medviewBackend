package com.app.medview.service;

import com.app.medview.model.Patient;
import com.app.medview.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor@Slf4j
public class MyUserDetailService implements UserDetailsService {
    private final PatientRepository patientRepository;
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        Optional<Patient> patient = Optional.ofNullable(patientRepository.getPatientByPhoneNum(userName));
        if (patient.isEmpty()){
            log.warn("here");
            throw new UsernameNotFoundException("User not found");
        }
        else{
            log.warn(patient.get().toString());
            return new User(patient.get().getPhoneNum(),patient.get().getPassword(),new ArrayList<>(Arrays.asList(patient.get().getRole())));
        }

    }

}
