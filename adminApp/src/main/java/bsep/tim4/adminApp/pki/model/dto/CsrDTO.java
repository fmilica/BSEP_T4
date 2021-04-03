package bsep.tim4.adminApp.pki.model.dto;

import bsep.tim4.adminApp.pki.model.enums.CsrStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CsrDTO {

    private Long id;

    private String commonName;

    private String name;

    private String surname;

    private String organizationName;

    private String organizationUnit;

    private String country;

    private String email;

    private CsrStatus status;
}
