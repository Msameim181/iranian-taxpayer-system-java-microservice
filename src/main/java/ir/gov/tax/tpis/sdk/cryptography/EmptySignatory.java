package ir.gov.tax.tpis.sdk.cryptography;

public class EmptySignatory implements Signatory{
    @Override
    public String sign(final String text) {
        return text;
    }
}
