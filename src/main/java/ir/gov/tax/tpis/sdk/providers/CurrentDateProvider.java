package ir.gov.tax.tpis.sdk.providers;

public interface CurrentDateProvider {
    Long toEpochMilli();

    String toDateFormat();
}
