package ir.gov.tax.tpis.sdk.factories;

import ir.gov.tax.tpis.sdk.cryptography.Signatory;
import ir.gov.tax.tpis.sdk.providers.CurrentDateProvider;
import ir.gov.tax.tpis.sdk.providers.CurrentDateProviderImpl;
import lombok.extern.slf4j.Slf4j;

import java.security.*;
import java.security.cert.X509Certificate;

@Slf4j
public class Pkcs11SignatoryFactory {

    private final SignatoryFactory signatoryFactory;

    public Pkcs11SignatoryFactory() {
        this(new CurrentDateProviderImpl());
    }

    public Pkcs11SignatoryFactory(final CurrentDateProviderImpl currentDateProvider) {
        this.signatoryFactory = new SignatoryFactory(currentDateProvider);
    }

    public Signatory create(final String alias, final String pin, final String pkcs11Path) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
        final KeyStoreFactory keyStoreFactory = new KeyStoreFactory(pkcs11Path, () -> pin);
        final KeyStore keyStore = keyStoreFactory.getKeyStore();
        final X509Certificate certificate = (X509Certificate) keyStore.getCertificate(alias);
        final PrivateKey key = (PrivateKey) keyStore.getKey(alias, pin.toCharArray());
        return this.signatoryFactory.create(key, certificate);
    }

}
