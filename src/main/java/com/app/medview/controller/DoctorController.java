package com.app.medview.controller;

import com.app.medview.model.AuthRequest;
import com.app.medview.model.AuthResponse;
import com.app.medview.model.Doctor;
import com.app.medview.model.Patient;
import com.app.medview.service.DoctorDetailService;
import com.app.medview.service.DoctorServiceImpl;
import com.app.medview.service.MyUserDetailService;
import com.app.medview.util.JwtUtil;
import com.app.medview.util.ResponseDoctor;
import com.app.medview.util.ResponseModel;
import com.app.medview.util.ResponsePatient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.print.Doc;
import java.util.HashMap;

@RestController()
@RequestMapping("/api/v1/doctor") @RequiredArgsConstructor@Slf4j
public class DoctorController {

    private final DoctorServiceImpl doctorService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final DoctorDetailService doctorDetailService;

    @GetMapping
    public ResponseEntity<ResponseModel> getDoctor(){
        try{
            Doctor doctor = doctorService.getDoctorByLicenseId(SecurityContextHolder.getContext().getAuthentication().getName());
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseModel(true,new ResponseDoctor(doctor),"Here's your doctor!",200));
        }catch (Exception e){
            e.printStackTrace();
            ResponseModel responseModel = new ResponseModel(false,-1,e.getMessage(),500);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseModel);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseModel> createDoctor(@RequestBody Doctor doctor){
        try{
            log.info(doctor.toString());
            HashMap<String ,Object> data = new HashMap<>();
            ResponseModel responseModel = new ResponseModel();
            if(doctor.getLicenseId()==null||doctor.getPassword()==null){
                responseModel.setSuccess(false);
                responseModel.setData(-1);
                responseModel.setMessage("Bad Request");
                responseModel.setStatus(400);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModel);
            }
            String password = new String(doctor.getPassword());

            if(!doctorService.checkIfPresent(doctor)){

                responseModel.setSuccess(false);
                responseModel.setData(-1);
                responseModel.setMessage("User already exists!");
                responseModel.setStatus(400);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModel);
            }


            Doctor doctor1 = doctorService.saveDoctor(doctor);

            if(doctor1==null) throw new Exception("Couldn't create the Patient");
            log.warn(doctor.getLicenseId()+" "+password);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(doctor.getLicenseId(), password));

            final UserDetails userDetails = doctorDetailService.loadUserByUsername(doctor.getLicenseId());
            final String jwt = jwtUtil.generateToken(userDetails,false);

            data.put("jwt",jwt);
            data.put("user",new ResponseDoctor(doctor));
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

    @PostMapping("/login")
    public ResponseEntity<ResponseModel> authenticateDoctor(@RequestBody AuthRequest authRequest){
        log.info(authRequest.getUsername()+" "+authRequest.getPassword());

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
            final UserDetails userDetails = doctorDetailService.loadUserByUsername(authRequest.getUsername());
            final String jwt = jwtUtil.generateToken(userDetails,false);
            HashMap<String,Object> data = new HashMap<>();
            Doctor doctor = doctorService.getDoctorByLicenseId(authRequest.getUsername());
            data.put("user",new ResponseDoctor(doctor));
            data.put("jwt",jwt);
            ResponseModel responseModel  = new ResponseModel(true,data,"Logged in Successfully!",200);

            return ResponseEntity.status(HttpStatus.OK).body(responseModel);
        }
        catch (BadCredentialsException e){
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

}
