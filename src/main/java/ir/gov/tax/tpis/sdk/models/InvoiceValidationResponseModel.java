package ir.gov.tax.tpis.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Getter
@Builder
@Jacksonized
@JsonIgnoreProperties("confirmationReferenceId")
public class InvoiceValidationResponseModel {
    private final List<ErrorModel> error;
    private final List<ErrorModel> warning;
    private final boolean success;
}
