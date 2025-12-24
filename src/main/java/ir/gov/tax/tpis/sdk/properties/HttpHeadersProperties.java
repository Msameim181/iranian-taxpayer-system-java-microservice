package ir.gov.tax.tpis.sdk.properties;

import java.util.Map;

public interface HttpHeadersProperties {
    String getAuthorizationHeaderName();
    Map<String, String> getCustomHeaders();
}
