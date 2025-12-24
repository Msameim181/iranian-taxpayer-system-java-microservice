package ir.gov.tax.tpis.sdk.properties;

import lombok.SneakyThrows;

public class DefaultUrlProperties implements UrlProperties {

    public static final String API = "api";
    public static final String SEPARATOR = "/";
    private static final String VERSION = "v2";
    private final String baseUrl;

    public DefaultUrlProperties(final String baseUrl) {
        final boolean endWithSlash = !baseUrl.isEmpty() && baseUrl.charAt(baseUrl.length() - 1) == '/';
        this.baseUrl = baseUrl + (endWithSlash ? "" : SEPARATOR) + API + SEPARATOR + VERSION + "/";
    }

    @Override
    @SneakyThrows
    public String getUrl(final String url) {
        return this.baseUrl + url;
    }
}
