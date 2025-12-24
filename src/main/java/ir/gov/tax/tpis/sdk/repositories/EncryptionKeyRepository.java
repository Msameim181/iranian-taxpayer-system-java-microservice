package ir.gov.tax.tpis.sdk.repositories;

public interface EncryptionKeyRepository {

    java.security.Key getKey();

    String getKeyId();
}
