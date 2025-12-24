package ir.gov.tax.tpis.sdk.models;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder(toBuilder = true)
@Jacksonized
public class KeyModel {
    private String key;
    private String id;
    private String algorithm;
    private int purpose;
}
