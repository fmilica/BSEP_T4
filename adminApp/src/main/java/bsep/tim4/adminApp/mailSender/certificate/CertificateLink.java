package bsep.tim4.adminApp.mailSender.certificate;

import bsep.tim4.adminApp.pki.model.CSR;
import bsep.tim4.adminApp.pki.model.CertificateData;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.security.cert.Certificate;

@Entity
@Getter
@Setter
public class CertificateLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne(targetEntity = CertificateData.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "cert_data_id")
    private CertificateData certificateData;

    public CertificateLink() {}

    public CertificateLink(String token, CertificateData certificateData) {
        this.token = token;
        this.certificateData = certificateData;
    }
}
