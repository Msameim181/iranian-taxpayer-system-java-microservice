package ir.gov.tax.tpis.sdk.microservice.service;

import ir.gov.tax.tpis.sdk.clients.TaxApi;
import ir.gov.tax.tpis.sdk.clients.TaxPublicApi;
import ir.gov.tax.tpis.sdk.dto.InquiryByReferenceNumberDto;
import ir.gov.tax.tpis.sdk.dto.InquiryByUidDto;
import ir.gov.tax.tpis.sdk.dto.InquiryDto;
import ir.gov.tax.tpis.sdk.dto.InvoiceDto;
import ir.gov.tax.tpis.sdk.dto.RegisterPaymentRequestDto;
import ir.gov.tax.tpis.sdk.models.FiscalFullInformationModel;
import ir.gov.tax.tpis.sdk.models.InquiryResultModel;
import ir.gov.tax.tpis.sdk.models.InvoiceResponseModel;
import ir.gov.tax.tpis.sdk.models.RegisterPaymentResultModel;
import ir.gov.tax.tpis.sdk.models.ServerInformationModel;
import ir.gov.tax.tpis.sdk.models.TaxpayerModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaxService {

    private static final Logger logger = LoggerFactory.getLogger(TaxService.class);

    private final TaxApi taxApi;
    private final TaxPublicApi taxPublicApi;

    public TaxService(TaxApi taxApi, TaxPublicApi taxPublicApi) {
        this.taxApi = taxApi;
        this.taxPublicApi = taxPublicApi;
    }

    public ServerInformationModel getServerInformation() {
        logger.info("Getting server information");
        return taxPublicApi.getServerInformation();
    }

    public List<InvoiceResponseModel> sendInvoices(List<InvoiceDto> invoices) {
        logger.info("Sending {} invoices", invoices.size());
        List<InvoiceResponseModel> responses = taxApi.sendInvoices(invoices);
        logger.info("Received {} responses", responses.size());
        return responses;
    }

    public List<InquiryResultModel> inquiryByTime(InquiryDto inquiryDto) {
        logger.info("Querying invoices by time range");
        return taxApi.inquiryByTime(inquiryDto);
    }

    public List<InquiryResultModel> inquiryByUid(InquiryByUidDto inquiryDto) {
        logger.info("Querying invoices by UID: {}", inquiryDto.getUidList());
        return taxApi.inquiryByUid(inquiryDto);
    }

    public List<InquiryResultModel> inquiryByReferenceId(InquiryByReferenceNumberDto inquiryDto) {
        logger.info("Querying invoices by reference numbers: {}", inquiryDto.getReferenceNumbers());
        return taxApi.inquiryByReferenceId(inquiryDto);
    }

    public TaxpayerModel getTaxpayer(String economicCode) {
        logger.info("Getting taxpayer info for economic code: {}", economicCode);
        return taxApi.getTaxpayer(economicCode);
    }

    public FiscalFullInformationModel getFiscalInformation(String memoryId) {
        logger.info("Getting fiscal information for memory ID: {}", memoryId);
        return taxApi.getFiscalInformation(memoryId);
    }

    public RegisterPaymentResultModel registerPayment(RegisterPaymentRequestDto paymentRequest) {
        logger.info("Registering payment for tax ID: {}", paymentRequest.getTaxId());
        return taxApi.registerPaymentRequest(paymentRequest);
    }
}
