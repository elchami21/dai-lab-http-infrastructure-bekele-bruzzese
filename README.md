# HTTP Infrastructure Lab

## Overview
Web infrastructure with static website, dynamic API, and reverse proxy supporting scalability and security.

## Step 1: Static Web Server
- Using Nginx to serve SB Admin 2 dashboard template
- Configuration:
  ```bash
  # Build and run static server
  cd static-server
  docker compose up --build
  ```
- Access via http://localhost:8080

### Verification
1. Check container is running: `docker ps`
2. Access dashboard at http://localhost:8080
3. Verify static assets (CSS, JS) load correctly

## Step 2: Docker Compose
- Configured basic multi-service setup
- Key features:
  - Build configuration
  - Port mapping
  - Service dependencies

### Verification
```bash
docker compose up --build  # Build and start services
docker compose down       # Stop services
```

## Step 3: HTTP API Server
- Javalin-based CRUD API for quotes
- Endpoints:
  - GET /api/quotes - List all quotes
  - GET /api/quotes/{id} - Get specific quote
  - POST /api/quotes - Create quote
  - PUT /api/quotes/{id} - Update quote
  - DELETE /api/quotes/{id} - Delete quote

### Testing API
```bash
# Get all quotes
curl http://localhost/api/quotes

# Create quote
curl -X POST http://localhost/api/quotes \
  -H "Content-Type: application/json" \
  -d '{"id":"3","text":"New quote","author":"Author"}'
```

## Step 4: Reverse Proxy (Traefik)
Traefik improves security by:
- Acting as single entry point
- Hiding internal service structure
- Managing SSL/TLS termination
- Controlling access to backend services

### Configuration
- Dashboard: http://localhost:8081
- Routes:
  - / → Static site
  - /api → API server

### Verification
1. Access Traefik dashboard
2. Verify routes in dashboard
3. Test both services through proxy

## Step 5: Scalability
Infrastructure supports multiple instances per service:
```bash
docker compose up --scale static-site=2 --scale api=2
```

### Verification
1. Check running instances: `docker ps`
2. Monitor load distribution in Traefik dashboard
3. Test services remain accessible during scaling

## Step 6: Load Balancing
- Round-robin for static content
- Sticky sessions for API (maintains session state)

### Verification
```bash
# Check sticky session cookie
curl -v http://localhost/api/quotes
```

## Step 7: HTTPS Security
- Self-signed certificates for development
- HTTPS enabled for all services
- Certificate location: ./certs/

### Verification
1. Access https://localhost
2. Verify certificate warning (self-signed)
3. Confirm HTTPS connection established