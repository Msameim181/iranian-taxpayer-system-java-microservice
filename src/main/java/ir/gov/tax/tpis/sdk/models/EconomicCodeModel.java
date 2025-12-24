package ir.gov.tax.tpis.sdk.models;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder(toBuilder = true)
@Jacksonized
public class EconomicCodeModel {
    private final String nameTrade;
    private final String taxpayerStatus;
    private final String taxpayerType;
    private final String postalcodeTaxpayer;
    private final String addressTaxpayer;
    private final String nationalId;
}
