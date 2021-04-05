package bsep.tim4.adminApp.mailSender.verification;

import bsep.tim4.adminApp.pki.model.CSR;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class VerificationLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne(targetEntity = CSR.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "csr_id")
    private CSR certificateRequest;

    public VerificationLink() {

    }

    public VerificationLink(String token, CSR certificateRequest) {
        this.token = token;
        this.certificateRequest = certificateRequest;
    }
}
