package ir.gov.tax.tpis.sdk.clients;

import ir.gov.tax.tpis.sdk.dto.*;
import ir.gov.tax.tpis.sdk.models.*;

import java.util.List;

public interface TaxApi {
    FiscalFullInformationModel getFiscalInformation(String memoryId);

    TaxpayerModel getTaxpayer(String economicCode);

    List<InvoiceResponseModel> sendInvoices(List<InvoiceDto> invoices);

    List<InquiryResultModel> inquiryByTime(InquiryDto dto);

    List<InquiryResultModel> inquiryByUid(InquiryByUidDto dto);

    List<InquiryResultModel> inquiryByReferenceId(InquiryByReferenceNumberDto dto);

    RegisterPaymentResultModel registerPaymentRequest(RegisterPaymentRequestDto paymentRequest);

    List<InvoiceStatusInquiryResponseDto> getInvoiceStatusInquiry(List<String> taxIds);
}
