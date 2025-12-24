package ir.gov.tax.tpis.sdk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.gov.tax.tpis.sdk.algorithms.VerhoeffAlgorithm;
import ir.gov.tax.tpis.sdk.dto.InvoiceDto;
import ir.gov.tax.tpis.sdk.providers.TaxIdProviderImpl;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

public class SampleInvoiceCreator {

    private final TaxIdProviderImpl taxIdProvider = new TaxIdProviderImpl(new VerhoeffAlgorithm());

    public InvoiceDto getInvoiceDto() {
        long randomSerialDecimal = ThreadLocalRandom.current().nextLong(1_000_000_000L);
        Instant now = Instant.now();
        long epochMilli = now.toEpochMilli();
        String taxId = this.taxIdProvider.generateTaxId("A111H1", randomSerialDecimal, now);

        try {
            String json =
                    "{\"header\":{\"indati2m\":null,\"indatim\":%d,\"inty\":1,\"ft\":null,\"inno\":\"%s\",\"irtaxid\":null,\"scln\":null,\"setm\":2,\"tins\":\"57490845111\",\"cap\":null,\"bid\":\"10100302746\",\"insp\":null,\"tvop\":null,\"bpc\":null,\"tax17\":null,\"taxid\":\"%s\",\"inp\":1,\"scc\":null,\"ins\":1,\"billid\":null,\"tprdis\":20000,\"tdis\":500,\"tadis\":19500,\"tvam\":1755,\"todam\":0,\"tbill\":21255,\"tob\":2,\"tinb\":\"10100302746\",\"sbc\":null,\"bbc\":null,\"bpn\":null,\"crn\":null,\"cdcn\":null,\"cdcd\":null,\"tonw\":null,\"torv\":null,\"tocv\":null},\"body\":[{\"sstid\":\"2710000138624\",\"sstt\":\"سرسیلندر قطعات صنعت فولاد سازی\",\"mu\":\"164\",\"am\":2,\"fee\":10000,\"cfee\":null,\"cut\":null,\"exr\":null,\"prdis\":20000,\"dis\":500,\"adis\":19500,\"vra\":9,\"vam\":1755,\"odt\":null,\"odr\":null,\"odam\":null,\"olt\":null,\"olr\":null,\"olam\":null,\"consfee\":null,\"spro\":null,\"bros\":null,\"tcpbs\":null,\"cop\":null,\"bsrn\":null,\"vop\":null,\"tsstam\":21255,\"nw\":null,\"ssrv\":null,\"sscv\":null}],\"payments\":[],\"extension\":null}";
            String formattedJson = String.format(json,
                                                 epochMilli,
                                                 StringUtils.leftPad(Long.toHexString(randomSerialDecimal), 10, '0'),
                                                 taxId);
            return new ObjectMapper().readValue(formattedJson, InvoiceDto.class);
        } catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
