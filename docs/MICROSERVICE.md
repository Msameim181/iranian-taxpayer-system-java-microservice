# Tax Collect Data SDK - Microservice Guide

## Overview

This guide explains how to deploy the Tax Collect Data SDK as a microservice that can be integrated with other services via REST API or message queues.

## Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                        Your Infrastructure                          │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│  ┌─────────────┐     ┌──────────────────┐     ┌─────────────────┐  │
│  │   Service A │────>│  Tax Microservice │────>│   Tax Server    │  │
│  └─────────────┘     │                  │     │   (External)    │  │
│                      │  - REST API      │     └─────────────────┘  │
│  ┌─────────────┐     │  - Health Check  │                          │
│  │   Service B │────>│  - Metrics       │                          │
│  └─────────────┘     └──────────────────┘                          │
│                              │                                      │
│  ┌─────────────┐             │                                      │
│  │   Service C │─────────────┘                                      │
│  └─────────────┘                                                    │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

## Microservice Wrapper

To expose the SDK as a microservice, you need to create a REST wrapper. Here's a Spring Boot implementation:

### Project Structure
```
tax-microservice/
├── src/main/java/
│   └── com/example/tax/
│       ├── TaxMicroserviceApplication.java
│       ├── config/
│       │   └── TaxApiConfig.java
│       ├── controller/
│       │   └── TaxController.java
│       ├── service/
│       │   └── TaxService.java
│       └── dto/
│           └── (request/response DTOs)
├── src/main/resources/
│   └── application.yml
├── Dockerfile
└── pom.xml
```

### Sample REST Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/invoices` | Submit invoices |
| GET | `/api/v1/invoices/inquiry` | Query invoices by time |
| GET | `/api/v1/invoices/{taxId}` | Get invoice status |
| POST | `/api/v1/payments` | Register payment |
| GET | `/api/v1/taxpayers/{economicCode}` | Get taxpayer info |
| GET | `/health` | Health check |
| GET | `/metrics` | Prometheus metrics |

### Environment Variables

| Variable | Description | Required |
|----------|-------------|----------|
| `TAX_API_BASE_URL` | Tax server URL | Yes |
| `TAX_MEMORY_ID` | Fiscal/Memory ID | Yes |
| `TAX_PRIVATE_KEY_PATH` | Path to private key | Yes |
| `TAX_CERTIFICATE_PATH` | Path to certificate | Yes |
| `SERVER_PORT` | Service port (default: 8080) | No |

## Docker Deployment

### Building the Image
```bash
docker build -t tax-microservice:latest .
```

### Running the Container
```bash
docker run -d \
  --name tax-microservice \
  -p 8080:8080 \
  -v /path/to/keys:/app/keys:ro \
  -e TAX_API_BASE_URL=https://tp.tax.gov.ir/requestsmanager \
  -e TAX_MEMORY_ID=YOUR_MEMORY_ID \
  -e TAX_PRIVATE_KEY_PATH=/app/keys/private.pem \
  -e TAX_CERTIFICATE_PATH=/app/keys/certificate.cer \
  tax-microservice:latest
```

### Docker Compose Integration
```yaml
version: '3.8'
services:
  tax-microservice:
    image: tax-microservice:latest
    ports:
      - "8080:8080"
    volumes:
      - ./keys:/app/keys:ro
    environment:
      TAX_API_BASE_URL: https://tp.tax.gov.ir/requestsmanager
      TAX_MEMORY_ID: ${TAX_MEMORY_ID}
      TAX_PRIVATE_KEY_PATH: /app/keys/private.pem
      TAX_CERTIFICATE_PATH: /app/keys/certificate.cer
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/health"]
      interval: 30s
      timeout: 10s
      retries: 3
    networks:
      - backend

  your-service:
    image: your-service:latest
    depends_on:
      tax-microservice:
        condition: service_healthy
    environment:
      TAX_SERVICE_URL: http://tax-microservice:8080
    networks:
      - backend

networks:
  backend:
    driver: bridge
```

