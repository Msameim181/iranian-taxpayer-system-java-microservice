package ir.gov.tax.tpis.sdk.middlewares;

public class EmptyMiddleware extends Middleware{
    @Override
    public String handle(String text) {
        return handleNext(text);
    }
}
