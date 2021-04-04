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

    private CertificateStatusEnum status;

    private List<CertificateViewDTO> children;

    public CertificateViewDTO(String alias) {
        this.alias = alias;
        this.children = new ArrayList<>();
    }

    public CertificateViewDTO(String alias, CertificateStatusEnum status) {
        this.alias = alias;
        this.status = status;
        this.children = new ArrayList<>();
    }
}
