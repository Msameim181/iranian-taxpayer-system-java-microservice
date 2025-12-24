package ir.gov.tax.tpis.sdk.clients;

import ir.gov.tax.tpis.sdk.dto.*;
import ir.gov.tax.tpis.sdk.models.*;

import java.util.List;

public interface LowLevelTaxApi {
    ServerInformationModel getServerInformation();

    FiscalFullInformationModel getFiscalInformation(String memoryId);

    TaxpayerModel getTaxpayer(String economicCode);

    TaxpayerInfoModel getTaxpayerInfo(String economicCode);

    Article6StatusResponseDto getArticle6Status(String economicCode, Long vatValue, Integer period);

    BatchResponseModel sendInvoices(List<PacketDto> packets);

    List<InquiryResultModel> inquiryByTime(InquiryDto dto);

    List<InquiryResultModel> inquiryByUid(InquiryByUidDto dto);

    List<InquiryResultModel> inquiryByReferenceId(InquiryByReferenceNumberDto dto);

    List<InvoiceStatusInquiryResponseDto> inquiryInvoiceStatus(List<String> taxIds);

    RegisterPaymentResultModel registerPaymentRequest(final RegisterPaymentRequestDto paymentRequestDto);
}
