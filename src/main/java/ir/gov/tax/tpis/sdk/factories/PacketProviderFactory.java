package ir.gov.tax.tpis.sdk.factories;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.gov.tax.tpis.sdk.cryptography.Encryptor;
import ir.gov.tax.tpis.sdk.cryptography.Signatory;
import ir.gov.tax.tpis.sdk.properties.TaxProperties;
import ir.gov.tax.tpis.sdk.providers.PacketProvider;
import ir.gov.tax.tpis.sdk.providers.PacketProviderImpl;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PacketProviderFactory {
    private final ObjectMapper objectMapper;
    private final TaxProperties packetProperties;

    public PacketProvider createPacketProvider(final Signatory signatory, final Encryptor encryptor) {
        return new PacketProviderImpl(this.objectMapper, this.packetProperties, signatory, encryptor);
    }
}
