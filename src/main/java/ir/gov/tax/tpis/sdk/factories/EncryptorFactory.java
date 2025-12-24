package ir.gov.tax.tpis.sdk.factories;

import ir.gov.tax.tpis.sdk.clients.TaxPublicApi;
import ir.gov.tax.tpis.sdk.cryptography.Encryptor;
import ir.gov.tax.tpis.sdk.cryptography.JweEncryptor;
import ir.gov.tax.tpis.sdk.models.KeyModel;
import ir.gov.tax.tpis.sdk.repositories.EncryptionKeyRepository;
import ir.gov.tax.tpis.sdk.repositories.EncryptionKeyRepositoryImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class EncryptorFactory {
    public Encryptor create(final TaxPublicApi publicApi) {
        final Supplier<List<KeyModel>> getPublicKeys = publicApi.getServerInformation()::getPublicKeys;
        return create(getPublicKeys);
    }

    @NotNull
    public Encryptor create(final Supplier<List<KeyModel>> getPublicKeys) {
        final EncryptionKeyRepository encryptionKeyRepository = new EncryptionKeyRepositoryImpl(getPublicKeys);
        return new JweEncryptor(encryptionKeyRepository);
    }
}
