package ir.gov.tax.tpis.sdk.microservice.config;

import ir.gov.tax.tpis.sdk.clients.TaxApi;
import ir.gov.tax.tpis.sdk.clients.TaxPublicApi;
import ir.gov.tax.tpis.sdk.cryptography.Encryptor;
import ir.gov.tax.tpis.sdk.cryptography.Signatory;
import ir.gov.tax.tpis.sdk.factories.EncryptorFactory;
import ir.gov.tax.tpis.sdk.factories.Pkcs8SignatoryFactory;
import ir.gov.tax.tpis.sdk.factories.TaxApiFactory;
import ir.gov.tax.tpis.sdk.properties.TaxProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

@Configuration
public class TaxApiConfig {

    private static final Logger logger = LoggerFactory.getLogger(TaxApiConfig.class);

    @Value("${tax.api.base-url:https://tp.tax.gov.ir/requestsmanager}")
    private String baseUrl;

    @Value("${tax.api.memory-id}")
    private String memoryId;

    @Value("${tax.api.private-key-path}")
    private String privateKeyPath;

    @Value("${tax.api.certificate-path}")
    private String certificatePath;

    @PostConstruct
    public void init() {
        logger.info("Initializing Tax API with base URL: {}", baseUrl);
        logger.info("Memory ID: {}", memoryId);
        logger.info("Private key path: {}", privateKeyPath);
        logger.info("Certificate path: {}", certificatePath);
    }

    @Bean
    public Signatory signatory() throws IOException, CertificateException, NoSuchAlgorithmException, InvalidKeySpecException {
        logger.info("Creating signatory from PKCS#8 key and certificate");
        return new Pkcs8SignatoryFactory()
                .create(privateKeyPath, certificatePath);
    }

    @Bean
    public TaxApiFactory taxApiFactory() {
        return new TaxApiFactory(baseUrl, new TaxProperties(memoryId));
    }

    @Bean
    public TaxPublicApi taxPublicApi(TaxApiFactory factory, Signatory signatory) {
        logger.info("Creating TaxPublicApi");
        return factory.createPublicApi(signatory);
    }

    @Bean
    public Encryptor encryptor(TaxPublicApi publicApi) {
        logger.info("Creating encryptor from server public keys");
        return new EncryptorFactory().create(publicApi);
    }

    @Bean
    public TaxApi taxApi(TaxApiFactory factory, Signatory signatory, Encryptor encryptor) {
        logger.info("Creating TaxApi");
        return factory.createApi(signatory, encryptor);
    }
}
