package ir.gov.tax.tpis.sdk.models;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder(toBuilder = true)
@Jacksonized
public class InquiryResultModel {
    private final String referenceNumber;
    private final String uid;
    private final String status;
    private final InvoiceValidationResponseModel data;
    private final String packetType;
    private final String fiscalId;
    private final String sign;
}
