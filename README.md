# Apex Vision - Route Optimization Microservice

## General Description

This microservice acts as the "Mathematical Brain" of the Apex Vision platform. Its sole responsibility is to receive an unordered list of geographic coordinates and return the most efficient delivery route possible.

The service is Stateless and is designed to integrate with the .NET Orchestrator through REST API.

## Technology Stack

- **Language**: Java 17 (LTS)
- **Framework**: Spring Boot 3.4.12
- **Dependency Manager**: Maven
- **API Documentation**: OpenAPI / Swagger UI
- **Containerization**: Docker (Multi-Stage Build with Alpine Runtime)

## Architecture and Algorithms

This service models the routing problem based on Graph Theory and implements robust software architecture patterns.

### 1. Mathematical Model

- **Nodes (Vertices)**: Each location (order) is treated as a node in a complete graph.
- **Edges**: Connections between nodes represent the physical distance between them.

### 2. Optimization Strategy (Strategy Pattern)

The system uses the **Strategy Pattern** to decouple the optimization logic from the business service. Currently, it implements:

- **Algorithm**: Nearest Neighbor (Greedy Heuristic).
- **Complexity**: O(N²).
- **Logic**: Iteratively evaluates the unvisited node with the lowest cost (shortest distance) from the current position.

### 3. Resilience & Fallback

To ensure High Availability (HA), the service includes a **Fallback Mechanism**:
- If the optimization algorithm fails (e.g., memory overflow or calculation error), the system **catches the exception and returns the original list**.
- This guarantees that the driver always receives a route (even if not optimized) instead of a 500 Server Error.

### 4. Geodesic Calculation (Haversine Formula)

Unlike basic systems that use Euclidean distance (Pythagoras), this engine uses Spherical Trigonometry.

- **Formula**: Haversine.
- **Justification**: Calculates the great-circle distance considering Earth's curvature, ensuring precision in logistical mileage.

## API Reference

### Optimize Route

Orders a list of stops to minimize total distance traveled.

**Endpoint**: `POST /api/v1/optimize`

**Content-Type**: `application/json`

#### Request Body (Example)

```json
{
  "fleetId": "camion-norte-01",
  "locations": [
    {
      "id": 101,
      "latitude": 4.59805, 
      "longitude": -74.07583 
    },
    {
      "id": 102,
      "latitude": 4.67678, 
      "longitude": -74.04823
    },
    {
      "id": 103,
      "latitude": 4.61539, 
      "longitude": -74.06915
    }
  ]
}
```

Note: It is assumed that the first element of the list (index 0) is the starting point (Depot).

#### Response Body (Example)

```json
{
  "totalDistanceKm": 9.27,
  "optimizedOrder": [
    {
      "id": 101,
      "latitude": 4.59805,
      "longitude": -74.07583,
      "sequenceNumber": 0
    },
    {
      "id": 103,
      "latitude": 4.61539,
      "longitude": -74.06915,
      "sequenceNumber": 1
    },
    {
      "id": 102,
      "latitude": 4.67678,
      "longitude": -74.04823,
      "sequenceNumber": 2
    }
  ]
}
```

#### Status Codes

- **200 OK**: Successful calculation.
- **400 Bad Request**: Invalid data (Latitude > 90, empty list, etc.).

## Deployment and Installation

### Local Execution

```bash
# 1. Clone repository
git clone <url-repo>

# 2. Package
./mvnw clean package

# 3. Execute
java -jar target/route-optimizer-0.0.1-SNAPSHOT.jar
```

### Docker Execution (Automated)

The project uses a Multi-Stage Dockerfile. You don't need Maven installed locally; the container handles the compilation.

```bash
# 1. Build the image (Compiles code + Builds runtime image)
docker build -t apex-vision/optimizer .

# 2. Run the container (Port 8080)
docker run -p 8080:8080 apex-vision/optimizer
```

### Interactive Documentation (Swagger)

Once deployed, the live API documentation is available at:

**URL**: http://localhost:8080/swagger-ui.html

## Project Structure

```
com.apexvision.optimizer
├── controller: Exposes REST endpoints (RouteController)
├── service: Business logic and Orchestration (RouteService)
├── strategy: Optimization algorithms implementations (NearestNeighborStrategyImpl)
├── utils: Mathematical formulas (GeoUtils)
└── dto: Data transfer objects and validations (RouteRequest, LocationDto)
```