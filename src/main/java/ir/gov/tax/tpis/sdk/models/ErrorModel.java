package ir.gov.tax.tpis.sdk.models;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@ToString
@Jacksonized
public class ErrorModel {
    private final String code;
    private final String message;
}