## Kubernetes Deployment

### Deployment YAML
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: tax-microservice
spec:
  replicas: 2
  selector:
    matchLabels:
      app: tax-microservice
  template:
    metadata:
      labels:
        app: tax-microservice
    spec:
      containers:
      - name: tax-microservice
        image: tax-microservice:latest
        ports:
        - containerPort: 8080
        env:
        - name: TAX_API_BASE_URL
          valueFrom:
            configMapKeyRef:
              name: tax-config
              key: api-url
        - name: TAX_MEMORY_ID
          valueFrom:
            secretKeyRef:
              name: tax-secrets
              key: memory-id
        volumeMounts:
        - name: keys
          mountPath: /app/keys
          readOnly: true
        livenessProbe:
          httpGet:
            path: /health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /health
            port: 8080
          initialDelaySeconds: 5
          periodSeconds: 5
      volumes:
      - name: keys
        secret:
          secretName: tax-keys
---
apiVersion: v1
kind: Service
metadata:
  name: tax-microservice
spec:
  selector:
    app: tax-microservice
  ports:
  - port: 80
    targetPort: 8080
```

## Integration Patterns

### 1. Synchronous REST Integration
```java
// Client service calling tax microservice
RestTemplate restTemplate = new RestTemplate();
InvoiceResponse response = restTemplate.postForObject(
    "http://tax-microservice:8080/api/v1/invoices",
    invoiceRequest,
    InvoiceResponse.class
);
```

### 2. Async with Message Queue
```yaml
# docker-compose.yml with RabbitMQ
services:
  rabbitmq:
    image: rabbitmq:3-management
    ports:
      - "5672:5672"
      - "15672:15672"

  tax-microservice:
    image: tax-microservice:latest
    environment:
      RABBITMQ_HOST: rabbitmq
      INVOICE_QUEUE: invoice-queue
      RESULT_QUEUE: result-queue
```

### 3. Event-Driven with Kafka
```yaml
services:
  kafka:
    image: confluentinc/cp-kafka:latest

  tax-microservice:
    image: tax-microservice:latest
    environment:
      KAFKA_BOOTSTRAP_SERVERS: kafka:9092
      INVOICE_TOPIC: invoices
      RESULT_TOPIC: invoice-results
```

## Monitoring & Observability

### Prometheus Metrics
```yaml
# prometheus.yml
scrape_configs:
  - job_name: 'tax-microservice'
    static_configs:
      - targets: ['tax-microservice:8080']
    metrics_path: '/metrics'
```

### Key Metrics to Monitor
- `tax_invoices_submitted_total` - Total invoices submitted
- `tax_api_request_duration_seconds` - API latency
- `tax_api_errors_total` - Error count by type
- `tax_encryption_key_age_seconds` - Time since key refresh

### Logging
```yaml
# logback.xml configuration
<appender name="JSON" class="ch.qos.logback.core.ConsoleAppender">
  <encoder class="net.logstash.logback.encoder.LogstashEncoder"/>
</appender>
```

## Security Best Practices

1. **Key Management**
   - Store keys in Kubernetes Secrets or HashiCorp Vault
   - Never commit keys to version control
   - Rotate keys according to policy

2. **Network Security**
   - Use internal network for service-to-service communication
   - Enable TLS for all external connections
   - Implement rate limiting

3. **Container Security**
   - Run as non-root user
   - Use read-only filesystem where possible
   - Scan images for vulnerabilities

## Troubleshooting

### Common Issues

| Issue | Possible Cause | Solution |
|-------|----------------|----------|
| Connection timeout | Tax server unreachable | Check network, firewall rules |
| Auth failure | Invalid/expired keys | Verify keys, check certificate expiration |
| Encryption error | Key mismatch | Refresh server public keys |
| Memory issues | Large batch size | Reduce batch size, increase heap |
