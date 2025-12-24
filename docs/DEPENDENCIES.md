# Tax Collect Data SDK - Dependencies

## Runtime Dependencies

### Core Dependencies

| Dependency | Version | Purpose | License |
|------------|---------|---------|---------|
| **Jackson Databind** | 2.14.1 | JSON serialization/deserialization | Apache 2.0 |
| **OkHttp3** | 4.9.3 | HTTP client for API calls | Apache 2.0 |
| **JOSE4J** | 0.9.3 | JWS/JWE signing and encryption | Apache 2.0 |
| **BouncyCastle** | 1.76 | Cryptographic operations (PKCS#8/11, X509) | MIT |
| **Logback Classic** | 1.2.6 | Logging implementation | EPL/LGPL |
| **Apache Commons Lang3** | 3.12.0 | Utility functions | Apache 2.0 |
| **Lombok** | 1.18.32 | Compile-time code generation | MIT |

### Dependency Details

#### Jackson Databind (2.14.1)
```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.14.1</version>
</dependency>
```
**Used for:**
- Serializing DTOs to JSON for API requests
- Deserializing JSON responses to model objects
- Custom ObjectMapper configuration

#### OkHttp3 (4.9.3)
```xml
<dependency>
    <groupId>com.squareup.okhttp3</groupId>
    <artifactId>okhttp</artifactId>
    <version>4.9.3</version>
</dependency>
```
**Used for:**
- HTTP/HTTPS communication with tax server
- Connection pooling and timeout management
- Request/response handling

#### JOSE4J (0.9.3)
```xml
<dependency>
    <groupId>org.bitbucket.b_c</groupId>
    <artifactId>jose4j</artifactId>
    <version>0.9.3</version>
</dependency>
```
**Used for:**
- JWS (JSON Web Signature) - RSA-SHA256 signing
- JWE (JSON Web Encryption) - RSA-OAEP-256 + AES-256-GCM
- Token/payload security

#### BouncyCastle (1.76)
```xml
<dependency>
    <groupId>org.bouncycastle</groupId>
    <artifactId>bcpkix-jdk18on</artifactId>
    <version>1.76</version>
</dependency>
```
**Used for:**
- PKCS#8 private key parsing
- PKCS#11 hardware token support
- X509 certificate handling
- Cryptographic provider

#### Logback Classic (1.2.6)
```xml
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.2.6</version>
</dependency>
```
**Used for:**
- Application logging
- Debug and error tracking
- SLF4J implementation

#### Apache Commons Lang3 (3.12.0)
```xml
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
    <version>3.12.0</version>
</dependency>
```
**Used for:**
- String utilities
- Object utilities
- General helper functions

#### Lombok (1.18.32)
```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.32</version>
</dependency>
```
**Used for:**
- `@Getter`, `@Setter` annotations
- `@Builder` pattern generation
- `@NoArgsConstructor`, `@AllArgsConstructor`
- Compile-time only (not runtime)

---

## Test Dependencies

| Dependency | Version | Purpose |
|------------|---------|---------|
| **JUnit 5 (Jupiter)** | 5.10.3 | Modern testing framework |
| **JUnit 3** | 3.8.1 | Legacy testing support |

---

## System Requirements

### Java Runtime
- **Minimum Version**: Java 9
- **Recommended**: Java 11 or higher
- **LTS Versions Supported**: Java 11, 17, 21

### Build Requirements
- **Maven**: 3.6.0+
- **Memory**: Minimum 512MB heap for compilation

---

## Transitive Dependencies

The following are automatically included via the main dependencies:

### From Jackson
- jackson-core
- jackson-annotations

### From OkHttp3
- okio
- kotlin-stdlib (runtime)

### From BouncyCastle
- bcprov-jdk18on
- bcutil-jdk18on

### From Logback
- slf4j-api
- logback-core

---

## Docker Runtime Dependencies

When running as a Docker microservice, ensure:

1. **JRE/JDK Image**: Use `eclipse-temurin:11-jre` or `eclipse-temurin:17-jre`
2. **Timezone Data**: Required for date/time operations
3. **CA Certificates**: For HTTPS communication with tax server
4. **Cryptographic Providers**: BouncyCastle included in JAR

---

## Security Considerations

### Cryptographic Requirements
- RSA key size: Minimum 2048 bits (recommended 4096)
- Certificate validity: Check expiration dates
- JCE Unlimited Strength: Required for strong encryption (default in Java 9+)

### Network Requirements
- HTTPS (TLS 1.2+) for tax server communication
- Outbound access to tax server endpoints
- DNS resolution for tax server hostname

---

## Version Compatibility Matrix

| SDK Version | Java | Jackson | OkHttp | BouncyCastle |
|-------------|------|---------|--------|--------------|
| 2.0.28 | 9+ | 2.14.1 | 4.9.3 | 1.76 |
| 2.0.x | 9+ | 2.14.x | 4.9.x | 1.7x |

---

## Updating Dependencies

To check for dependency updates:
```bash
mvn versions:display-dependency-updates
```

To update to latest compatible versions:
```bash
mvn versions:use-latest-releases
```

**Note**: Test thoroughly after any dependency update, especially for cryptographic libraries.
