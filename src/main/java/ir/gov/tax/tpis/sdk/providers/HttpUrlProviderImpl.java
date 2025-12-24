package ir.gov.tax.tpis.sdk.providers;

import ir.gov.tax.tpis.sdk.dto.InquiryByReferenceNumberDto;
import ir.gov.tax.tpis.sdk.dto.InquiryByUidDto;
import ir.gov.tax.tpis.sdk.dto.InquiryDto;
import ir.gov.tax.tpis.sdk.dto.RegisterPaymentRequestDto;
import ir.gov.tax.tpis.sdk.enums.PacketType;
import ir.gov.tax.tpis.sdk.properties.UrlProperties;
import lombok.RequiredArgsConstructor;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class HttpUrlProviderImpl implements HttpUrlProvider {
    public static final String MEMORY_ID = "memoryId";
    public static final String ECONOMIC_CODE = "economicCode";
    public static final String VAT_VALUE = "vatValue";
    public static final String PERIOD = "period";
    public static final String REFERENCE_IDS = "referenceIds";
    public static final String UID_LIST = "uidList";
    public static final String FISCAL_ID = "fiscalId";
    public static final String PAGE_SIZE = "pageSize";
    public static final String PAGE_NUMBER = "pageNumber";
    public static final String STATUS = "status";
    public static final String END = "end";
    public static final String START = "start";
    private final UrlProperties urlProperties;

    @Override
    public HttpUrl getInquiryHttpUrl(final InquiryDto dto) {
        final HttpUrl.Builder builder = getRequestUrl(PacketType.INQUIRY).newBuilder();
        addDate(builder, dto.getStart(), START);
        addDate(builder, dto.getEnd(), END);
        if (dto.getStatus() != null) {
            builder.addQueryParameter(STATUS, dto.getStatus().toString());
        }
        if (dto.getPageable() != null) {
            builder.addQueryParameter(PAGE_NUMBER, String.valueOf(dto.getPageable().getPageNumber()))
                   .addQueryParameter(PAGE_SIZE, String.valueOf(dto.getPageable().getPageSize()));
        }
        return builder.build();
    }

    @Override
    public HttpUrl getInquiryByUidHttpUrl(final InquiryByUidDto dto) {
        final HttpUrl.Builder builder = getRequestUrl(PacketType.INQUIRY_BY_UID).newBuilder();
        addDate(builder, dto.getStart(), START);
        addDate(builder, dto.getEnd(), END);
        builder.addQueryParameter(FISCAL_ID, dto.getFiscalId());
        dto.getUidList().forEach(x -> builder.addQueryParameter(UID_LIST, x));
        return builder.build();
    }

    @Override
    public HttpUrl getInquiryByReferenceIdHttpUrl(final InquiryByReferenceNumberDto dto) {
        final HttpUrl.Builder builder = getRequestUrl(PacketType.INQUIRY_BY_REFERENCE_ID).newBuilder();
        addDate(builder, dto.getStart(), START);
        addDate(builder, dto.getEnd(), END);
        dto.getReferenceNumbers().forEach(x -> builder.addQueryParameter(REFERENCE_IDS, x));
        return builder.build();
    }

    @Override
    public HttpUrl getTaxpayerHttpUrl(final String economicCode) {
        final HttpUrl.Builder builder = getRequestUrl(PacketType.GET_TAXPAYER).newBuilder();
        return builder.addQueryParameter(ECONOMIC_CODE, economicCode).build();
    }

    @Override
    public HttpUrl getTaxpayerInfoHttpUrl(final String economicCode) {
        final HttpUrl.Builder builder = getRequestUrl(PacketType.GET_TAXPAYER_INFORMATION).newBuilder();
        return builder.addQueryParameter(ECONOMIC_CODE, economicCode).build();
    }

    @Override
    public HttpUrl getArticle6Status(final String economicCode, final Long vatValue, final Integer period) {
        final HttpUrl.Builder builder = getRequestUrl(PacketType.GET_ARTICLE6_STATUS).newBuilder();
        builder.addQueryParameter(ECONOMIC_CODE, economicCode)
               .addQueryParameter(VAT_VALUE, String.valueOf(vatValue))
               .addQueryParameter(PERIOD, String.valueOf(period));
        return builder.build();
    }

    @Override
    public HttpUrl getFiscalInformationHttpUrl(final String memoryId) {
        final HttpUrl.Builder builder = getRequestUrl(PacketType.GET_FISCAL_INFORMATION).newBuilder();
        return builder.addQueryParameter(MEMORY_ID, memoryId).build();
    }

    @Override
    public HttpUrl getNonceHttpUrl() {
        return getRequestUrl(PacketType.NONCE);
    }

    @Override
    public HttpUrl getServerInformationHttpUrl() {
        return getRequestUrl(PacketType.GET_SERVER_INFORMATION);
    }

    @Override
    public HttpUrl getInvoiceHttpUrl() {
        return getRequestUrl(PacketType.INVOICE);
    }

    @Override
    public HttpUrl getInquiryInvoiceStatusHttpUrl(final List<String> taxIds) {
        final HttpUrl.Builder builder = getRequestUrl(PacketType.INQUIRY_INVOICE_STATUS).newBuilder();
        taxIds.forEach(x -> builder.addQueryParameter("taxIds", x));
        return builder.build();
    }

    @Override
    public HttpUrl getRegisterPaymentHttpUrl(final RegisterPaymentRequestDto paymentRequest) {
        return getRequestUrl(PacketType.INVOICE_PAYMENT);

    }

    private static void addDate(final HttpUrl.Builder builder, final ZonedDateTime date, final String name) {
        if (date != null) {
            builder.addQueryParameter(name, date.toOffsetDateTime().toString());
        }
    }


    @NotNull
    private HttpUrl getRequestUrl(final String request) {
        return Objects.requireNonNull(HttpUrl.parse(this.urlProperties.getUrl(request)));
    }
}
