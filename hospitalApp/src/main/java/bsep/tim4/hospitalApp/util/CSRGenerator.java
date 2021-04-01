package bsep.tim4.hospitalApp.util;

import bsep.tim4.hospitalApp.dto.CSRDto;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.security.*;

/**
 * Klasa za generisanje Certificate Signing Request-a po PKCS#10 standardu na osnovu prosledjenih podataka
 */
public class CSRGenerator {

    private PublicKey publicKey;
    private PrivateKey privateKey;
    private static KeyPairGenerator keyPairGenerator;
    private static CSRGenerator csrGenerator;

    public CSRGenerator() {
        KeyPair keyPair = generateKeyPair();
        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();
    }

    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] generatePKCS10(CSRDto csrDto) {
        try {
            Signature sig = Signature.getInstance("SHA1withRSA");
            // Navodimo kljuc kojim potpisujemo
            sig.initSign(this.privateKey);

            X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
            builder.addRDN(BCStyle.CN, csrDto.getCommonName());
            builder.addRDN(BCStyle.SURNAME, csrDto.getSurname());
            builder.addRDN(BCStyle.GIVENNAME, csrDto.getName());
            builder.addRDN(BCStyle.O, csrDto.getOrganizationName());
            builder.addRDN(BCStyle.OU, csrDto.getOrganizationUnit());
            builder.addRDN(BCStyle.C, csrDto.getCountry());
            builder.addRDN(BCStyle.E, csrDto.getEmailAddress());

            X500Name subject = builder.build();

            PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(
                    subject, this.publicKey);
            JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder("SHA256withRSA");
            ContentSigner signer = csBuilder.build(this.privateKey);
            PKCS10CertificationRequest csr = p10Builder.build(signer);

            return csr.getEncoded();
        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException | OperatorCreationException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        CSRDto csrDto = new CSRDto("COMONNEJM", "NEJM", "SURNEJM", "ORGNEJM", "ORGUNIT", "LOCAL", "COUNTRY", "IMEJL");
        CSRGenerator generatorCSR = new CSRGenerator();
        generatorCSR.generatePKCS10(csrDto);
    }
}
