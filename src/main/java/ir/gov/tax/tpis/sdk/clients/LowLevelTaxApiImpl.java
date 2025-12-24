package ir.gov.tax.tpis.sdk.clients;

import com.fasterxml.jackson.core.type.TypeReference;
import ir.gov.tax.tpis.sdk.dto.*;
import ir.gov.tax.tpis.sdk.models.*;
import ir.gov.tax.tpis.sdk.providers.RequestProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class LowLevelTaxApiImpl implements LowLevelTaxApi {
    private final HttpClient httpClient;
    private final RequestProvider requestProvider;

    @Override
    public ServerInformationModel getServerInformation() {
        final Request request = this.requestProvider.getServerInformationRequest();
        return sendRequest(request, ServerInformationModel.class);
    }

    @Override
    public FiscalFullInformationModel getFiscalInformation(final String memoryId) {
        final Request request = this.requestProvider.getFiscalInformationRequest(memoryId);
        return sendRequest(request, FiscalFullInformationModel.class);
    }

    @Override
    public TaxpayerModel getTaxpayer(final String economicCode) {
        final Request request = this.requestProvider.getTaxpayerRequest(economicCode);
        return sendRequest(request, TaxpayerModel.class);
    }

    @Override
    public TaxpayerInfoModel getTaxpayerInfo(final String economicCode) {
        final Request request = this.requestProvider.getTaxpayerInfoRequest(economicCode);
        return sendRequest(request, TaxpayerInfoModel.class);

    }

    @Override
    public Article6StatusResponseDto getArticle6Status(final String economicCode,
                                                       final Long vatValue,
                                                       final Integer period) {
        final Request request = this.requestProvider.getArticle6Status(economicCode, vatValue, period);
        return sendRequest(request, Article6StatusResponseDto.class);

    }

    @Override
    public BatchResponseModel sendInvoices(final List<PacketDto> packets) {
        final Request request = this.requestProvider.getInvoicesRequest(packets);
        return sendRequest(request, BatchResponseModel.class);
    }

    @Override
    public List<InquiryResultModel> inquiryByTime(final InquiryDto dto) {
        final Request request = this.requestProvider.getInquiryRequest(dto);
        return sendRequest(request, new TypeReference<>() {
        });
    }

    @Override
    public List<InquiryResultModel> inquiryByUid(final InquiryByUidDto dto) {
        final Request request = this.requestProvider.getInquiryByUidRequest(dto);
        return sendRequest(request, new TypeReference<>() {
        });
    }

    @Override
    public List<InquiryResultModel> inquiryByReferenceId(final InquiryByReferenceNumberDto dto) {
        final Request request = this.requestProvider.getInquiryByReferenceIdRequest(dto);
        return sendRequest(request, new TypeReference<>() {
        });
    }

    @Override
    public List<InvoiceStatusInquiryResponseDto> inquiryInvoiceStatus(final List<String> taxIds) {
        final Request request = this.requestProvider.getInquiryInvoicesStatusRequest(taxIds);
        return sendRequest(request, new TypeReference<>() {
        });
    }

    @Override
    public RegisterPaymentResultModel registerPaymentRequest(final RegisterPaymentRequestDto paymentRequestDto) {
        final Request request = this.requestProvider.getPaymentRegisterRequest(paymentRequestDto);
        return sendRequest(request, new TypeReference<>() {
        });
    }

    private <T> T sendRequest(final Request request, final TypeReference<T> valueTypeRef) {
        final Request nonceRequest = this.requestProvider.getNonceRequest();
        return this.httpClient.sendRequest(request, nonceRequest, valueTypeRef);
    }

    private <T> T sendRequest(final Request request, final Class<T> valueType) {
        final Request nonceRequest = this.requestProvider.getNonceRequest();
        return this.httpClient.sendRequest(request, nonceRequest, valueType);
    }
}
