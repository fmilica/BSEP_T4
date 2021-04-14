package bsep.tim4.hospitalApp.users.config;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;
import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

@Configuration
public class RestTemplateConfig {

    @Value("${trust.store}")
    private String trustStorePath;

    @Value("${trust.store.password}")
    private String trustStorePass;

    @Value("${pki.keystore-path}")
    private String keyStorePath;

    @Value("${pki.keystore-name}")
    private String keyStoreName;

    @Value("${pki.keystore-password}")
    private String keyStorePass;

    @Bean
    RestTemplate restTemplate() throws KeyStoreException, IOException, NoSuchAlgorithmException,
            UnrecoverableKeyException, KeyManagementException, CertificateException {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(new FileInputStream(new ClassPathResource(keyStoreName).getFile()),
                keyStorePass.toCharArray());
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(new FileInputStream(new ClassPathResource("truststoreHospital.jks").getFile()),
                trustStorePass.toCharArray());

        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
                new SSLContextBuilder()
                        .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
                        .loadKeyMaterial(keyStore, keyStorePass.toCharArray()).build());

        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(requestFactory);
    }
}
