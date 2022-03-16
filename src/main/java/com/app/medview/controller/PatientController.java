package com.app.medview.controller;

import com.app.medview.model.*;
import com.app.medview.repository.PatientRepository;
import com.app.medview.service.*;
import com.app.medview.util.JwtUtil;
import com.app.medview.util.ResponseModel;
import com.app.medview.util.ResponsePatient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/patient")@Slf4j@RequiredArgsConstructor
public class PatientController {

    private final PatientRepository patientRepository;

    private final PatientServiceImpl patientService;
    //get all patients
    private  final DocumentServiceImpl documentService;
    private final IllnessServiceImpl illnessService;

    private final AuthenticationManager authenticationManager;


    private final MyUserDetailService myUserDetailService;


    private final JwtUtil jwtUtil;

    private final AwsS3Service awsS3Service;

    @GetMapping
    public ResponseEntity<ResponseModel> getPatient() {
        Patient patient = patientRepository.getPatientByPhoneNum(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel(true,new ResponsePatient(patient),"Here's your patient",200));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseModel> login(@RequestBody AuthRequest authRequest)throws Exception{
        log.info(authRequest.getUsername()+" "+authRequest.getPassword());

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            final UserDetails userDetails = myUserDetailService.loadUserByUsername(authRequest.getUsername());
            log.warn("userDetails");
            final String jwt = jwtUtil.generateToken(userDetails,true);
            log.warn("jwt "+jwt);
            HashMap<String,Object> data = new HashMap<>();
            Patient patient = patientService.getPatientByPhone(authRequest.getUsername());
            data.put("user",new ResponsePatient(patient));
            data.put("jwt",jwt);
            ResponseModel responseModel  = new ResponseModel(true,data,"Logged in Successfully!",200);

            return ResponseEntity.status(HttpStatus.OK).body(responseModel);
        }
        catch (BadCredentialsException e){
            e.printStackTrace();
            ResponseModel responseModel = new ResponseModel(false,-1,"Incorrect credentials!",400);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModel);

        }
        catch (UsernameNotFoundException e){
            e.printStackTrace();
            ResponseModel responseModel = new ResponseModel(false,-1,"Incorrect credentials!",400);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModel);

        }
        catch (HttpClientErrorException.BadRequest e){
            e.printStackTrace();
            ResponseModel responseModel = new ResponseModel(false,-1,e.getMessage(),400);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModel);
        }
        catch (Exception e){
            e.printStackTrace();
            ResponseModel responseModel = new ResponseModel(false,-1,e.getMessage(),500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseModel);
        }
    }

    @GetMapping("/patients")
    public ResponseEntity<?> getAllPatients() {
        try{
            log.error("Here");
            return ResponseEntity.status(HttpStatus.OK).body(patientRepository.findAll());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseModel> createPatient(@RequestBody  Patient patient){
        try{
            log.info(patient.toString());
            HashMap<String ,Object> data = new HashMap<>();
            ResponseModel responseModel = new ResponseModel();
            if(patient.getUid()==null||patient.getPassword()==null||patient.getPhoneNum()==null){
                responseModel.setSuccess(false);
                responseModel.setData(-1);
                responseModel.setMessage("Bad Request");
                responseModel.setStatus(400);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModel);
            }
            String password = new String(patient.getPassword());

            if(!patientService.checkIfPresent(patient)){

                responseModel.setSuccess(false);
                responseModel.setData(-1);
                responseModel.setMessage("User already exists!");
                responseModel.setStatus(400);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModel);
            }


            Patient patient1 = patientService.savePatient(patient);

            if(patient1==null) throw new Exception("Couldn't create the Patient");
            log.warn(patient.getPhoneNum()+" "+password);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(patient.getPhoneNum(), password));
            final UserDetails userDetails = myUserDetailService.loadUserByUsername(patient.getPhoneNum());
            final String jwt = jwtUtil.generateToken(userDetails,true);

            data.put("jwt",jwt);
            data.put("user",new ResponsePatient(patient));
            responseModel.setSuccess(true);
            responseModel.setData(data);
            responseModel.setMessage("User created successfully!");
            responseModel.setStatus(201);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseModel);

        }catch (HttpClientErrorException.BadRequest e){
            ResponseModel responseModel = new ResponseModel(false,-1,e.getMessage(),400);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModel);
        }
        catch (Exception e){
            e.printStackTrace();
            ResponseModel responseModel = new ResponseModel(false,-1,e.getMessage(),500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseModel);
        }
    }

    @GetMapping("/getIllness")
    public ResponseEntity<ResponseModel> getIllnesses()throws Exception{
        log.info(SecurityContextHolder.getContext().getAuthentication().getName());
        try{

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            List<Illness> illnesses = patientService.getUserIllness(username,false);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel(true,illnesses,"Fetched successfully!",200));
        }catch (Exception e){
            e.printStackTrace();
            ResponseModel responseModel = new ResponseModel(false,-1,e.getMessage(),500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseModel);
        }
    }

    @GetMapping("/getDocuments")
    public ResponseEntity<ResponseModel> getDocuments()throws Exception{
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            List<Document> documents = patientService.getUserDocuments(username,false);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel(true,documents,"Fetched successfully!",200));
        }
        catch (Exception e){
            e.printStackTrace();
            ResponseModel responseModel = new ResponseModel(false,-1,e.getMessage(),500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseModel);
        }
    }

    @PostMapping("/addDocument")
    public ResponseEntity<ResponseModel> addDocuments(@RequestParam("file") MultipartFile file,@RequestParam("type") String type,
                                          @RequestParam("description") String desc){
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            log.warn(username+" username");
            if(username==null){
                throw new Exception("Unauthorized");
            }
            Patient patient = patientService.getPatientByPhone(username);
            if(patient==null)throw new Exception("Unauthorized");

            String fileKey = awsS3Service.uploadFile(file,"documents/"+username);
            Document document = new Document(type,fileKey, patient.getId().toString(),desc);

            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseModel(true,documentService.saveDocument(document),"Added successfully!",201));
        }catch (Exception e){
            e.printStackTrace();
            ResponseModel responseModel = new ResponseModel(false,-1,e.getMessage(),500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseModel);
        }
    }

    @PostMapping("/addIllness")
    public ResponseEntity<ResponseModel> addIllness(@RequestBody Illness illness)throws Exception{
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            if(username==null){
                throw new Exception("Unauthorized");
            }
            illnessService.saveIllness(illness);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseModel(true,illnessService.saveIllness(illness)
                    ,"Added successfully!",201));
        }catch (Exception e){
            e.printStackTrace();
            ResponseModel responseModel = new ResponseModel(false,-1,e.getMessage(),500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseModel);
        }
    }


}
