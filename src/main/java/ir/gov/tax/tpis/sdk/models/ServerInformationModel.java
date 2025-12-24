package ir.gov.tax.tpis.sdk.models;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Getter
@Builder(toBuilder = true)
@Jacksonized
public class ServerInformationModel {
    private long serverTime;
    private List<KeyModel> publicKeys;
}
