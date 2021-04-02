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

    public KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            KeyPair keyPair = keyGen.generateKeyPair();
            this.publicKey = keyPair.getPublic();
            this.privateKey = keyPair.getPrivate();
            return keyPair;
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    public PKCS10CertificationRequest generateCSR(CSRDto csrDto) {
        try {
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
            //return csr.getEncoded();
        } catch (OperatorCreationException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String convertCsr(PKCS10CertificationRequest csr) {
        try {
            StringWriter sw = new StringWriter();
            JcaPEMWriter writer = new JcaPEMWriter(sw);
            writer.writeObject(csr);
            writer.close();
            return sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
/*
    public static void main(String[] args) throws Exception {
        CSRDto csrDto = new CSRDto("COMONNEJM", "NEJM", "SURNEJM", "ORGNEJM", "ORGUNIT", "LOCAL", "COUNTRY", "IMEJL", "SIFRA");
        CSRGenerator generatorCSR = new CSRGenerator();
        generatorCSR.generateKeyPair();
        PKCS10CertificationRequest csr = generatorCSR.generateCSR(csrDto);

        StringWriter sw = new StringWriter();//create a StringWriter
        JcaPEMWriter writer = new JcaPEMWriter(sw);
        writer.writeObject(csr);
        writer.close();
        String csrString = sw.toString();
        System.out.println(csrString);
    }*/
}
