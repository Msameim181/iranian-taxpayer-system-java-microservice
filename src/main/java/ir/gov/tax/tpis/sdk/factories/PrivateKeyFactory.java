package ir.gov.tax.tpis.sdk.factories;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class PrivateKeyFactory {
    private final KeyFactory keyFactory;

    public PrivateKeyFactory() throws NoSuchAlgorithmException {
        this.keyFactory = KeyFactory.getInstance("RSA");
    }

    public PrivateKey fromPkcs8(final InputStream stream) throws IOException, InvalidKeySpecException {
        try (final InputStreamReader reader = new InputStreamReader(stream)) {
            final PEMParser pemParser = new PEMParser(reader);
            final PrivateKeyInfo pemKeyPair = (PrivateKeyInfo) pemParser.readObject();
            final byte[] encoded = pemKeyPair.getEncoded();
            return this.keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encoded));
        }
    }

    public PrivateKey fromPkcs8(final String path) throws IOException, InvalidKeySpecException {
        try (final InputStream stream = Files.newInputStream(Paths.get(path))) {
            return this.fromPkcs8(stream);
        }
    }
}
