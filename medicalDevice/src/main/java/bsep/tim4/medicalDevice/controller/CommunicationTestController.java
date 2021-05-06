package bsep.tim4.medicalDevice.controller;

import bsep.tim4.medicalDevice.util.SignatureUtil;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@RestController
@RequestMapping(value="api/communication-test")
public class CommunicationTestController {

    @Autowired
    RestTemplate restTemplate;

    @Value("${hospital.uri}")
    private String hospitalApplicationUri;

    @Value("${trust.store.password}")
    private String truststorePass;

    @Value("${server.ssl.key-store}")
    private String keystore;

    @Value("${server.ssl.key-store-password}")
    private String keystorePass;

    @Value("${server.ssl.key-alias}")
    private String alias;

    private final String testCommunicationUri = "/log/receive";
    private final String testContent = "Ovo je pokusaj slanja poruke.";

    @GetMapping
    public ResponseEntity<String> testCommunication() throws IOException, CertificateException, CMSException, OperatorCreationException {

        final String testCommsFullUri = hospitalApplicationUri +  testCommunicationUri;

        byte[] signedMessage = SignatureUtil.signMessage(testContent, keystore, keystorePass, alias);

        HttpEntity<byte[]> request =
                new HttpEntity<>(signedMessage);

        try {
            ResponseEntity<String> responseEntityStr = restTemplate.
                    postForEntity(testCommsFullUri, request, String.class);
            String testReturn = responseEntityStr.getBody();
            return new ResponseEntity<>(testReturn, HttpStatus.OK);
        } catch(HttpClientErrorException e) {
            return new ResponseEntity<>("Odbila nas bolnica", HttpStatus.BAD_REQUEST);
        }
    }
}