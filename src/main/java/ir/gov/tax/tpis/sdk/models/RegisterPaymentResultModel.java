package ir.gov.tax.tpis.sdk.models;

import ir.gov.tax.tpis.sdk.enums.RequestStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Getter
@Builder(toBuilder = true)
@Jacksonized
public class RegisterPaymentResultModel {
    private RequestStatus requestStatus;
    private List<ErrorModel> error;
}
