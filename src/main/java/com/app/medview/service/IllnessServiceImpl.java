package com.app.medview.service;

import com.app.medview.model.Illness;
import com.app.medview.model.Patient;
import com.app.medview.repository.IllnessRepository;
import com.app.medview.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service@Transactional@RequiredArgsConstructor
public class IllnessServiceImpl implements IllnessService{
    public final PatientRepository patientRepository;
    public final IllnessRepository illnessRepository;
    @Override
    public Illness saveIllness(Illness illness) {
        return illnessRepository.save(illness);
    }

    @Override
    public List<Illness> getIllnesses(String phone) {
        Optional<Patient> optionalPatient = Optional.ofNullable(patientRepository.getPatientByPhoneNum(phone));
        if(optionalPatient.isPresent()){
            return (List<Illness>) optionalPatient.get().getPastIllnesses();
        }else{
            throw new UsernameNotFoundException("User not found!");
        }
    }
}
