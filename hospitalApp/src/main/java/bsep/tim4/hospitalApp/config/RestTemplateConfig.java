package bsep.tim4.hospitalApp.config;

import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@Configuration
public class RestTemplateConfig {

    @Value("${trust.store.name}")
    private String trustStoreName;

    @Value("${trust.store.password}")
    private String trustStorePass;

    @Bean
    RestTemplate restTemplate() throws KeyStoreException, IOException, NoSuchAlgorithmException,
            KeyManagementException, CertificateException {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(new FileInputStream(new ClassPathResource(trustStoreName).getFile()),
                (trustStorePass).toCharArray());

        SSLContext sslContext = new SSLContextBuilder()
                .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();

        CloseableHttpClient httpClient = HttpClients.custom().setSSLContext(sslContext).build();
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(requestFactory);
    }
}
