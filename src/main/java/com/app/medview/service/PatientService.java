package com.app.medview.service;

import com.app.medview.model.Document;
import com.app.medview.model.Illness;
import com.app.medview.model.Patient;

import java.util.List;

public interface PatientService {
    Patient getPatientByPhone(String phone);
    Patient getPatientByUid(String uid);
    Patient savePatient(Patient patientModel);
    List<Illness> getUserIllness(String uid,boolean isUid);
    List<Document> getUserDocuments(String uid,boolean isUid);


}
