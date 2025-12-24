# Tax Collect Data SDK - Architecture Documentation

## Overview

The Tax Collect Data SDK (v2.0.28) is a Java SDK for integrating with the Iranian Tax System (TPIS - Tax Collection Data System). It provides secure communication with the tax authority for invoice submission, inquiry, and payment registration.

## Project Structure

```
tax-collect-data-sdk-java/
├── src/main/java/ir/gov/tax/tpis/sdk/
│   ├── algorithms/          # Error detection algorithms (Verhoeff)
│   ├── clients/             # API client interfaces and implementations
│   ├── cryptography/        # JWS signing and JWE encryption
│   ├── dto/                 # Data Transfer Objects
│   ├── enums/               # Constants and enumerations
│   ├── exceptions/          # Custom exceptions
│   ├── factories/           # Factory classes for SDK components
│   ├── initializers/        # PKCS#11 initialization
│   ├── middlewares/         # Request processing middleware
│   ├── models/              # Response models
│   ├── properties/          # Configuration properties
│   ├── providers/           # Service providers
│   └── repositories/        # Key repositories
├── src/test/                # Test files and resources
├── docs/                    # Documentation
├── pom.xml                  # Maven configuration
└── Dockerfile               # Docker configuration
```

## Core Components

### 1. API Clients

| Interface | Implementation | Purpose |
|-----------|---------------|---------|
| `TaxApi` | `TaxApiImpl` | High-level API for all tax operations |
| `TaxPublicApi` | `TaxPublicApiImpl` | Public operations (server info, keys) |
| `LowLevelTaxApi` | `LowLevelTaxApiImpl` | Low-level HTTP operations |

### 2. Cryptography Layer

- **JwsSignatory**: Signs payloads using RSA-SHA256 with X509 certificate chain
- **JweEncryptor**: Encrypts using RSA-OAEP-256 key wrap + AES-256-GCM
- **EmptySignatory/EmptyEncryptor**: No-op implementations for testing

### 3. Middleware Chain

```
Request → SignatoryMiddleware → EncryptionMiddleware → HTTP Client → Response
```

The middleware pattern allows composable request processing:
1. **SignatoryMiddleware**: Signs the payload with JWS
2. **EncryptionMiddleware**: Encrypts the signed payload with JWE

### 4. HTTP Communication

- Uses OkHttp3 with 10-minute timeout
- Automatic nonce handling for authentication
- JSON serialization via Jackson

## Security Model

### Authentication Flow
1. Client requests nonce from server
2. Client signs nonce with private key (JWS)
3. Signed nonce included in Authorization header
4. Server validates signature before processing

### Encryption Flow
1. Payload is signed with client's private key (JWS)
2. Signed payload is encrypted with server's public key (JWE)
3. Server decrypts with private key, validates signature
4. Response follows same pattern in reverse

### Key Management
- Client keys: PKCS#8 private key + X509 certificate
- Server keys: Fetched via `getServerInformation()`, cached 1 hour
- PKCS#11 support: Hardware token integration available

## Tax ID Generation

Tax IDs are generated using:
```
[MemoryId] + [TimeHex(5)] + [SerialHex(10)] + [VerhoeffChecksum(1)]
```

- **MemoryId**: Fiscal identifier (e.g., "A111H1")
- **TimeHex**: Days since epoch in hexadecimal
- **SerialHex**: Sequential serial number in hex
- **VerhoeffChecksum**: Error detection digit

## Configuration

### TaxProperties
- `memoryId`: Fiscal ID / client identifier

### UrlProperties
- `baseUrl`: API base URL

### HttpHeadersProperties
- Custom HTTP headers
- Authorization header configuration

## Error Handling

| Exception | Description |
|-----------|-------------|
| `TaxCollectionApiException` | API returned an error response |
| `UnknownResponseException` | Unexpected response format |
| `PacketTypeNotFoundException` | Invalid packet type |
| `Pkcs11SignatoryException` | PKCS#11 related errors |
