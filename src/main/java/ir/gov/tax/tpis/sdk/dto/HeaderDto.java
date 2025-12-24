package ir.gov.tax.tpis.sdk.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder(toBuilder = true)
@Jacksonized
public class HeaderDto {
    private Long indati2m;
    private Long indatim;
    private Integer inty;
    private Integer ft;
    private String inno;
    private String irtaxid;
    private String scln;
    private Integer setm;
    private String tins;
    private Long cap;
    private String bid;
    private Long insp;
    private Long tvop;
    private String bpc;
    private Long tax17;
    private String taxid;
    private Integer inp;
    private String scc;
    private Integer ins;
    private String billid;
    private Long tprdis;
    private Long tdis;
    private Long tadis;
    private Long tvam;
    private Long todam;
    private Long tbill;
    private Integer tob;
    private String tinb;
    private String sbc;
    private String bbc;
    private String bpn;
    private String crn;
    private String cdcn;
    private Integer cdcd;
    private BigDecimal tonw;
    private Long torv;
    private BigDecimal tocv;
    private String tinc;
    private String lno;
    private String lrno;
    private String ocu;
    private String oci;
    private String dco;
    private String dci;
    private String tid;
    private String rid;
    private Byte lt;
    private String cno;
    private String did;
    private List<ShippingGoodDto> sg;
    private String asn;
    private Integer asd;
    private String in;
    private String an;
}
