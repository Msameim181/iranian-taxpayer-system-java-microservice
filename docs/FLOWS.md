# Tax Collect Data SDK - API Flows

## Table of Contents
1. [Initialization Flow](#1-initialization-flow)
2. [Invoice Submission Flow](#2-invoice-submission-flow)
3. [Invoice Inquiry Flows](#3-invoice-inquiry-flows)
4. [Payment Registration Flow](#4-payment-registration-flow)
5. [Taxpayer Information Flow](#5-taxpayer-information-flow)
6. [Fiscal Information Flow](#6-fiscal-information-flow)

---

## 1. Initialization Flow

### Purpose
Initialize the SDK with proper authentication credentials and encryption keys.

### Sequence Diagram
```
┌─────────┐     ┌─────────────┐     ┌─────────────┐     ┌──────────┐
│  Client │     │ TaxApiFactory│     │ TaxPublicApi│     │ Tax Server│
└────┬────┘     └──────┬──────┘     └──────┬──────┘     └────┬─────┘
     │                 │                   │                  │
     │ create(keys)    │                   │                  │
     │────────────────>│                   │                  │
     │                 │                   │                  │
     │ createPublicApi()                   │                  │
     │────────────────>│                   │                  │
     │                 │                   │                  │
     │<────────────────│ TaxPublicApi      │                  │
     │                 │                   │                  │
     │ getServerInformation()              │                  │
     │────────────────────────────────────>│                  │
     │                 │                   │ GET /server/info │
     │                 │                   │─────────────────>│
     │                 │                   │<─────────────────│
     │<────────────────────────────────────│ ServerInfoModel  │
     │                 │                   │                  │
     │ createApi(signatory, encryptor)     │                  │
     │────────────────>│                   │                  │
     │<────────────────│ TaxApi            │                  │
```

### Code Example
```java
// 1. Create signatory from certificate and private key
Signatory signatory = new Pkcs8SignatoryFactory(new CurrentDateProviderImpl())
    .create(privateKeyPath, certificatePath);

// 2. Create API factory
TaxApiFactory factory = new TaxApiFactory(
    "https://tp.tax.gov.ir/requestsmanager",
    new TaxProperties("YOUR_MEMORY_ID")
);

// 3. Create public API and get encryption keys
TaxPublicApi publicApi = factory.createPublicApi(signatory);
ServerInformationModel serverInfo = publicApi.getServerInformation();

// 4. Create encryptor from server's public keys
Encryptor encryptor = new EncryptorFactory().create(publicApi);

// 5. Create main API
TaxApi taxApi = factory.createApi(signatory, encryptor);
```

### Required Inputs
- **Private Key**: PKCS#8 format (.pem file)
- **Certificate**: X509 certificate (.cer file)
- **Memory ID**: Fiscal identifier assigned by tax authority
- **Base URL**: Tax API endpoint

### Output
- Initialized `TaxApi` instance ready for operations

---

## 2. Invoice Submission Flow

### Purpose
Submit one or more invoices to the tax authority.

### Sequence Diagram
```
┌─────────┐     ┌────────┐     ┌──────────────┐     ┌────────────┐     ┌──────────┐
│  Client │     │ TaxApi │     │PacketProvider│     │ HttpClient │     │Tax Server│
└────┬────┘     └───┬────┘     └──────┬───────┘     └─────┬──────┘     └────┬─────┘
     │              │                 │                   │                 │
     │sendInvoices()│                 │                   │                 │
     │─────────────>│                 │                   │                 │
     │              │ create packets  │                   │                 │
     │              │────────────────>│                   │                 │
     │              │                 │ sign (JWS)        │                 │
     │              │                 │──────────┐        │                 │
     │              │                 │<─────────┘        │                 │
     │              │                 │ encrypt (JWE)     │                 │
     │              │                 │──────────┐        │                 │
     │              │                 │<─────────┘        │                 │
     │              │<────────────────│ PacketDto[]      │                 │
     │              │                 │                   │                 │
     │              │ POST /invoice   │                   │                 │
     │              │─────────────────────────────────────>│                │
     │              │                 │                   │ validate & save │
     │              │                 │                   │<────────────────│
     │              │<─────────────────────────────────────│ response       │
     │<─────────────│ InvoiceResponseModel[]              │                 │
```

### Code Example
```java
// Create invoice
InvoiceDto invoice = InvoiceDto.builder()
    .header(HeaderDto.builder()
        .taxId("GENERATED_TAX_ID")
        .indatim(System.currentTimeMillis())
        .indati2m(System.currentTimeMillis())
        .inty(1)  // Invoice type
        .inp(1)   // Invoice pattern
        .ins(1)   // Invoice subject
        .tins("SELLER_TIN")
        .tob(2)   // Type of buyer
        .bid("BUYER_ID")
        .bpc("POSTAL_CODE")
        .scln("SELLER_CLIENT_ID")
        .scc("SELLER_COUNTRY_CODE")
        .setm(1)  // Settlement method
        .cap(100000L)  // Total amount
        .insp(90000L)  // Amount before discount
        .tvam(9000L)   // VAT amount
        .todam(1000L)  // Discount amount
        .tbill(99000L) // Total bill
        .build())
    .body(List.of(
        BodyItemDto.builder()
            .sstid("SERVICE_STUFF_ID")
            .sstt("Product Name")
            .am(1.0)      // Amount
            .mu("UNIT")   // Measurement unit
            .fee(100000L) // Fee
            .cfee(100000L)
            .cut("IRR")
            .exr(1)
            .prdis(90000L)
            .dis(10000L)
            .adis(90000L)
            .vra(9.0)     // VAT rate
            .vam(8100L)   // VAT amount
            .tsstam(98100L)
            .build()
    ))
    .payments(List.of(
        PaymentItemDto.builder()
            .iinn("BANK_ID")
            .acn("ACCOUNT_NUMBER")
            .trmn("TERMINAL_NUMBER")
            .trn("TRACE_NUMBER")
            .pcn("CARD_NUMBER")
            .pid("PAYMENT_ID")
            .pdt(System.currentTimeMillis())
            .build()
    ))
    .build();

// Send invoices
List<InvoiceResponseModel> responses = taxApi.sendInvoices(List.of(invoice));

// Process response
for (InvoiceResponseModel response : responses) {
    System.out.println("UID: " + response.getUid());
    System.out.println("Reference: " + response.getReferenceNumber());
    System.out.println("Tax ID: " + response.getTaxId());
}
```

### Required Inputs
- **InvoiceDto**: Complete invoice data including:
  - `header`: Invoice metadata (70+ fields)
  - `body`: Line items with products/services
  - `payments`: Payment details (optional)
  - `extension`: Additional data (optional)

### Output
- `List<InvoiceResponseModel>`:
  - `uid`: Unique identifier
  - `referenceNumber`: Server reference
  - `taxId`: Generated tax ID
  - `errorCode`: Error code (if failed)
  - `errorDetail`: Error description (if failed)

---

## 3. Invoice Inquiry Flows

### 3.1 Inquiry by Time

### Purpose
Query invoices within a date range.

### Sequence Diagram
```
┌─────────┐     ┌────────┐     ┌──────────┐
│  Client │     │ TaxApi │     │Tax Server│
└────┬────┘     └───┬────┘     └────┬─────┘
     │              │               │
     │inquiryByTime()               │
     │─────────────>│               │
     │              │ GET /inquiry  │
     │              │──────────────>│
     │              │<──────────────│
     │<─────────────│ results       │
```

### Code Example
```java
List<InquiryResultModel> results = taxApi.inquiryByTime(
    InquiryDto.builder()
        .start(ZonedDateTime.now().minusDays(30))
        .end(ZonedDateTime.now())
        .status(RequestStatus.SUCCESS)  // Optional
        .build()
);

for (InquiryResultModel result : results) {
    System.out.println("Tax ID: " + result.getTaxId());
    System.out.println("Status: " + result.getStatus());
}
```

### 3.2 Inquiry by UID

### Purpose
Query specific invoices by their unique identifiers.

### Code Example
```java
List<InquiryResultModel> results = taxApi.inquiryByUid(
    InquiryByUidDto.builder()
        .uidList(List.of("uid1", "uid2", "uid3"))
        .fiscalId("YOUR_FISCAL_ID")
        .build()
);
```

### 3.3 Inquiry by Reference Number

### Purpose
Query invoices by server-assigned reference numbers.

### Code Example
```java
List<InquiryResultModel> results = taxApi.inquiryByReferenceId(
    InquiryByReferenceNumberDto.builder()
        .referenceNumber(List.of("ref1", "ref2"))
        .build()
);
```

### 3.4 Invoice Status Inquiry

### Purpose
Check the processing status of submitted invoices.

### Code Example
```java
List<InvoiceStatusInquiryResultModel> statuses =
    taxApi.getInvoiceStatusInquiry(List.of("taxId1", "taxId2"));

for (InvoiceStatusInquiryResultModel status : statuses) {
    System.out.println("Tax ID: " + status.getTaxId());
    System.out.println("Status: " + status.getStatus());
}
```

---

## 4. Payment Registration Flow

### Purpose
Register payment information for an invoice.

### Sequence Diagram
```
┌─────────┐     ┌────────┐     ┌──────────┐
│  Client │     │ TaxApi │     │Tax Server│
└────┬────┘     └───┬────┘     └────┬─────┘
     │              │               │
     │registerPaymentRequest()      │
     │─────────────>│               │
     │              │ POST /payment │
     │              │──────────────>│
     │              │<──────────────│
     │<─────────────│ result        │
```

### Code Example
```java
RegisterPaymentResultModel result = taxApi.registerPaymentRequest(
    RegisterPaymentRequestDto.builder()
        .taxId("A111H104EA6001D0B32AC6")
        .paymentDate(System.currentTimeMillis())
        .paidAmount(100000L)
        .paymentMethod(PaymentMethod.CARD)
        .build()
);

System.out.println("Payment registered: " + result.getSuccess());
```

### Required Inputs
- **taxId**: Invoice tax ID
- **paymentDate**: Payment timestamp
- **paidAmount**: Amount paid
- **paymentMethod**: Payment method (CASH, CARD, CREDIT, INTERNET, etc.)

### Output
- `RegisterPaymentResultModel` with success/failure status

---

## 5. Taxpayer Information Flow

### Purpose
Retrieve information about a taxpayer by their economic code.

### Sequence Diagram
```
┌─────────┐     ┌────────┐     ┌──────────┐
│  Client │     │ TaxApi │     │Tax Server│
└────┬────┘     └───┬────┘     └────┬─────┘
     │              │               │
     │getTaxpayer() │               │
     │─────────────>│               │
     │              │ GET /taxpayer │
     │              │──────────────>│
     │              │<──────────────│
     │<─────────────│ TaxpayerModel │
```

### Code Example
```java
TaxpayerModel taxpayer = taxApi.getTaxpayer("1234567890");

System.out.println("Name: " + taxpayer.getName());
System.out.println("National ID: " + taxpayer.getNationalId());
System.out.println("Status: " + taxpayer.getStatus());
System.out.println("Type: " + taxpayer.getType());
```

### Required Inputs
- **economicCode**: Taxpayer's economic code

### Output
- `TaxpayerModel`:
  - Name
  - National ID
  - Status
  - Type
  - Address
  - Economic code

---

## 6. Fiscal Information Flow

### Purpose
Retrieve fiscal information for a memory/fiscal ID.

### Code Example
```java
FiscalFullInformationModel fiscalInfo =
    taxApi.getFiscalInformation("MEMORY_ID");

System.out.println("Fiscal ID: " + fiscalInfo.getFiscalId());
System.out.println("Status: " + fiscalInfo.getStatus());
```

---

## Error Handling

### Common Errors

| Error Code | Description | Resolution |
|------------|-------------|------------|
| `AUTH_001` | Invalid signature | Check private key and certificate |
| `AUTH_002` | Expired nonce | Retry request |
| `INV_001` | Invalid invoice data | Validate invoice fields |
| `INV_002` | Duplicate tax ID | Generate new tax ID |
| `SRV_001` | Server unavailable | Retry with backoff |

### Exception Handling Example
```java
try {
    List<InvoiceResponseModel> responses = taxApi.sendInvoices(invoices);
} catch (TaxCollectionApiException e) {
    System.err.println("API Error: " + e.getErrorResponse());
} catch (UnknownResponseException e) {
    System.err.println("Unknown response: " + e.getMessage());
}
```

---

## Best Practices

1. **Retry Logic**: Implement exponential backoff for transient failures
2. **Batch Processing**: Submit invoices in batches (max 100 per request)
3. **Key Rotation**: Refresh encryption keys before expiration (1 hour)
4. **Logging**: Enable debug logging for troubleshooting
5. **Validation**: Validate invoice data before submission
6. **Idempotency**: Store and check tax IDs to prevent duplicates
