package ir.gov.tax.tpis.sdk.exceptions;

import lombok.Getter;

@Getter
public class UnknownResponseException extends RuntimeException {
    private final int statusCode;
    private final String body;

    public UnknownResponseException(final int statusCode, final String body) {
        super(String.format("HTTP_%d: %s", statusCode, body));
        this.statusCode = statusCode;
        this.body = body;
    }
}
