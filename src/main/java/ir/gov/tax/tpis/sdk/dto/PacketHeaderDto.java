package ir.gov.tax.tpis.sdk.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@Builder(toBuilder = true)
public class PacketHeaderDto {
    private final String requestTraceId;
    private final String fiscalId;
}