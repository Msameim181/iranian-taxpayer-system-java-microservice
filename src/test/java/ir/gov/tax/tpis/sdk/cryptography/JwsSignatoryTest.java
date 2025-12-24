package ir.gov.tax.tpis.sdk.cryptography;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.gov.tax.tpis.sdk.factories.Pkcs11SignatoryFactory;
import ir.gov.tax.tpis.sdk.factories.Pkcs8SignatoryFactory;
import ir.gov.tax.tpis.sdk.models.TokenModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;


public class JwsSignatoryTest {
    public static final String alia = "ali ravanan [Sign]'s General Governmental Intermediate Silver CA-G3 ID";
    public static final String pin = "1234";
    public static final String library = "G:\\Repos\\TaxPayer\\tax-collect-data-sdk-dotnet\\TaxCollectData.Sample\\ShuttleCsp11_3003.dll";
    private static final String certificatePath = "src/test/resources/cryptography/cert1.crt";
    private static final String privateKeyPath = "src/test/resources/cryptography/privatekey1.pem";
    private static final String jwsPath = "src/test/resources/cryptography/jws";
    private final Pkcs8SignatoryFactory pkcs8SignatoryFactory;
    private final Pkcs11SignatoryFactory pkcs11SignatoryFactory;

    public JwsSignatoryTest() throws CertificateException, NoSuchAlgorithmException {
        this.pkcs8SignatoryFactory = new Pkcs8SignatoryFactory();
        this.pkcs11SignatoryFactory = new Pkcs11SignatoryFactory();

    }

    @Test
    void Test() throws CertificateException, IOException, InvalidKeySpecException {
        final Signatory signatory = this.pkcs8SignatoryFactory.create(privateKeyPath, certificatePath);
        final TokenModel tokenModel = TokenModel.builder().nonce("d55ba2e8-f253-48e7-8d14-b446815ac46b").clientId("A11226").build();
        final String sign = signatory.sign(new ObjectMapper().writeValueAsString(tokenModel));
        final String jws = new String(Files.readAllBytes(Paths.get(jwsPath)));
        Assertions.assertEquals(sign, jws);
    }

//    @Test
//    public void testToken() throws IOException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {
//        final Signatory signatory = this.pkcs11SignatoryFactory.create(alia, pin, library);
//        final TokenModel tokenModel = TokenModel.builder().nonce("d55ba2e8-f253-48e7-8d14-b446815ac46b").clientId("A11226").build();
//        final String sign = signatory.sign(new ObjectMapper().writeValueAsString(tokenModel));
//        final String jws = new String(Files.readAllBytes(Paths.get(jwsPath)));
//        Assertions.assertEquals(sign, jws);
//    }
}