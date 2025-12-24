package ir.gov.tax.tpis.sdk.providers;

import ir.gov.tax.tpis.sdk.algorithms.ErrorDetectionAlgorithm;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;

@RequiredArgsConstructor
public class TaxIdProviderImpl implements TaxIdProvider {
    private final ErrorDetectionAlgorithm errorDetectionAlgorithm;

    @Override
    public String generateTaxId(String memoryId, long serial, Instant createDate) {

        int timeDayRange = (int) (createDate.getEpochSecond() / (3600 * 24));
        String hexTime = Long.toString(timeDayRange, 16);
        String hexSerial = Long.toString(serial, 16);

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(memoryId);
        stringBuilder.append(StringUtils.leftPad(hexTime, 5, "0"));
        stringBuilder.append(StringUtils.leftPad(hexSerial, 10, "0"));
        String controlText = toDecimal(memoryId) + StringUtils.leftPad(String.valueOf(timeDayRange), 6, "0") +
                             StringUtils.leftPad(String.valueOf(serial), 12, "0");
        stringBuilder.append(errorDetectionAlgorithm.generateCheckDigit(controlText));
        return stringBuilder.toString().toUpperCase();
    }

    private String toDecimal(String memoryId) {
        StringBuilder decimalFormat = new StringBuilder();
        for (int i = 0; i < memoryId.length(); i++) {
            if (Character.isDigit(memoryId.charAt(i))) {
                decimalFormat.append(memoryId.charAt(i));
            } else {
                decimalFormat.append((int) memoryId.charAt(i));
            }
        }
        return decimalFormat.toString();
    }
}
