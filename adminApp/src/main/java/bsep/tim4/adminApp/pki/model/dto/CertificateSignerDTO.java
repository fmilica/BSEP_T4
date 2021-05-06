package bsep.tim4.adminApp.pki.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.PrivateKey;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CertificateSignerDTO {

    private String alias;

    private String commonName;

    private String name;

    private String surname;

    private String organizationName;

    private String organizationUnit;

    private String country;

    private String email;

}
