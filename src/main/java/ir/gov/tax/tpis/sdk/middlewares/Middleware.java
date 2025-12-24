package ir.gov.tax.tpis.sdk.middlewares;

public abstract class Middleware {
    private Middleware next;

    public static Middleware link(Middleware first, Middleware... chain) {
        Middleware head = first;
        for (Middleware nextInChain: chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    public abstract String handle(String text);

    protected String handleNext(String text) {
        if (next == null) {
            return text;
        }
        return next.handle(text);
    }
}
