package ir.gov.tax.tpis.sdk.models;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenModel {
    private final String nonce;
    private final String clientId;
}
