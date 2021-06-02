package bsep.tim4.hospitalApp.util;

import bsep.tim4.hospitalApp.dto.CSRDto;
import lombok.Getter;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;

import java.io.IOException;
import java.io.StringWriter;
import java.security.*;

/**
 * Klasa za generisanje Certificate Signing Request-a po PKCS#10 standardu na osnovu prosledjenih podataka
 */
@Getter
public class CSRGenerator {

    private PublicKey publicKey;
    private PrivateKey privateKey;

    public CSRGenerator() {}

    public PKCS10CertificationRequest generateCSR(CSRDto csrDto) throws OperatorCreationException {

        // kreiramo builder za X500Name sa odgovarajucim podacima
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, csrDto.getCommonName());
        builder.addRDN(BCStyle.SURNAME, csrDto.getSurname());
        builder.addRDN(BCStyle.GIVENNAME, csrDto.getName());
        builder.addRDN(BCStyle.O, csrDto.getOrganizationName());
        builder.addRDN(BCStyle.OU, csrDto.getOrganizationUnit());
        builder.addRDN(BCStyle.C, csrDto.getCountry());
        builder.addRDN(BCStyle.E, csrDto.getEmail());

        X500Name subject = builder.build();

        // kreiramo builder za csr sa podacima i javnim kljucem
        PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(
                subject, this.publicKey);
        JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder("SHA256withRSA");
        // potpisujemo sadrzaj privatnim kljucem
        ContentSigner signer = csBuilder.build(this.privateKey);
        PKCS10CertificationRequest csr = p10Builder.build(signer);

        return csr;
    }

    public String convertCsr(PKCS10CertificationRequest csr) throws IOException {

        StringWriter sw = new StringWriter();
        JcaPEMWriter writer = new JcaPEMWriter(sw);
        writer.writeObject(csr);
        writer.close();
        return sw.toString();
    }
}
