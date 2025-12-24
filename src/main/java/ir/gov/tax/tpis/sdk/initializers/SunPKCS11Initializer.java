package ir.gov.tax.tpis.sdk.initializers;

import ir.gov.tax.tpis.sdk.exceptions.Pkcs11SignatoryException;

import java.security.Provider;
import java.security.Security;

public enum SunPKCS11Initializer {
    ;


    private static final String SUN_PKCS11_PROVIDERNAME = "SunPKCS11";

    SunPKCS11Initializer() {

    }

    public static Provider getProvider(final String configString) {
        try {
            final Provider provider = Security.getProvider(SUN_PKCS11_PROVIDERNAME);
            // "--" is permitted in the constructor sun.security.pkcs11.Config
            return provider.configure("--" + configString);
        } catch (final Exception e) {
            throw new Pkcs11SignatoryException("Unable to instantiate PKCS11 (JDK >= 9)", e);
        }
    }
}