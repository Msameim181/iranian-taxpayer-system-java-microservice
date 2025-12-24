package ir.gov.tax.tpis.sdk.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Getter
@Builder(toBuilder = true)
@Jacksonized
public class BodyItemDto {
    private String sstid;
    private String sstt;
    private String mu;
    private BigDecimal am;
    private BigDecimal fee;
    private BigDecimal cfee;
    private String cut;
    private Long exr;
    private Long prdis;
    private Long dis;
    private Long adis;
    private BigDecimal vra;
    private Long vam;
    private String odt;
    private BigDecimal odr;
    private Long odam;
    private String olt;
    private BigDecimal olr;
    private Long olam;
    private Long consfee;
    private Long spro;
    private Long bros;
    private Long tcpbs;
    private Long cop;
    private String bsrn;
    private Long vop;
    private Long tsstam;
    private BigDecimal nw;
    private Long ssrv;
    private BigDecimal sscv;
    private BigDecimal cui;
    private BigDecimal cpr;
    private Long sovat;
    private String hs;
    private BigDecimal vba;
}
