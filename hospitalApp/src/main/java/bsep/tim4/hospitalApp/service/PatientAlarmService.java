package bsep.tim4.hospitalApp.service;

import bsep.tim4.hospitalApp.dto.PatientAlarmDto;
import bsep.tim4.hospitalApp.dto.PatientStatusDto;
import bsep.tim4.hospitalApp.exceptions.NonExistentIdException;
import bsep.tim4.hospitalApp.model.Patient;
import bsep.tim4.hospitalApp.model.PatientAlarm;
import bsep.tim4.hospitalApp.model.PatientEncrypted;
import bsep.tim4.hospitalApp.model.PatientStatus;
import bsep.tim4.hospitalApp.repository.PatientAlarmRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PatientAlarmService {

    @Autowired
    private PatientAlarmRepository patientAlarmRepository;

    @Autowired
    private PatientService patientService;

    public Page<PatientAlarmDto> findAll(Pageable pageable) throws JsonProcessingException, NonExistentIdException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidKeyException {
        List<PatientAlarmDto> patientAlarmDtos = new ArrayList<>();
        Page<PatientAlarm> page = patientAlarmRepository.findAllByOrderByTimestampDesc(pageable);
        for(PatientAlarm patientAlarm : page.toList()) {
            Patient patient = patientService.findById(patientAlarm.getPatientId());
            patientAlarmDtos.add(new PatientAlarmDto(patient.getName(), patientAlarm.getMessage(), patientAlarm.getTimestamp()));
        }
        return new PageImpl<>(patientAlarmDtos, page.getPageable(), page.getTotalElements());
    }
}
