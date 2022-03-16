package com.app.medview.service;

import com.app.medview.exception.ResourceNotFoundException;
import com.app.medview.model.Document;
import com.app.medview.model.Illness;
import com.app.medview.model.Patient;
import com.app.medview.repository.DocumentRepository;
import com.app.medview.repository.IllnessRepository;
import com.app.medview.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service @Transactional @Slf4j@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final IllnessRepository illnessRepository;
    private final DocumentRepository documentRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Override
    public Patient getPatientByPhone(String phone) {
        log.info(phone);
        return patientRepository.getPatientByPhoneNum(phone);
    }

    @Override
    public Patient getPatientByUid(String uid) {
        return patientRepository.getPatientByUid(uid);
    }

    @Override
    public Patient savePatient(Patient patient) {
        String encodedPassword = passwordEncoder.encode(patient.getPassword());
        patient.setPassword(encodedPassword);
        return patientRepository.save(patient);
    }




    @Override
    public List<Illness> getUserIllness(String uid, boolean isUid) {
        Patient patient;
        if(isUid){
            patient = patientRepository.getPatientByUid(uid);
        }else{
            patient = patientRepository.getPatientByPhoneNum(uid);
        }
        if(patient==null){
            throw new ResourceNotFoundException("User not found");
        }
        Set<Illness> illnesses = patient.getPastIllnesses();
        return new ArrayList<>(illnesses);
    }

    @Override
    public List<Document> getUserDocuments(String uid, boolean isUid) {
        Patient patient;
        if(isUid){
            patient = patientRepository.getPatientByUid(uid);
        }else{
            patient = patientRepository.getPatientByPhoneNum(uid);
        }
        if(patient==null){
            throw new ResourceNotFoundException("User not found");
        }
        Set<Document> documents = patient.getDocuments();
        return new ArrayList<>(documents);
    }

    public boolean checkIfPresent(Patient patient){
        Optional<Patient> patient1 = Optional.ofNullable(patientRepository.getPatientByPhoneNum(patient.getPhoneNum()));
        Optional<Patient> patient2 = Optional.ofNullable(patientRepository.getPatientByUid(patient.getUid()));
        return patient1.isEmpty()&& patient2.isEmpty();
    }


}
