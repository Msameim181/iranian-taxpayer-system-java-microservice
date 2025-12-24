package ir.gov.tax.tpis.sdk.repositories;

import ir.gov.tax.tpis.sdk.models.KeyModel;
import lombok.RequiredArgsConstructor;

import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class EncryptionKeyRepositoryImpl implements EncryptionKeyRepository {

    public final Random random = new Random();
    private final Supplier<List<KeyModel>> builder;
    private Key key;
    private String keyId;
    private ZonedDateTime expiredTime;

    @Override
    public Key getKey() {
        if (needRefresh()) {
            refresh();
        }
        return this.key;
    }

    @Override
    public String getKeyId() {
        if (needRefresh()) {
            refresh();
        }
        return this.keyId;
    }

    private static PublicKey getPublicKeyFromBase64(final String key) {
        try {
            final byte[] byteKey = Base64.getDecoder().decode(key);
            final X509EncodedKeySpec x509publicKey = new X509EncodedKeySpec(byteKey);
            final KeyFactory kf = KeyFactory.getInstance("RSA");

            return kf.generatePublic(x509publicKey);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private synchronized void refresh() {
        if (!needRefresh()) {
            return;
        }
        final List<KeyModel> keyModels = this.builder.get();
        final KeyModel keyModel = keyModels.get(this.random.nextInt(keyModels.size()));
        this.key = getPublicKeyFromBase64(keyModel.getKey());
        this.keyId = keyModel.getId();
        this.expiredTime = ZonedDateTime.now().plusHours(1L);
    }

    private boolean needRefresh() {
        return this.expiredTime == null || ZonedDateTime.now().isAfter(this.expiredTime);
    }
}
