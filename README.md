# DAI Lab - HTTP Infrastructure

## Step 0: GitHub Repository
Repository structure:
```
.
├── api-server/         # Javalin API
├── static-server/      # Nginx static site
├── certs/             # SSL certificates
├── docker-compose.yml  # Infrastructure configuration
└── traefik.yml        # Reverse proxy configuration
```

## Step 1: Static Web Server
Uses Nginx to serve an SB Admin 2 dashboard template.

### Configuration
- Dockerfile uses nginx:alpine base image
- nginx.conf configures server on port 80
- Static content copied to /usr/share/nginx/html

### Demo procedure
1. `cd static-server`
2. `docker compose up --build`
3. Visit http://localhost:8080 to see dashboard

## Step 2: Docker Compose
Enables infrastructure management with a single configuration file.

### Configuration
```yaml
version: '3'
services:
  static-site:
    build: 
      context: ./static-server
    ports:
      - "8080:80"
```

### Demo procedure
1. `docker compose build` - Rebuilds images
2. `docker compose up` - Starts services
3. `docker compose down` - Stops services

## Step 3: HTTP API Server
Javalin-based CRUD API for managing quotes.

### Implementation
- CRUD operations via REST endpoints
- In-memory storage using ConcurrentHashMap
- JSON request/response format

### Demo procedure using API tool (e.g., Insomnia):
1. GET /api/quotes - List all quotes
2. POST /api/quotes - Create new quote
```json
{
    "id": "3",
    "text": "New quote",
    "author": "Author"
}
```
3. PUT /api/quotes/3 - Update quote
4. DELETE /api/quotes/3 - Delete quote

## Step 4: Reverse Proxy
Traefik provides secure access to internal services.

### Security Benefits
1. Single entry point - Reduces attack surface
2. Service isolation - Internal services not directly exposed
3. SSL termination - Centralized certificate management
4. Access control - Route-level security policies

### Demo procedure
1. Access Traefik dashboard:
   - Visit http://localhost:8081
   - View HTTP routers, services, middleware

2. Test routing:
   - http://localhost/ → Static site
   - http://localhost/api → API service

## Step 5: Scalability
Support for multiple service instances with dynamic detection.

### Demo procedure
1. Start with multiple instances:
```bash
docker compose up --scale static-site=2 --scale api=2
```

2. Verify in Traefik dashboard:
   - View multiple endpoints per service
   - Monitor health status

3. Add instance dynamically:
```bash
docker compose up --scale api=3
```

## Step 6: Load Balancing
Round-robin for static content, sticky sessions for API.

### Demo procedure
1. Check round-robin:
   - Make multiple requests to static site
   - Observe server distribution in Traefik logs

2. Verify sticky sessions:
```bash
curl -v http://localhost/api/quotes
# Check Set-Cookie header
# Same server handles subsequent requests
```

## Step 7: HTTPS
SSL/TLS encryption for all services.

### Configuration
1. Generate certificates:
```bash
openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
  -keyout certs/private.key -out certs/cert.crt
```

2. Configure Traefik:
```yaml
tls:
  certificates:
    - certFile: "/etc/traefik/certificates/cert.crt"
      keyFile: "/etc/traefik/certificates/private.key"
```

### Demo procedure
1. Access https://localhost
2. Verify SSL certificate (self-signed warning expected)
3. Test HTTPS for both services:
   - Static site: https://localhost
   - API: https://localhost/api/quotes