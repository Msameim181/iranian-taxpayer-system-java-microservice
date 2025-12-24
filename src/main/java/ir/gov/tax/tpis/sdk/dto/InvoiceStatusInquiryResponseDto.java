package ir.gov.tax.tpis.sdk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceStatusInquiryResponseDto {
    private String taxId;
    private String invoiceStatus;
    private String article6Status;
    private String error;
}