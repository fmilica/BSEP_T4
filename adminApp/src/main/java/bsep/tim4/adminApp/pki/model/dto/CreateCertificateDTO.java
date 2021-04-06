package bsep.tim4.adminApp.pki.model.dto;

import bsep.tim4.adminApp.pki.model.enums.CertificateTemplateEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCertificateDTO {
    private Long csrId;

    private String caAlias;

    private Date beginDate;

    private Date endDate;

    private CertificateTemplateEnum template;
}
