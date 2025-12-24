package ir.gov.tax.tpis.sdk.providers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.gov.tax.tpis.sdk.dto.PacketHeaderDto;
import ir.gov.tax.tpis.sdk.properties.TaxProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
abstract class AbstractPacketProvider {
    private final ObjectMapper objectMapper;
    @Getter(AccessLevel.PROTECTED)
    private final TaxProperties packetProperties;

    protected PacketHeaderDto getHeader() {
        final String requestTraceId = UUID.randomUUID().toString();
        final String fiscalId = this.packetProperties.getMemoryId();
        return PacketHeaderDto.builder().requestTraceId(requestTraceId).fiscalId(fiscalId).build();
    }

    protected String serialize(final Object data) {
        try {
            if (data == null) {
                return "";
            }
            return this.objectMapper.writeValueAsString(data);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
