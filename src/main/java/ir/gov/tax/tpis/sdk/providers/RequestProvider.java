package ir.gov.tax.tpis.sdk.providers;

import ir.gov.tax.tpis.sdk.dto.*;
import okhttp3.Request;

import java.util.List;

public interface RequestProvider {
    Request getNonceRequest();
    Request getServerInformationRequest();

    Request getInquiryRequest(final InquiryDto dto);

    Request getInquiryByUidRequest(final InquiryByUidDto dto);

    Request getInquiryByReferenceIdRequest(final InquiryByReferenceNumberDto dto);

    Request getTaxpayerRequest(final String economicCode);

    Request getTaxpayerInfoRequest(final String economicCode);

    Request getArticle6Status(final String economicCode, final Long vatValue, final Integer period);

    Request getFiscalInformationRequest(final String memoryId);

    Request getInvoicesRequest(final List<PacketDto> data);

    Request getInquiryInvoicesStatusRequest(final List<String> taxIds);

    Request getPaymentRegisterRequest (final RegisterPaymentRequestDto paymentRequestDto);
}
