package com.app.medview.service;

import com.app.medview.model.Document;
import com.app.medview.model.Illness;
import com.app.medview.model.Patient;
import com.app.medview.repository.DocumentRepository;
import com.app.medview.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service@Transactional@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService{
    private final DocumentRepository documentRepository;
    private final PatientRepository patientRepository;
    @Override
    public Document saveDocument(Document document) {
        return documentRepository.save(document);
    }

    @Override
    public List<Document> getDocument(String phone) {
        Optional<Patient> optionalPatient = Optional.ofNullable(patientRepository.getPatientByPhoneNum(phone));
        if(optionalPatient.isPresent()){
            return (List<Document>) optionalPatient.get().getDocuments();
        }else{
            throw new UsernameNotFoundException("User not found!");
        }
    }
}
