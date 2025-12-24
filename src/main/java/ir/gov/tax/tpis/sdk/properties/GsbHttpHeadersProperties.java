package ir.gov.tax.tpis.sdk.properties;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class GsbHttpHeadersProperties implements HttpHeadersProperties {
    private static final String TOKEN = "Token";
    private final Map<String, String> customHeaders;
    @Override
    public String getAuthorizationHeaderName() {
        return TOKEN;
    }

    @Override
    public Map<String, String> getCustomHeaders() {
        return customHeaders;
    }
}
