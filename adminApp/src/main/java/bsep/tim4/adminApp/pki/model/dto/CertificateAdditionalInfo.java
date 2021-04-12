package bsep.tim4.adminApp.pki.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CertificateAdditionalInfo {

    private boolean ca;

    private List<Integer> keyUsages;

    private List<String> extendedKeyUsages;

    public CertificateAdditionalInfo(int root) {
        this.ca = true;
        //KeyUsage.keyCertSign | KeyUsage.digitalSignature | KeyUsage.dataEncipherment
        this.keyUsages = Arrays.asList(4, 128, 16);
        this.extendedKeyUsages = new ArrayList<>();
    }

    public int getKeyUsages() {
        int total = 0;
        for(Integer value : keyUsages) {
            total = total | value;
        }
        return total;
    }

    public KeyPurposeId[] getExtendedKeyUsages() {
        List<KeyPurposeId> extended = new ArrayList<>();
        for(String extendedKeyUsage : extendedKeyUsages) {
            switch (extendedKeyUsage) {
                case "id_kp_serverAuth":
                    extended.add(KeyPurposeId.id_kp_serverAuth);
                    break;
                case "id_kp_clientAuth":
                    extended.add(KeyPurposeId.id_kp_clientAuth);
                    break;
                case "id_kp_codeSigning":
                    extended.add(KeyPurposeId.id_kp_codeSigning);
                    break;
                case "id_kp_emailProtection":
                    extended.add(KeyPurposeId.id_kp_emailProtection);
                    break;
                case "id_kp_timeStamping":
                    extended.add(KeyPurposeId.id_kp_timeStamping);
                    break;
                case "id_kp_OCSPSigning":
                    extended.add(KeyPurposeId.id_kp_OCSPSigning);
                    break;
            }
        }
        KeyPurposeId[] extendedArray = new KeyPurposeId[extended.size()];
        extendedArray = extended.toArray(extendedArray);
        return extendedArray;
    }
}
