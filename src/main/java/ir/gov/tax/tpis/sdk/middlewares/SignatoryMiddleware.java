package ir.gov.tax.tpis.sdk.middlewares;

import ir.gov.tax.tpis.sdk.cryptography.Signatory;

public class SignatoryMiddleware extends Middleware {
    private final Signatory signatory;

    public SignatoryMiddleware(Signatory signatory) {
        this.signatory = signatory;
    }

    @Override
    public String handle(String text) {
        return handleNext(signatory.sign(text));
    }
}
