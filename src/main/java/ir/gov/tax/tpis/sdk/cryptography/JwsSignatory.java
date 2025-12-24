package ir.gov.tax.tpis.sdk.cryptography;

import ir.gov.tax.tpis.sdk.providers.CurrentDateProvider;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.lang.JoseException;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class JwsSignatory implements Signatory {

    public static final String JOSE = "jose";
    public static final String SIG_T = "sigT";
    public static final String TYP = "typ";
    public static final String CRIT = "crit";
    public static final String CONTENT_TYPE_HEADER_VALUE = "text/plain";
    private final PrivateKey privateKey;
    private final X509Certificate certificate;
    private final CurrentDateProvider currentDateProvider;

    @SneakyThrows
    @Override
    public String sign(final String text) {
        final StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        try {
            return innerSign(text);
        } finally {
            log.debug("sign in {} ms", stopwatch.getTime(TimeUnit.MILLISECONDS));
        }
    }

    private String innerSign(final String text) throws JoseException {
        final JsonWebSignature jws = new JsonWebSignature();

        jws.setPayload(text);
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        jws.setKey(this.privateKey);
        jws.setCertificateChainHeaderValue(this.certificate);
        jws.setHeader(SIG_T, this.currentDateProvider.toDateFormat());
        jws.setHeader(TYP, JOSE);
        jws.setHeader(CRIT, new String[]{SIG_T});
        jws.setContentTypeHeaderValue(CONTENT_TYPE_HEADER_VALUE);

        jws.sign();

        return jws.getCompactSerialization();
    }
}
