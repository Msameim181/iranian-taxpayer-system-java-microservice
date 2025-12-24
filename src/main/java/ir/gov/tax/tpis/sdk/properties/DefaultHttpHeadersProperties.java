package ir.gov.tax.tpis.sdk.properties;

import java.util.Map;

public class DefaultHttpHeadersProperties implements HttpHeadersProperties {
    private static final String AUTHORIZATION = "Authorization";
    @Override
    public String getAuthorizationHeaderName() {
        return AUTHORIZATION;
    }

    @Override
    public Map<String, String> getCustomHeaders() {
        return Map.of();
    }
}
