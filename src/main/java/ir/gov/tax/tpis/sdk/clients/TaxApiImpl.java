package ir.gov.tax.tpis.sdk.clients;

import ir.gov.tax.tpis.sdk.dto.*;
import ir.gov.tax.tpis.sdk.models.*;
import ir.gov.tax.tpis.sdk.providers.PacketProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TaxApiImpl implements TaxApi {
    private final LowLevelTaxApi lowLevelApi;
    private final PacketProvider packetFactory;

    @Override
    public List<InquiryResultModel> inquiryByTime(final InquiryDto dto) {
        return this.lowLevelApi.inquiryByTime(dto);
    }

    @Override
    public List<InquiryResultModel> inquiryByUid(final InquiryByUidDto dto) {
        return this.lowLevelApi.inquiryByUid(dto);
    }

    @Override
    public List<InquiryResultModel> inquiryByReferenceId(final InquiryByReferenceNumberDto dto) {
        return this.lowLevelApi.inquiryByReferenceId(dto);
    }

    @Override
    public RegisterPaymentResultModel registerPaymentRequest(RegisterPaymentRequestDto paymentRequest) {
        return this.lowLevelApi.registerPaymentRequest(paymentRequest);
    }

    @Override
    public List<InvoiceStatusInquiryResponseDto> getInvoiceStatusInquiry(final List<String> taxIds) {
        return this.lowLevelApi.inquiryInvoiceStatus(taxIds);
    }

    @Override
    public FiscalFullInformationModel getFiscalInformation(final String memoryId) {
        return this.lowLevelApi.getFiscalInformation(memoryId);
    }

    @Override
    public TaxpayerModel getTaxpayer(final String economicCode) {
        return this.lowLevelApi.getTaxpayer(economicCode);
    }

    @Override
    public List<InvoiceResponseModel> sendInvoices(final List<InvoiceDto> invoices) {
        final List<InvoicePacket> invoicePackets =
                invoices.stream().parallel().map(this::getInvoicePacket).collect(Collectors.toList());
        final BatchResponseModel response = send(invoicePackets);
        final Map<String, String> map = invoicePackets.stream()
                                                      .collect(Collectors.toMap(TaxApiImpl::getRequestTraceId,
                                                                                InvoicePacket::getTaxId));
        return response.getResult().stream().map(x -> getInvoiceResponseModel(map, x)).collect(Collectors.toList());
    }

    private static String getRequestTraceId(final InvoicePacket x) {
        return x.getPacketDto().getHeader().getRequestTraceId();
    }

    @NotNull
    private static InvoiceResponseModel getInvoiceResponseModel(final Map<String, String> map,
                                                                final ResponsePacketModel x) {
        return new InvoiceResponseModel(x.getData(), x.getUid(), x.getReferenceNumber(), map.get(x.getUid()));
    }

    private BatchResponseModel send(final List<InvoicePacket> invoicePackets) {
        final List<PacketDto> packets =
                invoicePackets.stream().map(InvoicePacket::getPacketDto).collect(Collectors.toList());
        return this.lowLevelApi.sendInvoices(packets);
    }

    @NotNull
    private InvoicePacket getInvoicePacket(final InvoiceDto x) {
        return new InvoicePacket(x.getHeader().getTaxid(), this.packetFactory.createInvoicePacket(x));
    }

    @Getter
    @RequiredArgsConstructor
    private static class InvoicePacket {
        private final String taxId;
        private final PacketDto packetDto;
    }
}
