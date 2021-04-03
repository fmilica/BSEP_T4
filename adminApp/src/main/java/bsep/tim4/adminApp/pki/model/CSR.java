package bsep.tim4.adminApp.pki.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.security.PublicKey;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CSR {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private PublicKey publicKey;

    private String commonName;

    private String name;

    private String surname;

    private String organizationName;

    private String organizationUnit;

    private String country;

    private String email;

    //private X500Name x500Name;

    private boolean verified;

    public CSR(PublicKey publicKey, X500Name x500Name) {
        this.publicKey = publicKey;
        this.mapX500Name(x500Name);
        this.verified = false;
    }

    private void mapX500Name(X500Name x500Name) {
        commonName = x500Name.getRDNs(BCStyle.CN)[0].getFirst().getValue().toString();
        surname = x500Name.getRDNs(BCStyle.SURNAME)[0].getFirst().getValue().toString();
        name = x500Name.getRDNs(BCStyle.GIVENNAME)[0].getFirst().getValue().toString();
        organizationName = x500Name.getRDNs(BCStyle.O)[0].getFirst().getValue().toString();
        organizationUnit = x500Name.getRDNs(BCStyle.OU)[0].getFirst().getValue().toString();
        country = x500Name.getRDNs(BCStyle.C)[0].getFirst().getValue().toString();
        email = x500Name.getRDNs(BCStyle.E)[0].getFirst().getValue().toString();
    }
}
