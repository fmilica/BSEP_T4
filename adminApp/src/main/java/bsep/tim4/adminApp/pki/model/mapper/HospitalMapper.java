package bsep.tim4.adminApp.pki.model.mapper;

import bsep.tim4.adminApp.pki.model.CSR;
import bsep.tim4.adminApp.pki.model.Hospital;
import bsep.tim4.adminApp.pki.model.Simulator;
import bsep.tim4.adminApp.pki.model.dto.CsrDTO;
import bsep.tim4.adminApp.pki.model.dto.HospitalDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HospitalMapper {

    public HospitalDTO toHospitalDto(Hospital hospital) {
        String simulators = hospital.getSimulators()
                .stream().map(Simulator::getId).map(Object::toString)
                .collect(Collectors.joining(", "));
        return new HospitalDTO(hospital.getId(), hospital.getEmail(), simulators);
    }

    public List<HospitalDTO> toHospitalDtoList(List<Hospital> hospitalList) {
        List<HospitalDTO> hospitalDTOS = new ArrayList<HospitalDTO>();
        for(Hospital hospital : hospitalList) {
            hospitalDTOS.add(toHospitalDto(hospital));
        }
        return hospitalDTOS;
    }

}
