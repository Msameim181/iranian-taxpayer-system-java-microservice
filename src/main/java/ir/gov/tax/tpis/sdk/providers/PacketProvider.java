package ir.gov.tax.tpis.sdk.providers;

import ir.gov.tax.tpis.sdk.dto.*;

public interface PacketProvider {

    PacketDto createInvoicePacket(InvoiceDto invoice);
}
