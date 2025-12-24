package ir.gov.tax.tpis.sdk.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder(toBuilder = true)
@Jacksonized
public class PaymentItemDto {
    private String iinn;
    private String acn;
    private String trmn;
    private String trn;
    private String pcn;
    private String pid;
    private Long pdt;
    private Integer pmt;
    private Long pv;
    private String vatpid;
    private String vatbid;
}
