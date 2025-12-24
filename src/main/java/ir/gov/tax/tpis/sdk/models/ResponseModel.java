package ir.gov.tax.tpis.sdk.models;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class ResponseModel {
    private final long timestamp;
    private final ResponsePacketModel result;
}
