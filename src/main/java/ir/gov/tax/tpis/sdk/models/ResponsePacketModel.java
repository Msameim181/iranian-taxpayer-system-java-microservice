package ir.gov.tax.tpis.sdk.models;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class ResponsePacketModel {
    private final String uid;
    private final String packetType;
    private final String referenceNumber;
    private final String data;
}