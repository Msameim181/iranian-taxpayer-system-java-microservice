package ir.gov.tax.tpis.sdk.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Builder(toBuilder = true)
@Jacksonized
public class InquiryByReferenceNumberDto {
    private final ZonedDateTime start;
    private final ZonedDateTime end;
    private final List<String> referenceNumbers;
}
