package ir.gov.tax.tpis.sdk.enums;

public class PacketType {
    public static final String GET_SERVER_INFORMATION = "server-information";
    public static final String INQUIRY = "inquiry";
    public static final String INQUIRY_BY_UID = "inquiry-by-uid";
    public static final String INQUIRY_BY_REFERENCE_ID = "inquiry-by-reference-id";
    public static final String GET_FISCAL_INFORMATION = "fiscal-information";
    public static final String GET_TAXPAYER = "taxpayer";
    public static final String GET_TAXPAYER_INFORMATION = "taxpayer-info";
    public static final String GET_ARTICLE6_STATUS = "taxpayer-article6-status";
    public static final String INVOICE = "invoice";
    public static final String NONCE = "nonce";
    public static final String INQUIRY_INVOICE_STATUS = "inquiry-invoice-status";
    public static final String INVOICE_PAYMENT = "invoice-payment";

    private PacketType() {
        throw new IllegalStateException("Enum class");
    }
}
