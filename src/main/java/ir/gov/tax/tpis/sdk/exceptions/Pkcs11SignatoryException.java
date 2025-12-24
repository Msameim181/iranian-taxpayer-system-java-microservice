package ir.gov.tax.tpis.sdk.exceptions;

public class Pkcs11SignatoryException extends RuntimeException {

    public Pkcs11SignatoryException(final String message) {
        super(message);
    }

    public Pkcs11SignatoryException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
