package bsep.tim4.adminApp.pki.model.dto;

import bsep.tim4.adminApp.pki.model.enums.CertificateTemplateEnum;
import bsep.tim4.adminApp.pki.model.enums.KeyUsageEnum;

import java.util.Date;
import java.util.List;

public class CertificateDTO {

    private Long certificateRequestId;

    private Long signingCertificateId;

    private Date startDate;

    private Date endDate;

    private CertificateTemplateEnum certificateType;

    private List<KeyUsageEnum> keyUsages;
}
