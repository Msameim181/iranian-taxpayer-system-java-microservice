package ir.gov.tax.tpis.sdk.models;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Getter
@Builder
@Jacksonized
public class BatchResponseModel {
    private final long timestamp;
    private final List<ResponsePacketModel> result;
}
