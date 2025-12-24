package ir.gov.tax.tpis.sdk.dto;

import ir.gov.tax.tpis.sdk.enums.PaymentMethod;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder(toBuilder = true)
@Jacksonized

public class RegisterPaymentRequestDto {
    private String taxId;

    private Long paidAmount;

    private Long paymentDate;

    private PaymentMethod paymentMethod;

    private String terminalNumber;

    private String referenceNumber;
}
