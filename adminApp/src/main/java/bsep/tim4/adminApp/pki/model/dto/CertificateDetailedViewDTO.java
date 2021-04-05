package bsep.tim4.adminApp.pki.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CertificateDetailedViewDTO {
    // certificate
    private String certAlias;
    private BigInteger serialNumb;
    private int version;
    private Date validFrom;
    private Date validUntil;
    private String signatureAlgorithm;
    private byte[] publicKey;
    private String commonName;

    // subject
    private String subjectData;

    // issuer
    private String issuerData;
}
