package ir.gov.tax.tpis.sdk.models;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder(toBuilder = true)
@Jacksonized
public class TaxpayerInfoModel {
    private String taxpayerName;
    private Integer taxpayerProfileStatus;
    private Boolean taxpayerCallStatus;
    private Boolean taxpayerArticle2Status;
}
