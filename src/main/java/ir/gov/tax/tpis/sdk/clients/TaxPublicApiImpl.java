package ir.gov.tax.tpis.sdk.clients;

import com.fasterxml.jackson.core.type.TypeReference;
import ir.gov.tax.tpis.sdk.enums.PacketType;
import ir.gov.tax.tpis.sdk.models.ServerInformationModel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TaxPublicApiImpl implements TaxPublicApi {
    private final LowLevelTaxApi sender;

    @Override
    public ServerInformationModel getServerInformation() {
        return this.sender.getServerInformation();
    }
}
