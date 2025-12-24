package ir.gov.tax.tpis.sdk;

import ir.gov.tax.tpis.sdk.clients.TaxApi;
import ir.gov.tax.tpis.sdk.clients.TaxPublicApi;
import ir.gov.tax.tpis.sdk.cryptography.Encryptor;
import ir.gov.tax.tpis.sdk.cryptography.Signatory;
import ir.gov.tax.tpis.sdk.dto.*;
import ir.gov.tax.tpis.sdk.enums.PaymentMethod;
import ir.gov.tax.tpis.sdk.factories.EncryptorFactory;
import ir.gov.tax.tpis.sdk.factories.Pkcs8SignatoryFactory;
import ir.gov.tax.tpis.sdk.factories.TaxApiFactory;
import ir.gov.tax.tpis.sdk.models.*;
import ir.gov.tax.tpis.sdk.properties.TaxProperties;
import ir.gov.tax.tpis.sdk.providers.CurrentDateProviderImpl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public enum Test {
    ;
    private final static String certificatePath = "src/test/java/ir/gov/tax/tpis/sdk/cryptography/57490845111.cer";
    private final static String privateKeyPath = "src/test/java/ir/gov/tax/tpis/sdk/cryptography/57490845111.pem";
    private final static String memoryId = "A111H1";

    public static void main(final String[] args) throws Exception {
        final Signatory signatory = new Pkcs8SignatoryFactory(new CurrentDateProviderImpl()).create(privateKeyPath, certificatePath);
        final TaxApiFactory taxApiFactory =
                new TaxApiFactory("http://localhost:8055/requestsmanager", new TaxProperties(memoryId));
        final TaxPublicApi publicApi = taxApiFactory.createPublicApi(signatory);
        final ServerInformationModel serverInformation1 = publicApi.getServerInformation();
        final Encryptor encryptor = new EncryptorFactory().create(publicApi);
        final TaxApi taxApi = taxApiFactory.createApi(signatory, encryptor);
        final ServerInformationModel serverInformation = publicApi.getServerInformation();
        final List<InquiryResultModel> inquiryResultModels =
                taxApi.inquiryByTime(InquiryDto.builder().start(ZonedDateTime.of(LocalDateTime.of(2025, 2, 14, 0, 0), ZoneId.systemDefault())).build());
        final List<InquiryResultModel> inquiryRangeResultModels =
                taxApi.inquiryByTime(InquiryDto.builder().start(ZonedDateTime.of(LocalDateTime.of(2025, 2, 14, 0, 0), ZoneId.systemDefault())).end(ZonedDateTime.now()).build());
            final List<InvoiceDto> invoices =
                    IntStream.range(0, 1).mapToObj(x -> new SampleInvoiceCreator().getInvoiceDto()).collect(
                            Collectors.toList());
            final List<InvoiceResponseModel> responseModels = taxApi.sendInvoices(invoices);
            Thread.sleep(500);
            final List<String> uidList = responseModels.stream().map(InvoiceResponseModel::getUid).collect(Collectors.toList());
            final List<String> referenceNumbers =
                    responseModels.stream().map(InvoiceResponseModel::getReferenceNumber).collect(Collectors.toList());

            final List<InquiryResultModel> inquiryUidResultModels =
                    taxApi.inquiryByUid(InquiryByUidDto.builder().uidList(uidList).fiscalId(memoryId).build());
            final List<InquiryResultModel> inquiryByReferenceIdModels =
                    taxApi.inquiryByReferenceId(InquiryByReferenceNumberDto.builder()
                                                                           .referenceNumbers(referenceNumbers)
                                                                           .build());
            Thread.sleep(100);

        final TaxpayerModel taxpayer = taxApi.getTaxpayer("14003778990");
        final FiscalFullInformationModel fiscalFullInformationModel = taxApi.getFiscalInformation(memoryId);

        RegisterPaymentResultModel paymentResponse =
                taxApi.registerPaymentRequest(RegisterPaymentRequestDto.builder().taxId("A111H104EA6001D0B32AC6")
                                                                       .paymentDate(17595000L)
                                                                       .paidAmount(5L)
                                                                       .paymentMethod(PaymentMethod.INTERNET)
                                                                       .build());

    }
}