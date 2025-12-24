package ir.gov.tax.tpis.sdk.dto;

import ir.gov.tax.tpis.sdk.models.ErrorModel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Getter
@ToString
@Jacksonized
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class ErrorResponseDto {
    private final long timestamp;
    private final String requestTraceId;
    private final List<ErrorModel> errors;
}
