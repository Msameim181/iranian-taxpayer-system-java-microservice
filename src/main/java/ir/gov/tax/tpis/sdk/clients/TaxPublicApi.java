package ir.gov.tax.tpis.sdk.clients;

import ir.gov.tax.tpis.sdk.models.ServerInformationModel;


public interface TaxPublicApi {
    ServerInformationModel getServerInformation();
}
