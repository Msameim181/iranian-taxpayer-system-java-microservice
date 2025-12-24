package ir.gov.tax.tpis.sdk.providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.gov.tax.tpis.sdk.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@RequiredArgsConstructor
public class RequestProviderImpl implements RequestProvider {
    private static final String MEDIA_TYPE = "application/json; charset=utf-8";
    private final ObjectMapper mapper;
    private final HttpUrlProvider httpUrlProvider;

    @Override
    public Request getNonceRequest() {
        return new Request.Builder().url(this.httpUrlProvider.getNonceHttpUrl()).get().build();
    }

    @Override
    public Request getServerInformationRequest() {
        final HttpUrl requestUrl = this.httpUrlProvider.getServerInformationHttpUrl();
        return getRequest(requestUrl);
    }

    @Override
    public Request getInquiryRequest(final InquiryDto dto) {
        return getRequest(this.httpUrlProvider.getInquiryHttpUrl(dto));
    }

    @Override
    public Request getInquiryByUidRequest(final InquiryByUidDto dto) {
        return getRequest(this.httpUrlProvider.getInquiryByUidHttpUrl(dto));
    }

    @Override
    public Request getInquiryByReferenceIdRequest(final InquiryByReferenceNumberDto dto) {
        return getRequest(this.httpUrlProvider.getInquiryByReferenceIdHttpUrl(dto));
    }

    @Override
    public Request getTaxpayerRequest(final String economicCode) {
        return getRequest(this.httpUrlProvider.getTaxpayerHttpUrl(economicCode));
    }

    @Override
    public Request getTaxpayerInfoRequest(final String economicCode) {
        return getRequest(this.httpUrlProvider.getTaxpayerInfoHttpUrl(economicCode));
    }

    @Override
    public Request getArticle6Status(final String economicCode, final Long vatValue, final Integer period) {
        return getRequest(this.httpUrlProvider.getArticle6Status(economicCode, vatValue, period));
    }

    @Override
    public Request getFiscalInformationRequest(final String memoryId) {
        return getRequest(this.httpUrlProvider.getFiscalInformationHttpUrl(memoryId));
    }

    @Override
    public Request getInvoicesRequest(final List<PacketDto> data) {
        final HttpUrl url = this.httpUrlProvider.getInvoiceHttpUrl();
        final RequestBody body = getRequestBody(data);
        return getRequest(url, body);
    }

    @Override
    public Request getInquiryInvoicesStatusRequest(final List<String> taxIds) {
        final HttpUrl url = this.httpUrlProvider.getInquiryInvoiceStatusHttpUrl(taxIds);
        return getRequest(url);
    }

    @Override
    public Request getPaymentRegisterRequest(final RegisterPaymentRequestDto paymentRequestDto) {
        final HttpUrl url = this.httpUrlProvider.getRegisterPaymentHttpUrl(paymentRequestDto);
        return getRequest(url, getRequestBody(paymentRequestDto));
    }

    @NotNull
    private static Request getRequest(final HttpUrl httpUrl) {
        return new Request.Builder().url(httpUrl).get().build();
    }

    @NotNull
    private static Request getRequest(final HttpUrl url, final RequestBody body) {
        return new Request.Builder().url(url).post(body).build();
    }

    @SneakyThrows
    @NotNull
    private <T> RequestBody getRequestBody(final T data) {
        final byte[] body = this.mapper.writeValueAsBytes(data);
        return RequestBody.create(body, MediaType.parse(MEDIA_TYPE));
    }

}
