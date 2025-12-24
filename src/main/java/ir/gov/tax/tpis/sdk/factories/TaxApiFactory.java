package ir.gov.tax.tpis.sdk.factories;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.gov.tax.tpis.sdk.clients.*;
import ir.gov.tax.tpis.sdk.cryptography.Encryptor;
import ir.gov.tax.tpis.sdk.cryptography.Signatory;
import ir.gov.tax.tpis.sdk.properties.*;
import ir.gov.tax.tpis.sdk.providers.HttpUrlProviderImpl;
import ir.gov.tax.tpis.sdk.providers.PacketProvider;
import ir.gov.tax.tpis.sdk.providers.RequestProviderImpl;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class TaxApiFactory {
    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final PacketProviderFactory packetProviderFactory;
    private final TaxProperties properties;
    private final UrlProperties urlProperties;
    private final HttpHeadersProperties httpHeadersProperties;

    public TaxApiFactory(final String baseUrl, final TaxProperties properties) {
        this(new DefaultUrlProperties(baseUrl), properties, new DefaultHttpHeadersProperties());
    }

    public TaxApiFactory(final UrlProperties urlProperties,
                         final TaxProperties properties,
                         final HttpHeadersProperties httpHeadersProperties) {
        this.packetProviderFactory = new PacketProviderFactory(this.objectMapper, properties);
        this.properties = properties;
        this.urlProperties = urlProperties;
        this.httpHeadersProperties = httpHeadersProperties;

    }

    public TaxPublicApi createPublicApi(final Signatory signatory) {
        final LowLevelTaxApi lowLevelTaxApi = this.createLowLevelApi(signatory);
        return new TaxPublicApiImpl(lowLevelTaxApi);
    }

    public TaxApi createApi(final Signatory signatory, final Encryptor encryptor) {
        final LowLevelTaxApi lowLevelTaxApi = this.createLowLevelApi(signatory);
        final PacketProvider packetProvider = this.packetProviderFactory.createPacketProvider(signatory, encryptor);
        return new TaxApiImpl(lowLevelTaxApi, packetProvider);
    }

    public LowLevelTaxApi createLowLevelApi(final Signatory signatory) {
        final HttpUrlProviderImpl httpUrlProvider = createHttpUrlProvider();
        final RequestProviderImpl requestProvider = createRequestProvider(httpUrlProvider);
        final HttpClient httpClient = createHttpClient(signatory);
        return new LowLevelTaxApiImpl(httpClient, requestProvider);
    }

    @NotNull
    private HttpClient createHttpClient(final Signatory signatory) {
        final OkHttpClient okHttpClient = createOkHttpClient();
        return new HttpClient(signatory, this.objectMapper, okHttpClient, this.properties, this.httpHeadersProperties);
    }

    @NotNull
    private HttpUrlProviderImpl createHttpUrlProvider() {
        return new HttpUrlProviderImpl(this.urlProperties);
    }

    @NotNull
    private RequestProviderImpl createRequestProvider(final HttpUrlProviderImpl httpUrlProvider) {
        return new RequestProviderImpl(this.objectMapper, httpUrlProvider);
    }

    private static OkHttpClient createOkHttpClient() {
        return new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.MINUTES).build();
    }
}
