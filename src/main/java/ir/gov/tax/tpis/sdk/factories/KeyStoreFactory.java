package ir.gov.tax.tpis.sdk.factories;

import ir.gov.tax.tpis.sdk.exceptions.Pkcs11SignatoryException;
import ir.gov.tax.tpis.sdk.initializers.SunPKCS11Initializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.PasswordCallback;
import java.security.KeyStore;
import java.security.Provider;
import java.security.Security;
import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public class KeyStoreFactory {
    private static final String SUN_PKCS11_KEYSTORE_TYPE = "PKCS11";
    private static final String NEW_LINE = "\n";
    private static final String DOUBLE_QUOTE = "\"";
    private final String pkcs11Path;
    private final Supplier<String> pinSupplier;
    private Provider provider;

    public KeyStore getKeyStore() throws Pkcs11SignatoryException {
        try {
            final KeyStore keyStore = KeyStore.getInstance(SUN_PKCS11_KEYSTORE_TYPE, getProvider());
            keyStore.load(this::getNoPasswordCallback);
            return keyStore;
        } catch (final Exception e) {
            if ("CKR_PIN_INCORRECT".equals(e.getMessage())) {
                throw new Pkcs11SignatoryException("Bad password for PKCS11", e);
            }
            throw new Pkcs11SignatoryException("Can't initialize Sun PKCS#11 security provider. Reason: " + e.getMessage(), e);
        }
    }

    private Provider getProvider() {
        if (this.provider == null) {
            final String configString = buildConfig();
            log.debug("PKCS11 Config : \n{}", configString);

            this.provider = SunPKCS11Initializer.getProvider(configString);

            if (this.provider == null) {
                throw new Pkcs11SignatoryException("Unable to create PKCS11 provider");
            }

            // we need to add the provider to be able to sign later
            Security.addProvider(this.provider);
        }
        return this.provider;
    }

    private String buildConfig() {
        final String aPKCS11LibraryFileName = escapePath();

        return "name = SmartCard" + UUID.randomUUID() +
                NEW_LINE + "library = " + DOUBLE_QUOTE + aPKCS11LibraryFileName +
                DOUBLE_QUOTE;
    }

    private String escapePath() {
        return this.pkcs11Path != null ? this.pkcs11Path.replace("\\", "\\\\") : "";
    }

    @NotNull
    private KeyStore.CallbackHandlerProtection getNoPasswordCallback() {
        return new KeyStore.CallbackHandlerProtection(callbacks -> {
            for (final Callback c : callbacks) {
                if (c instanceof PasswordCallback) {
                    ((PasswordCallback) c).setPassword(this.pinSupplier.get().toCharArray());
                    return;
                }
            }
            throw new Pkcs11SignatoryException("No password callback");
        });
    }
}
