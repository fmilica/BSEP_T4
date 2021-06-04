package bsep.tim4.hospitalApp.service;

import bsep.tim4.hospitalApp.dto.PatientStatusDto;
import bsep.tim4.hospitalApp.exceptions.NonExistentIdException;
import bsep.tim4.hospitalApp.model.Patient;
import bsep.tim4.hospitalApp.model.PatientAlarm;
import bsep.tim4.hospitalApp.model.PatientEncrypted;
import bsep.tim4.hospitalApp.model.PatientStatus;
import bsep.tim4.hospitalApp.repository.PatientAlarmRepository;
import bsep.tim4.hospitalApp.repository.PatientRepository;
import bsep.tim4.hospitalApp.repository.PatientStatusRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PatientStatusService {

    @Autowired
    private PatientStatusRepository patientStatusRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientAlarmRepository patientAlarmRepository;

    @Autowired
    private PatientService patientService;

    @Autowired
    private KieSessionService kieSessionService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public PatientStatus save(PatientStatus patientStatus) throws NonExistentIdException {

        PatientEncrypted patient = patientRepository.findById(patientStatus.getPatientId()).orElse(null);

        if(patient == null) {
            throw new NonExistentIdException("Patient");
        }

        patientStatus = patientStatusRepository.save(patientStatus);
        KieSession kieSession = kieSessionService.getRulesSession();
        kieSession.insert(patientStatus);
        kieSession.fireAllRules();

        QueryResults results = kieSession.getQueryResults("getAllAlarmsByPatientStatusId", patientStatus.getId());
        List<PatientAlarm> alarms = new ArrayList<>();

        for (QueryResultsRow row : results) {
            PatientAlarm alarm = (PatientAlarm) row.get("$alarm");
            alarms.add(alarm);
        }

        saveAndSendNewAlarms(alarms);
        return patientStatus;
    }

    public Page<PatientStatusDto> findAll(Pageable pageable) throws JsonProcessingException, NonExistentIdException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidKeyException {
        Page<PatientStatus> page = patientStatusRepository.findAllByOrderByTimestampDesc(pageable);
        List<PatientStatusDto> patientStatusDtos = new ArrayList<>();
        for(PatientStatus patientStatus : page.toList()) {
            Patient patient = patientService.findById(patientStatus.getPatientId());
            patientStatusDtos.add(new PatientStatusDto(patient.getName(), patientStatus.getHeartRate(), patientStatus.getLowerBloodPressure() + "/" +
                    patientStatus.getUpperBloodPressure(), patientStatus.getBodyTemperature(), patientStatus.getRespiratoryRate(), patientStatus.getTimestamp()));
        }
        return new PageImpl<>(patientStatusDtos, page.getPageable(), page.getTotalElements());
    }

    public Page<PatientStatusDto> findAllByPatientId(String patientId, Pageable pageable) throws NonExistentIdException, JsonProcessingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidKeyException {
        Page<PatientStatus> page = patientStatusRepository.findAllByPatientIdOrderByTimestampDesc(patientId, pageable);
        List<PatientStatusDto> patientStatusDtos = new ArrayList<>();
        for(PatientStatus patientStatus : page.toList()) {
            Patient patient = patientService.findById(patientStatus.getPatientId());
            patientStatusDtos.add(new PatientStatusDto(patient.getName(), patientStatus.getHeartRate(), patientStatus.getLowerBloodPressure() + "/" +
                    patientStatus.getUpperBloodPressure(), patientStatus.getBodyTemperature(), patientStatus.getRespiratoryRate(), patientStatus.getTimestamp()));
        }
        return new PageImpl<>(patientStatusDtos, page.getPageable(), page.getTotalElements());
    }

    private void saveAndSendNewAlarms(List<PatientAlarm> alarms) {
        PatientAlarm lastSaved = this.patientAlarmRepository.findFirstByOrderByTimestampDesc();
        List<PatientAlarm> newAlarms = new ArrayList<>();
        if (lastSaved != null) {
            for (PatientAlarm alarm : alarms) {
                if (alarm.getTimestamp().after(lastSaved.getTimestamp())) {
                    newAlarms.add(patientAlarmRepository.save(alarm));
                }
            }
        } else {
            for (PatientAlarm alarm : alarms) {
                newAlarms.add(patientAlarmRepository.save(alarm));
            }
        }
        this.simpMessagingTemplate.convertAndSend("/topic/patients", newAlarms);
    }
}
