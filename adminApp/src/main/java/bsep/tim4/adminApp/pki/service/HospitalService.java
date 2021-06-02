package bsep.tim4.adminApp.pki.service;

import bsep.tim4.adminApp.pki.exceptions.NonExistentIdException;
import bsep.tim4.adminApp.pki.model.Hospital;
import bsep.tim4.adminApp.pki.model.Simulator;
import bsep.tim4.adminApp.pki.model.dto.LogConfig;
import bsep.tim4.adminApp.pki.repository.HospitalRepository;
import bsep.tim4.adminApp.pki.repository.SimulatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private SimulatorRepository simulatorRepository;

    public List<Hospital> getAll() {
        return hospitalRepository.findAll();
    }

    public List<LogConfig> addSimulator(Long hospitalId, List<LogConfig> logConfigList) throws NonExistentIdException {
        Hospital hospital = hospitalRepository.findById(hospitalId).orElse(null);
        if (hospital == null) {
            throw new NonExistentIdException("Hospital");
        }
        Set<Simulator> addedSimulators = new HashSet<>();
        for (LogConfig logConfig : logConfigList) {
            Simulator simulator = simulatorRepository.findById(logConfig.getSimulatorId()).orElse(null);
            if (simulator == null) {
                throw new NonExistentIdException("Simulator");
            }
            addedSimulators.add(simulator);
            logConfig.setPath(simulator.getPath());
        }
        hospital.setSimulators(addedSimulators);
        hospitalRepository.save(hospital);
        return logConfigList;
    }

    public void handleCsr(String email) {
        Hospital hospital = hospitalRepository.findOneByEmail(email);
        if (hospital != null) {
            // bolnica postoji
            // uvecavamo broj uredjaja za 1
            hospital.setMedicalDeviceNumber(hospital.getMedicalDeviceNumber() + 1);
            hospitalRepository.save(hospital);
        } else {
            // bolnica ne postoji
            // kreiramo je
            Hospital newHospital = new Hospital(email);
            hospitalRepository.save(newHospital);
        }
    }
}
