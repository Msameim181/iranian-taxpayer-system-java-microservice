package ir.gov.tax.tpis.sdk.models;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder(toBuilder = true)
@Jacksonized
public class FiscalFullInformationModel {
    private String nameTrade;
    private String fiscalStatus;
    private String saleThreshold;
    private String economicCode;
    private String nationalId;
}
