package bsep.tim4.adminApp.pki.service;

import bsep.tim4.adminApp.pki.model.Hospital;
import bsep.tim4.adminApp.pki.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    public List<Hospital> getAll() {
        return hospitalRepository.findAll();
    }

}
