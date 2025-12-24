package ir.gov.tax.tpis.sdk.providers;

import ir.gov.tax.tpis.sdk.dto.InquiryByReferenceNumberDto;
import ir.gov.tax.tpis.sdk.dto.InquiryDto;
import ir.gov.tax.tpis.sdk.dto.InquiryByUidDto;
import ir.gov.tax.tpis.sdk.dto.RegisterPaymentRequestDto;
import okhttp3.HttpUrl;

import java.util.List;

public interface HttpUrlProvider {
    HttpUrl getInquiryHttpUrl(InquiryDto dto);

    HttpUrl getInquiryByUidHttpUrl(InquiryByUidDto dto);

    HttpUrl getInquiryByReferenceIdHttpUrl(InquiryByReferenceNumberDto dto);

    HttpUrl getTaxpayerHttpUrl(String economicCode);

    HttpUrl getTaxpayerInfoHttpUrl(String economicCode);

    HttpUrl getArticle6Status(String economicCode, Long vatValue, Integer period);

    HttpUrl getFiscalInformationHttpUrl(String memoryId);

    HttpUrl getNonceHttpUrl();

    HttpUrl getServerInformationHttpUrl();

    HttpUrl getInvoiceHttpUrl();

    HttpUrl getInquiryInvoiceStatusHttpUrl(List<String> taxIds);
    HttpUrl getRegisterPaymentHttpUrl (RegisterPaymentRequestDto paymentRequest);
}
