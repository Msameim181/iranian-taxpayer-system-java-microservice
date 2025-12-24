package ir.gov.tax.tpis.sdk.cryptography;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmptyEncryptor implements Encryptor {
    @Override
    public String encrypt(final String text) {
        return text;
    }
}
