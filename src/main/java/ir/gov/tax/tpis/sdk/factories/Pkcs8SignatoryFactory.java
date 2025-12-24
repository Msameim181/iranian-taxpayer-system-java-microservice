package ir.gov.tax.tpis.sdk.factories;

import ir.gov.tax.tpis.sdk.cryptography.Signatory;
import ir.gov.tax.tpis.sdk.providers.CurrentDateProviderImpl;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;

public class Pkcs8SignatoryFactory {
    private final SignatoryFactory signatoryFactory;
    private final X509CertificateFactory x509CertificateFactory;
    private final PrivateKeyFactory privateKeyFactory;

    public Pkcs8SignatoryFactory() throws CertificateException, NoSuchAlgorithmException {
        this(new CurrentDateProviderImpl());
    }

    public Pkcs8SignatoryFactory(final CurrentDateProviderImpl currentDateProvider) throws CertificateException, NoSuchAlgorithmException {
        this.signatoryFactory = new SignatoryFactory(currentDateProvider);
        this.x509CertificateFactory = new X509CertificateFactory();
        this.privateKeyFactory = new PrivateKeyFactory();
    }

    public Signatory create(final InputStream privateKeyStream,
                            final InputStream certificateStream) throws IOException, InvalidKeySpecException, CertificateException {
        final PrivateKey privateKey = this.privateKeyFactory.fromPkcs8(privateKeyStream);
        final X509Certificate x509Certificate = this.x509CertificateFactory.generateCertificate(certificateStream);
        return this.signatoryFactory.create(privateKey, x509Certificate);
    }

    public Signatory create(final String privateKeyPath,
                            final String certificatePath) throws IOException, InvalidKeySpecException, CertificateException {
        final PrivateKey privateKey = this.privateKeyFactory.fromPkcs8(privateKeyPath);
        final X509Certificate x509Certificate = this.x509CertificateFactory.generateCertificate(certificatePath);
        return this.signatoryFactory.create(privateKey, x509Certificate);
    }
}
