package ir.gov.tax.tpis.sdk.dto;

import ir.gov.tax.tpis.sdk.enums.RequestStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.time.ZonedDateTime;

@Getter
@Builder(toBuilder = true)
@Jacksonized
public class InquiryDto {
    private final ZonedDateTime start;
    private final ZonedDateTime end;
    private final Pageable pageable;
    private final RequestStatus status;
}
