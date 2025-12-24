package ir.gov.tax.tpis.sdk.factories;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.stream.Collectors;

public class X509CertificateFactory {
    private final CertificateFactory factory;

    public X509CertificateFactory() throws CertificateException {
        this.factory = CertificateFactory.getInstance("X.509");
    }

    public X509Certificate generateCertificate(final InputStream stream) throws CertificateException {
        return (X509Certificate) this.factory.generateCertificate(stream);
    }

    public X509Certificate generateCertificate(final String path) throws IOException, CertificateException {
        try (final InputStream stream = Files.newInputStream(Paths.get(path))) {
            return this.generateCertificate(new ByteArrayInputStream(getCertificateCleaned(stream).getBytes()));
        }
    }

    private static String getCertificateCleaned(final InputStream certificateStream) {
        final String text = new BufferedReader(new InputStreamReader(certificateStream, StandardCharsets.UTF_8)).lines()
                                                                                                                .collect(
                                                                                                                        Collectors.joining(
                                                                                                                                "\n"));

        if (!text.isEmpty() && text.charAt(0) == '-') {
            return text;
        }

        return "-----BEGIN CERTIFICATE-----\n" + text + "-----END CERTIFICATE-----";
    }
}
