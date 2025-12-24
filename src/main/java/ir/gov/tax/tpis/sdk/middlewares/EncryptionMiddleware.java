package ir.gov.tax.tpis.sdk.middlewares;

import ir.gov.tax.tpis.sdk.cryptography.Encryptor;

public class EncryptionMiddleware extends Middleware {
    private final Encryptor encryptor;

    public EncryptionMiddleware(Encryptor encryptor) {
        this.encryptor = encryptor;
    }

    @Override
    public String handle(String text) {
        return handleNext(encryptor.encrypt(text));
    }
}
