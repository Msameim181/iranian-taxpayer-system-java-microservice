package ir.gov.tax.tpis.sdk.exceptions;

import ir.gov.tax.tpis.sdk.dto.ErrorResponseDto;
import lombok.Getter;

@Getter
public class TaxCollectionApiException extends RuntimeException {
    private final ErrorResponseDto errorResponse;

    public TaxCollectionApiException(final ErrorResponseDto errorResponse) {
        super(errorResponse.toString());
        this.errorResponse = errorResponse;
    }
}
