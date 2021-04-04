package bsep.tim4.adminApp.pki.model.dto;

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

    private boolean revoked;

    private List<CertificateViewDTO> children;

    public CertificateViewDTO(String alias) {
        this.alias = alias;
        this.children = new ArrayList<>();
    }

    public CertificateViewDTO(String alias, boolean revoked) {
        this.alias = alias;
        this.revoked = revoked;
        this.children = new ArrayList<>();
    }
}
