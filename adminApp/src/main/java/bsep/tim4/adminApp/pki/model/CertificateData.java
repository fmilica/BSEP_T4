package bsep.tim4.adminApp.pki.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CertificateData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String commonName;

    private String alias;

    private String parentAlias;

    private boolean revoked;

    private Date validFrom;

    private Date validTo;

    private String revocationReason;

    @Temporal(TemporalType.TIMESTAMP)
    private Date revocationDate;

    public CertificateData(String commonName, String alias, String parentAlias, Date validFrom, Date validTo) {
        this.commonName = commonName;
        this.alias = alias;
        this.parentAlias = parentAlias;
        this.revoked = false;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    public String getEmail() {
        return this.alias;
    }
}
