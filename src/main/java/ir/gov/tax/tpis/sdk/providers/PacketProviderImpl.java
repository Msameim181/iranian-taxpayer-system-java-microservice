package ir.gov.tax.tpis.sdk.providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.gov.tax.tpis.sdk.cryptography.Encryptor;
import ir.gov.tax.tpis.sdk.cryptography.Signatory;
import ir.gov.tax.tpis.sdk.dto.*;
import ir.gov.tax.tpis.sdk.middlewares.EmptyMiddleware;
import ir.gov.tax.tpis.sdk.middlewares.EncryptionMiddleware;
import ir.gov.tax.tpis.sdk.middlewares.Middleware;
import ir.gov.tax.tpis.sdk.middlewares.SignatoryMiddleware;
import ir.gov.tax.tpis.sdk.properties.TaxProperties;

public class PacketProviderImpl extends AbstractPacketProvider implements PacketProvider {

    private final EncryptionMiddleware encryptionMiddleware;
    private final SignatoryMiddleware signatoryMiddleware;
    private final EmptyMiddleware emptyMiddleware;

    public PacketProviderImpl(final ObjectMapper objectMapper,
                              final TaxProperties packetProperties,
                              final Signatory signatory,
                              final Encryptor encryptor) {
        super(objectMapper, packetProperties);
        this.encryptionMiddleware = new EncryptionMiddleware(encryptor);
        this.signatoryMiddleware = new SignatoryMiddleware(signatory);
        this.emptyMiddleware = new EmptyMiddleware();
    }


    @Override
    public PacketDto createInvoicePacket(final InvoiceDto invoice) {
        return createPrivatePacket(invoice);
    }

    private PacketDto createPrivatePacket(final Object dto) {
        return getPacketDto(dto, getCryptographyMiddlewares());
    }

    private Middleware getEmptyMiddleware() {
        return Middleware.link(this.emptyMiddleware);
    }

    private Middleware getCryptographyMiddlewares() {
        return Middleware.link(this.signatoryMiddleware, this.encryptionMiddleware);
    }

    private PacketDto getPacketDto(final Object data, final Middleware middleware) {
        return PacketDto.builder().payload(middleware.handle(serialize(data))).header(getHeader()).build();
    }
}
