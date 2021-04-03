package bsep.tim4.adminApp.pki.model.mapper;

import bsep.tim4.adminApp.pki.model.CSR;
import bsep.tim4.adminApp.pki.model.dto.CsrDTO;

import java.util.ArrayList;
import java.util.List;

public class CsrMapper {

    public CsrDTO toCsrDto(CSR csr) {
        return new CsrDTO(csr.getId(), csr.getCommonName(), csr.getName(), csr.getSurname(), csr.getOrganizationName(),
                csr.getOrganizationUnit(), csr.getCountry(), csr.getEmail());
    }

    public List<CsrDTO> toCsrDtoList(List<CSR> csrList) {
        List<CsrDTO> csrDTOS = new ArrayList<CsrDTO>();
        for(CSR csr : csrList) {
            csrDTOS.add(toCsrDto(csr));
        }
        return csrDTOS;
    }

}
