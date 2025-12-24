package ir.gov.tax.tpis.sdk.models;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder(toBuilder = true)
@Jacksonized
public class TaxpayerModel {
    private String nameTrade;
    private String taxpayerStatus;
    private String taxpayerType;
    private String postalcodeTaxpayer;
    private String addressTaxpayer;
    private String nationalId;

}
