# Tax Collect Data SDK - Docker Guide

## Quick Start

### 1. Prerequisites
- Docker 20.10+
- Docker Compose 2.0+
- Your private key (.pem) and certificate (.cer) from tax authority

### 2. Setup Keys
```bash
# Create keys directory
mkdir -p keys

# Copy your credentials
cp /path/to/your/private.pem keys/private.pem
cp /path/to/your/certificate.cer keys/certificate.cer
```

### 3. Configure Environment
```bash
# Copy example environment file
cp .env.example .env

# Edit with your values
nano .env
```

### 4. Build and Run

#### Simple Deployment
```bash
# Build the image
docker-compose build

# Start the service
docker-compose up -d

# Check logs
docker-compose logs -f tax-microservice
```

#### Full Stack Deployment (with monitoring)
```bash
docker-compose -f docker-compose.full.yml up -d
```

---

## Docker Commands Reference

### Build
```bash
# Build image
docker build -t tax-collect-data-sdk:2.0.28 .

# Build with no cache
docker build --no-cache -t tax-collect-data-sdk:2.0.28 .
```

### Run
```bash
# Run standalone container
docker run -d \
  --name tax-microservice \
  -p 8080:8080 \
  -v $(pwd)/keys:/app/keys:ro \
  -e TAX_API_BASE_URL=https://tp.tax.gov.ir/requestsmanager \
  -e TAX_MEMORY_ID=YOUR_MEMORY_ID \
  tax-collect-data-sdk:2.0.28

# Run with custom JVM options
docker run -d \
  --name tax-microservice \
  -p 8080:8080 \
  -v $(pwd)/keys:/app/keys:ro \
  -e TAX_MEMORY_ID=YOUR_MEMORY_ID \
  -e JAVA_OPTS="-Xms512m -Xmx1g -XX:+UseG1GC" \
  tax-collect-data-sdk:2.0.28
```

### Manage
```bash
# View logs
docker logs -f tax-microservice

# Check health
docker inspect --format='{{.State.Health.Status}}' tax-microservice

# Shell access
docker exec -it tax-microservice sh

# Stop
docker stop tax-microservice

# Remove
docker rm tax-microservice
```

---

## Configuration

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `TAX_API_BASE_URL` | `https://tp.tax.gov.ir/requestsmanager` | Tax server URL |
| `TAX_MEMORY_ID` | (required) | Your fiscal/memory ID |
| `TAX_PRIVATE_KEY_PATH` | `/app/keys/private.pem` | Private key path in container |
| `TAX_CERTIFICATE_PATH` | `/app/keys/certificate.cer` | Certificate path in container |
| `JAVA_OPTS` | `-Xms256m -Xmx512m -XX:+UseG1GC` | JVM options |
| `TZ` | `Asia/Tehran` | Timezone |

### Volume Mounts

| Host Path | Container Path | Purpose |
|-----------|----------------|---------|
| `./keys` | `/app/keys` | Cryptographic keys (read-only) |
| `tax-logs` | `/app/logs` | Application logs |

### Ports

| Port | Protocol | Description |
|------|----------|-------------|
| 8080 | HTTP | API endpoint |

---

## Architecture Options

### Option 1: Standalone
Simple deployment for single-service use.
```
┌────────────────┐
│ Tax Microservice│
│    (8080)      │
└────────────────┘
```

### Option 2: With Database (Recommended)
Store invoice records and audit logs.
```
┌────────────────┐     ┌────────────┐
│ Tax Microservice│────>│ PostgreSQL │
│    (8080)      │     │   (5432)   │
└────────────────┘     └────────────┘
        │
        └──────────────>┌────────────┐
                        │   Redis    │
                        │   (6379)   │
                        └────────────┘
```

### Option 3: Full Stack
Production-ready with monitoring and message queue.
```
                    ┌────────────┐
                    │   Nginx    │
                    │  (80/443)  │
                    └─────┬──────┘
                          │
        ┌─────────────────┼─────────────────┐
        │                 │                 │
        ▼                 ▼                 ▼
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│Tax Microservice│  │  Prometheus  │  │   Grafana    │
│    (8080)    │  │    (9090)    │  │   (3000)     │
└──────┬───────┘  └──────────────┘  └──────────────┘
       │
       ├──────────────────┬──────────────────┐
       │                  │                  │
       ▼                  ▼                  ▼
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│  PostgreSQL  │  │    Redis     │  │  RabbitMQ    │
│   (5432)     │  │   (6379)     │  │ (5672/15672) │
└──────────────┘  └──────────────┘  └──────────────┘
```

---

## Production Recommendations

### 1. Security
```yaml
# docker-compose.prod.yml additions
services:
  tax-microservice:
    # Run as non-root (already configured in Dockerfile)
    user: "1001:1001"

    # Read-only filesystem
    read_only: true
    tmpfs:
      - /tmp

    # Security options
    security_opt:
      - no-new-privileges:true

    # Resource limits
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 1G
        reservations:
          cpus: '0.5'
          memory: 256M
```

### 2. High Availability
```yaml
services:
  tax-microservice:
    deploy:
      replicas: 3
      update_config:
        parallelism: 1
        delay: 10s
      restart_policy:
        condition: on-failure
        delay: 5s
        max_attempts: 3
```

### 3. Logging
```yaml
services:
  tax-microservice:
    logging:
      driver: "json-file"
      options:
        max-size: "50m"
        max-file: "5"
        compress: "true"
```

### 4. Secrets Management
```yaml
services:
  tax-microservice:
    secrets:
      - private_key
      - certificate
    environment:
      TAX_PRIVATE_KEY_PATH: /run/secrets/private_key
      TAX_CERTIFICATE_PATH: /run/secrets/certificate

secrets:
  private_key:
    file: ./keys/private.pem
  certificate:
    file: ./keys/certificate.cer
```

---

## Troubleshooting

### Container won't start
```bash
# Check logs
docker logs tax-microservice

# Verify keys are mounted
docker exec tax-microservice ls -la /app/keys

# Check environment
docker exec tax-microservice env | grep TAX
```

### Connection issues
```bash
# Test from inside container
docker exec tax-microservice curl -v https://tp.tax.gov.ir/

# Check DNS resolution
docker exec tax-microservice nslookup tp.tax.gov.ir
```

### Memory issues
```bash
# Check memory usage
docker stats tax-microservice

# Increase memory limit
docker run -m 1g -e JAVA_OPTS="-Xms512m -Xmx768m" ...
```

### Health check failing
```bash
# Check health endpoint
curl http://localhost:8080/health

# View health check logs
docker inspect tax-microservice | jq '.[0].State.Health'
```

---

## CI/CD Integration

### GitLab CI
```yaml
build:
  stage: build
  script:
    - docker build -t $CI_REGISTRY_IMAGE:$CI_COMMIT_SHA .
    - docker push $CI_REGISTRY_IMAGE:$CI_COMMIT_SHA

deploy:
  stage: deploy
  script:
    - docker-compose pull
    - docker-compose up -d
```

### GitHub Actions
```yaml
- name: Build and push
  uses: docker/build-push-action@v5
  with:
    context: .
    push: true
    tags: ghcr.io/${{ github.repository }}:${{ github.sha }}
```
