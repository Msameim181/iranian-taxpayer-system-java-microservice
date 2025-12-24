package ir.gov.tax.tpis.sdk.factories;

import ir.gov.tax.tpis.sdk.cryptography.JwsSignatory;
import ir.gov.tax.tpis.sdk.cryptography.Signatory;
import ir.gov.tax.tpis.sdk.providers.CurrentDateProvider;
import lombok.RequiredArgsConstructor;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

@RequiredArgsConstructor
public class SignatoryFactory {
    private final CurrentDateProvider currentDateProvider;

    public Signatory create(final PrivateKey privateKey, final X509Certificate x509Certificate) {
        return new JwsSignatory(privateKey, x509Certificate, this.currentDateProvider);
    }
}
