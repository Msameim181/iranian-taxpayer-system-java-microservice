package ir.gov.tax.tpis.sdk.cryptography;

import ir.gov.tax.tpis.sdk.repositories.EncryptionKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class JweEncryptor implements Encryptor {
    private final EncryptionKeyRepository repository;

    @SneakyThrows
    @Override
    public String encrypt(final String text) {
        final StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        try {
            final JsonWebEncryption jwe = new JsonWebEncryption();

            jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.RSA_OAEP_256);
            jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_256_GCM);
            jwe.setPayload(text);
            jwe.setKey(this.repository.getKey());
            jwe.setKeyIdHeaderValue(this.repository.getKeyId());

            return jwe.getCompactSerialization();
        } finally {
            log.debug("encrypt in {} ms", stopwatch.getTime(TimeUnit.MILLISECONDS));
        }
    }
}
