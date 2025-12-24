package ir.gov.tax.tpis.sdk.microservice.controller;

import ir.gov.tax.tpis.sdk.exceptions.TaxCollectionApiException;
import ir.gov.tax.tpis.sdk.exceptions.UnknownResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(TaxCollectionApiException.class)
    public ResponseEntity<Map<String, Object>> handleTaxApiException(TaxCollectionApiException e) {
        logger.error("Tax API error: {}", e.getMessage(), e);
        Map<String, Object> response = new HashMap<>();
        response.put("error", "TAX_API_ERROR");
        response.put("message", e.getMessage());
        response.put("details", e.getErrorResponse());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
    }

    @ExceptionHandler(UnknownResponseException.class)
    public ResponseEntity<Map<String, Object>> handleUnknownResponse(UnknownResponseException e) {
        logger.error("Unknown response from tax server: {}", e.getMessage(), e);
        Map<String, Object> response = new HashMap<>();
        response.put("error", "UNKNOWN_RESPONSE");
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception e) {
        logger.error("Unexpected error: {}", e.getMessage(), e);
        Map<String, Object> response = new HashMap<>();
        response.put("error", "INTERNAL_ERROR");
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
