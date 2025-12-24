package ir.gov.tax.tpis.sdk.microservice.controller;

import ir.gov.tax.tpis.sdk.dto.InquiryByReferenceNumberDto;
import ir.gov.tax.tpis.sdk.dto.InquiryByUidDto;
import ir.gov.tax.tpis.sdk.dto.InquiryDto;
import ir.gov.tax.tpis.sdk.dto.InvoiceDto;
import ir.gov.tax.tpis.sdk.dto.RegisterPaymentRequestDto;
import ir.gov.tax.tpis.sdk.microservice.service.TaxService;
import ir.gov.tax.tpis.sdk.models.FiscalFullInformationModel;
import ir.gov.tax.tpis.sdk.models.InquiryResultModel;
import ir.gov.tax.tpis.sdk.models.InvoiceResponseModel;
import ir.gov.tax.tpis.sdk.models.RegisterPaymentResultModel;
import ir.gov.tax.tpis.sdk.models.ServerInformationModel;
import ir.gov.tax.tpis.sdk.models.TaxpayerModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TaxController {

    private final TaxService taxService;

    public TaxController(TaxService taxService) {
        this.taxService = taxService;
    }

    @GetMapping("/server/info")
    public ResponseEntity<ServerInformationModel> getServerInfo() {
        return ResponseEntity.ok(taxService.getServerInformation());
    }

    @PostMapping("/invoices")
    public ResponseEntity<List<InvoiceResponseModel>> sendInvoices(@RequestBody List<InvoiceDto> invoices) {
        return ResponseEntity.ok(taxService.sendInvoices(invoices));
    }

    @PostMapping("/invoices/inquiry/time")
    public ResponseEntity<List<InquiryResultModel>> inquiryByTime(@RequestBody InquiryDto inquiryDto) {
        return ResponseEntity.ok(taxService.inquiryByTime(inquiryDto));
    }

    @PostMapping("/invoices/inquiry/uid")
    public ResponseEntity<List<InquiryResultModel>> inquiryByUid(@RequestBody InquiryByUidDto inquiryDto) {
        return ResponseEntity.ok(taxService.inquiryByUid(inquiryDto));
    }

    @PostMapping("/invoices/inquiry/reference")
    public ResponseEntity<List<InquiryResultModel>> inquiryByReference(@RequestBody InquiryByReferenceNumberDto inquiryDto) {
        return ResponseEntity.ok(taxService.inquiryByReferenceId(inquiryDto));
    }

    @GetMapping("/taxpayers/{economicCode}")
    public ResponseEntity<TaxpayerModel> getTaxpayer(@PathVariable String economicCode) {
        return ResponseEntity.ok(taxService.getTaxpayer(economicCode));
    }

    @GetMapping("/fiscal/{memoryId}")
    public ResponseEntity<FiscalFullInformationModel> getFiscalInfo(@PathVariable String memoryId) {
        return ResponseEntity.ok(taxService.getFiscalInformation(memoryId));
    }

    @PostMapping("/payments")
    public ResponseEntity<RegisterPaymentResultModel> registerPayment(@RequestBody RegisterPaymentRequestDto paymentRequest) {
        return ResponseEntity.ok(taxService.registerPayment(paymentRequest));
    }
}
