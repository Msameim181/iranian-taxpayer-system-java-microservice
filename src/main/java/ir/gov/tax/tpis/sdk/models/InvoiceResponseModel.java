package ir.gov.tax.tpis.sdk.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InvoiceResponseModel {
    private final String data;
    private final String uid;
    private final String referenceNumber;
    private final String taxId;
}
