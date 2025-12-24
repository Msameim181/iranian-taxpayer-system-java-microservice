package ir.gov.tax.tpis.sdk.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Getter
@Builder(toBuilder = true)
@Jacksonized
public class InvoiceDto {
	private final HeaderDto header;
	private final List<BodyItemDto> body;
	private final List<PaymentItemDto> payments;
	private final List<ExtensionItemDto> extension;
}
