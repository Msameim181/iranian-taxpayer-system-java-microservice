# Iranian Tax System SDK for Java 🇮🇷 (Dockerize and Ready to use)

[![Java](https://img.shields.io/badge/Java-11+-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-green.svg)](https://spring.io/projects/spring-boot)
[![Version](https://img.shields.io/badge/version-2.0.28-brightgreen.svg)](https://github.com/Msameim181/iranian-taxpayer-system-java-microservice)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/Msameim181/iranian-taxpayer-system-java-microservice/blob/main/LICENSE)

A comprehensive Java SDK for seamless integration with the Iranian Tax Collection Data System (TPIS). This SDK provides both library and microservice deployment options with enterprise-grade security, supporting JWS/JWE cryptography for secure communication with the Iranian Tax Authority.

References: 
1. [بسته توسعه نرم افزار(SDK) به زبان جاوا(نسخه باگواهی امضاء) شهریورماه 1404](https://www.intamedia.ir/%D8%A8%D8%B3%D8%AA%D9%87-%D8%AA%D9%88%D8%B3%D8%B9%D9%87-%D9%86%D8%B1%D9%85-%D8%A7%D9%81%D8%B2%D8%A7%D8%B1sdk-%D8%A8%D9%87-%D8%B2%D8%A8%D8%A7%D9%86-%D8%AC%D8%A7%D9%88%D8%A7%D9%86%D8%B3%D8%AE%D9%87-%D9%82%D8%AF%DB%8C%D9%85-%D8%A8%D8%A7%DA%AF%D9%88%D8%A7%D9%87%DB%8C-%D8%A7%D9%85%D8%B6%D8%A7-%D8%A8%D9%87%D9%85%D9%86-%D9%85%D8%A7%D9%871403)
2. [پایانه های فروشگاهی و سامانه مودیان](https://www.intamedia.ir/Law-of-store-terminals-and-taxpayer-system#226989--------)
3. [سازمان امور مالیاتی کشور](http://www.intamedia.ir/)

## 📋 Table of Contents

- [Features](#-features)
- [Architecture](#-architecture)
- [Quick Start](#-quick-start)
- [Installation](#-installation)
- [Configuration](#-configuration)
- [Usage Examples](#-usage-examples)
- [Microservice Deployment](#-microservice-deployment)
- [Docker](#-docker)
- [API Reference](#-api-reference)
- [Security](#-security)
- [Documentation](#-documentation)
- [Contributing](#-contributing)
- [License](#-license)
- [Support](#-support)

## ✨ Features

### Core Capabilities
- **📝 Invoice Management** - Submit, query, and manage invoices
- **💰 Payment Registration** - Register and track payment transactions
- **🔍 Taxpayer Information** - Retrieve taxpayer details by economic code
- **📊 Status Inquiry** - Real-time invoice status tracking
- **🔐 Enterprise Security** - JWS signing + JWE encryption
- **🔄 Retry Logic** - Built-in resilience and error handling

### Deployment Options
- **📦 Standalone Library** - Embed in your Java application
- **🚀 Microservice** - Deploy as independent REST API service
- **🐳 Docker Support** - Containerized deployment with Docker/Kubernetes
- **☁️ Cloud-Ready** - Horizontal scaling, health checks, metrics

### Security & Compliance
- **JWS (JSON Web Signature)** - RSA-SHA256 request signing
- **JWE (JSON Web Encryption)** - RSA-OAEP-256 + AES-256-GCM
- **PKCS#8/PKCS#11 Support** - Software and hardware token integration
- **X.509 Certificates** - Standard certificate-based authentication
- **Nonce Authentication** - Replay attack prevention

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Your Application                         │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌──────────────┐         ┌────────────────┐               │
│  │   TaxApi     │────────>│ Signatory      │               │
│  │              │         │ (JWS Signing)  │               │
│  └──────┬───────┘         └────────────────┘               │
│         │                                                   │
│         │                 ┌────────────────┐               │
│         └────────────────>│  Encryptor     │               │
│                           │ (JWE Encrypt)  │               │
│                           └────────┬───────┘               │
│                                    │                       │
│                           ┌────────▼───────┐               │
│                           │  HTTP Client   │               │
│                           │    (OkHttp3)   │               │
│                           └────────┬───────┘               │
└────────────────────────────────────┼───────────────────────┘
                                     │
                                     │ HTTPS
                                     │
                          ┌──────────▼──────────┐
                          │   Iranian Tax       │
                          │  Authority Server   │
                          │  (tp.tax.gov.ir)    │
                          └─────────────────────┘
```

## 🚀 Quick Start

### Prerequisites

- **Java**: 11 or higher
- **Maven**: 3.6.0 or higher
- **Credentials**: Private key and certificate from Iranian Tax Authority
- **Memory ID**: Your fiscal identifier

### 5-Minute Setup

```bash
# 1. Clone the repository
git clone https://github.com/Msameim181/iranian-taxpayer-system-java-microservice.git
cd iranian-taxpayer-system-java-microservice

# 2. Setup your keys
mkdir -p keys
cp /path/to/your/private.pem keys/
cp /path/to/your/certificate.cer keys/

# 3. Build the project
mvn clean install

# 4. Run as microservice (optional)
mvn spring-boot:run
```

## 📦 Installation

### Maven Dependency

```xml
<dependency>
    <groupId>ir.gov.tax.tpis.sdk</groupId>
    <artifactId>tax-collect-data-sdk</artifactId>
    <version>2.0.28</version>
</dependency>
```

### Build from Source

```bash
# Build JAR with dependencies
mvn clean compile assembly:single

# Build Spring Boot executable
mvn clean package

# Skip tests (if needed)
mvn clean package -DskipTests
```

## ⚙️ Configuration

### Application Properties (application.yml)

```yaml
tax:
  api:
    base-url: https://tp.tax.gov.ir/requestsmanager
    memory-id: YOUR_MEMORY_ID
    private-key-path: /path/to/private.pem
    certificate-path: /path/to/certificate.cer
```

### Environment Variables

```bash
export TAX_API_BASE_URL="https://tp.tax.gov.ir/requestsmanager"
export TAX_MEMORY_ID="YOUR_MEMORY_ID"
export TAX_PRIVATE_KEY_PATH="/path/to/private.pem"
export TAX_CERTIFICATE_PATH="/path/to/certificate.cer"
```

## 💻 Usage Examples

### Initialize the SDK

```java
import ir.gov.tax.tpis.sdk.clients.TaxApi;
import ir.gov.tax.tpis.sdk.clients.TaxApiImpl;
import ir.gov.tax.tpis.sdk.cryptography.*;
import ir.gov.tax.tpis.sdk.factories.*;

// 1. Create signatory from your credentials
Signatory signatory = new Pkcs8SignatoryFactory(new CurrentDateProviderImpl())
    .create("keys/private.pem", "keys/certificate.cer");

// 2. Initialize API factory
TaxApiFactory factory = new TaxApiFactory(
    "https://tp.tax.gov.ir/requestsmanager",
    new TaxProperties("YOUR_MEMORY_ID")
);

// 3. Create public API and fetch server keys
TaxPublicApi publicApi = factory.createPublicApi(signatory);
ServerInformationModel serverInfo = publicApi.getServerInformation();

// 4. Create encryptor
Encryptor encryptor = new EncryptorFactory().create(publicApi);

// 5. Create authenticated API
TaxApi taxApi = factory.createApi(signatory, encryptor);
```

### Submit an Invoice

```java
// Create invoice
InvoiceDto invoice = InvoiceDto.builder()
    .header(HeaderDto.builder()
        .taxId(generateTaxId())
        .indatim(System.currentTimeMillis())
        .indati2m(System.currentTimeMillis())
        .inty(1)  // Invoice type
        .inp(1)   // Invoice pattern
        .ins(1)   // Invoice subject
        .tins("SELLER_TIN")
        .bid("BUYER_ID")
        .setm(1)  // Settlement method
        .cap(100000L)
        .insp(90000L)
        .tvam(9000L)
        .build())
    .body(List.of(
        BodyItemDto.builder()
            .sstid("PRODUCT_ID")
            .sstt("Product Name")
            .am(1.0)
            .fee(100000L)
            .vra(9.0)  // VAT 9%
            .vam(8100L)
            .build()
    ))
    .build();

// Submit invoice
List<InvoiceResponseModel> responses = taxApi.sendInvoices(List.of(invoice));

// Process response
for (InvoiceResponseModel response : responses) {
    System.out.println("UID: " + response.getUid());
    System.out.println("Reference: " + response.getReferenceNumber());
    if (response.getErrorCode() != null) {
        System.err.println("Error: " + response.getErrorDetail());
    }
}
```

### Query Invoices

```java
// Query by time range
List<InquiryResultModel> results = taxApi.inquiryByTime(
    InquiryDto.builder()
        .start(ZonedDateTime.now().minusDays(7))
        .end(ZonedDateTime.now())
        .status(RequestStatus.SUCCESS)
        .build()
);

// Query by UID
List<InquiryResultModel> results = taxApi.inquiryByUid(
    InquiryByUidDto.builder()
        .uidList(List.of("uid1", "uid2"))
        .fiscalId("YOUR_FISCAL_ID")
        .build()
);
```

### Register Payment

```java
RegisterPaymentResultModel result = taxApi.registerPaymentRequest(
    RegisterPaymentRequestDto.builder()
        .taxId("A111H104EA6001D0B32AC6")
        .paymentDate(System.currentTimeMillis())
        .paidAmount(100000L)
        .paymentMethod(PaymentMethod.CARD)
        .build()
);
```

### Get Taxpayer Information

```java
TaxpayerModel taxpayer = taxApi.getTaxpayer("1234567890");
System.out.println("Name: " + taxpayer.getName());
System.out.println("Status: " + taxpayer.getStatus());
```

## 🌐 Microservice Deployment

### REST API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/v1/invoices` | Submit invoices |
| `GET` | `/api/v1/invoices/inquiry` | Query invoices by time |
| `GET` | `/api/v1/invoices/{taxId}` | Get invoice status |
| `POST` | `/api/v1/payments` | Register payment |
| `GET` | `/api/v1/taxpayers/{code}` | Get taxpayer info |
| `GET` | `/health` | Health check |
| `GET` | `/metrics` | Prometheus metrics |

### Run as Microservice

```bash
# Using Maven
mvn spring-boot:run

# Using Java
java -jar target/tax-collect-data-sdk-2.0.28.jar

# Access API
curl http://localhost:8080/health
```

## 🐳 Docker

### Quick Start with Docker

```bash
# Build image
docker build -t tax-microservice:latest .

# Run container
docker run -d \
  --name tax-microservice \
  -p 8080:8080 \
  -v $(pwd)/keys:/app/keys:ro \
  -e TAX_MEMORY_ID=YOUR_MEMORY_ID \
  -e TAX_API_BASE_URL=https://tp.tax.gov.ir/requestsmanager \
  tax-microservice:latest
```

### Docker Compose

```bash
# Simple deployment
docker-compose up -d

# Full stack with monitoring
docker-compose -f docker-compose.full.yml up -d

# View logs
docker-compose logs -f
```

### Kubernetes

```bash
# Apply deployment
kubectl apply -f k8s/

# Check status
kubectl get pods -l app=tax-microservice

# View logs
kubectl logs -f deployment/tax-microservice
```

## 📚 API Reference

### Core Classes

| Class | Description |
|-------|-------------|
| `TaxApi` | Main API interface for all operations |
| `TaxApiFactory` | Factory for creating API instances |
| `Signatory` | Interface for JWS signing |
| `Encryptor` | Interface for JWE encryption |
| `InvoiceDto` | Invoice data transfer object |
| `HeaderDto` | Invoice header information |
| `BodyItemDto` | Invoice line items |

### Exception Handling

```java
try {
    List<InvoiceResponseModel> responses = taxApi.sendInvoices(invoices);
} catch (TaxCollectionApiException e) {
    // API returned error response
    ErrorResponseDto error = e.getErrorResponse();
    log.error("API Error: {} - {}", error.getCode(), error.getMessage());
} catch (UnknownResponseException e) {
    // Unexpected response format
    log.error("Unknown response: {}", e.getMessage());
} catch (Pkcs11SignatoryException e) {
    // PKCS#11 hardware token error
    log.error("Token error: {}", e.getMessage());
}
```

## 🔐 Security

### Cryptographic Standards

- **Signing**: JWS with RS256 (RSA-SHA256)
- **Encryption**: JWE with RSA-OAEP-256 + A256GCM
- **Key Size**: RSA 2048-bit minimum (4096-bit recommended)
- **Certificate**: X.509 v3 from Iranian Tax Authority

### Key Management Best Practices

1. **Secure Storage**: Store keys in secure vaults (HashiCorp Vault, AWS KMS)
2. **Access Control**: Limit key access with file permissions (chmod 600)
3. **Rotation**: Rotate keys according to security policy
4. **Backup**: Maintain secure offline backups
5. **Monitoring**: Track key usage and expiration

### Security Checklist

- [ ] Keys stored securely (not in version control)
- [ ] Keys have proper file permissions (600)
- [ ] Certificate expiration monitored
- [ ] TLS 1.2+ for all connections
- [ ] Network access restricted
- [ ] Audit logging enabled
- [ ] Regular security updates applied

## 📖 Documentation

Comprehensive documentation available in the [`docs/`](docs/) directory:

- [**Architecture**](docs/ARCHITECTURE.md) - System design and components
- [**Dependencies**](docs/DEPENDENCIES.md) - All project dependencies
- [**API Flows**](docs/FLOWS.md) - Detailed API operation flows
- [**Microservice Guide**](docs/MICROSERVICE.md) - Deployment as microservice
- [**Docker Guide**](docs/DOCKER.md) - Container deployment

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=TaxApiTest

# Run with coverage
mvn clean test jacoco:report

# Integration tests
mvn verify
```

## 🤝 Contributing

Contributions are welcome! Please follow these guidelines:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. **Open** a Pull Request

### Development Setup

```bash
# Clone your fork
git clone https://github.com/YOUR_USERNAME/iranian-taxpayer-system-java-microservice.git

# Install dependencies
mvn clean install

# Run tests
mvn test

# Format code (if formatter configured)
mvn formatter:format
```

## 📄 License

This project is licensed under the **Apache License 2.0** - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

### Getting Help

- **Issues**: [GitHub Issues](https://github.com/Msameim181/iranian-taxpayer-system-java-microservice/issues)
- **Discussions**: [GitHub Discussions](https://github.com/Msameim181/iranian-taxpayer-system-java-microservice/discussions)
- **Documentation**: [docs/](docs/) directory

### Common Issues

| Issue | Solution |
|-------|----------|
| Connection timeout | Check firewall and network access to tax server |
| Authentication failure | Verify keys and certificate validity |
| Encryption error | Refresh server public keys |
| Invalid invoice | Validate all required fields |

### Reporting Issues

When reporting issues, please include:

1. SDK version (`2.0.28`)
2. Java version (`java -version`)
3. Operating system
4. Error message or stack trace
5. Minimal reproducible example

## 🙏 Acknowledgments

- Iranian Tax Organization for providing the Tax Collection Data System
- All contributors who have helped improve this SDK
- Open source libraries: Spring Boot, OkHttp, Jackson, BouncyCastle, JOSE4J

## 📊 Project Statistics

- **Version**: 2.0.28
- **Language**: Java 11+
- **Framework**: Spring Boot 2.7.18
- **Build Tool**: Maven 3.6+
- **License**: Apache 2.0

---

<p align="center">
  Made with ❤️ for the Iranian developer community
</p>

<p align="center">
  <sub>Built with Java • Secured with JWS/JWE • Powered by Spring Boot</sub>
</p>
