package ir.gov.tax.tpis.sdk.providers;

import java.time.Instant;

public interface TaxIdProvider {
    String generateTaxId(String memoryId, long serial, Instant createDate);
}
