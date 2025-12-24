package ir.gov.tax.tpis.sdk.algorithms;

public interface ErrorDetectionAlgorithm {
    String generateCheckDigit(String num);

    boolean validateCheckDigit(String num);
}
