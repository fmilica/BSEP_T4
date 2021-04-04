package bsep.tim4.adminApp.pki.model.mapper;

import bsep.tim4.adminApp.pki.model.CSR;
import bsep.tim4.adminApp.pki.model.IssuerData;
import bsep.tim4.adminApp.pki.model.dto.CertificateSignerDTO;
import bsep.tim4.adminApp.pki.model.dto.CsrDTO;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.style.BCStyle;

import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;

public class CertificateSignerMapper {

    public CertificateSignerDTO toCertificateSignerDto(IssuerData issuerData) {
        PrivateKey privateKey = issuerData.getPrivateKey();
        String alias = (issuerData.getX500name().getRDNs(BCStyle.E)[0]).getFirst().getValue().toString();
        String commonName = (issuerData.getX500name().getRDNs(BCStyle.CN)[0]).getFirst().getValue().toString();
        String name = (issuerData.getX500name().getRDNs(BCStyle.NAME)[0]).getFirst().getValue().toString();
        String surname = (issuerData.getX500name().getRDNs(BCStyle.SURNAME)[0]).getFirst().getValue().toString();
        String organizationName = (issuerData.getX500name().getRDNs(BCStyle.O)[0]).getFirst().getValue().toString();
        String organizationUnit = (issuerData.getX500name().getRDNs(BCStyle.OU)[0]).getFirst().getValue().toString();
        String country = (issuerData.getX500name().getRDNs(BCStyle.C)[0]).getFirst().getValue().toString();
        String email = (issuerData.getX500name().getRDNs(BCStyle.E)[0]).getFirst().getValue().toString();

        return new CertificateSignerDTO(privateKey, alias, commonName, name, surname, organizationName, organizationUnit,
                    country, email);
    }

    public List<CertificateSignerDTO> toCertificateSignerDtoList(List<IssuerData> issuerDatas) {
        List<CertificateSignerDTO> certificateSignerDTOS = new ArrayList<CertificateSignerDTO>();
        for(IssuerData issuerData : issuerDatas) {
            certificateSignerDTOS.add(toCertificateSignerDto(issuerData));
        }
        return certificateSignerDTOS;
    }
}
