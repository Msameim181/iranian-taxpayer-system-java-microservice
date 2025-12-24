package ir.gov.tax.tpis.sdk.providers;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CurrentDateProviderImpl implements CurrentDateProvider {

    public static final String YYYY_MM_DD_T_HH_MM_SS_Z = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String UTC = "UTC";

    @Override
    public Long toEpochMilli() {
        return Instant.now().toEpochMilli();
    }

    @Override
    public String toDateFormat() {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(YYYY_MM_DD_T_HH_MM_SS_Z, Locale.ROOT);
        return ZonedDateTime.now(ZoneOffset.UTC).format(dateTimeFormatter);
    }
}
