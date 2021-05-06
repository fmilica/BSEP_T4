package bsep.tim4.adminApp.pki.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCertificateDTO {
    private Long csrId;

    @NotBlank(message = "Certificate authority alias cannot be empty.")
    @Size(max = 50, message = "Certificate authority alias is too long.")
    @Pattern(regexp = "[a-zA-Z0-9-]+", message = "Certificate authority alias is not valid.")
    private String caAlias;

    @NotNull(message = "Begin date cannot be empty.")
    //@FutureOrPresent(message = "Begin date cannot be before today.")
    private Date beginDate;

    @NotNull(message = "End date cannot be empty.")
    @Future(message = "End date cannot be before today.")
    private Date endDate;

    private CertificateAdditionalInfo additionalInfo;
}
