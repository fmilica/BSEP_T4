package bsep.tim4.adminApp.pki.model.dto;

import bsep.tim4.adminApp.pki.model.enums.CertificateStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CertificateViewDTO {

    private String alias;

    private String commonName;

    private boolean revoked;

    private List<CertificateViewDTO> children;

    public CertificateViewDTO(String alias) {
        this.alias = alias;
        this.children = new ArrayList<>();
    }

    public CertificateViewDTO(String alias, String commonName, boolean revoked) {
        this.alias = alias;
        this.commonName = commonName;
        this.revoked = revoked;
        this.children = new ArrayList<>();
    }
}
