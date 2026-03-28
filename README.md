# DevMon: Distributed Network Observability System


## Status: Active Development :-  Phase 2: Backend MVP With 48-hour Data Retention (Complete)  


DevMon is a high-frequency telemetry ingestion system designed to provide real-time observability for remote network devices. The system utilizes a lightweight Go agent to stream system metrics to a Java Spring Boot backend, managed by a PostgreSQL data lifecycle strategy  
## System Architecture
  The project is built on a decoupled, three-tier architecture:

  1. Metric Ingestion (Go): A lightweight, concurrent binary that collects CPU and RAM metrics every 2 seconds. It communicates via signed REST API calls to minimize overhead on the host machine.

  2. Processing Engine (Spring Boot): A Java-based backend that validates incoming telemetry, manages device states (Online/Offline), and persists data using Spring Data JPA.

  3. Storage Layer (Postgres/Supabase): A managed database layer optimized for time-series data using custom B-Tree indexing on temporal columns.

## Technical Features


  - High-Frequency Telemetry
  - Supports heartbeat intervals as low as 2 seconds without significant database contention.
  - Uses a stateless REST architecture for the ingestion endpoint to ensure horizontal scalability.
  - Automated Data Lifecycle Management
  - Implements a Time-Series TTL (Time-To-Live) strategy.
  - An automated **Cleanup Scheduler** purges data older than 48 hours every 60 minutes.
  - Maintains a "Steady State" storage footprint (~8.6MB per device per 48h), ensuring the system remains within free-tier cloud constraints.

## Data Integrity & Performance
  - Utilizes a Strong/Weak Entity relationship between Devices and Metrics.
  - Optimized PostgreSQL indexing on the recorded_at column to transform "Full Table Scans" into "Index Seeks" during cleanup and retrieval.

## Tech Stack

- Backend: Java 21, Spring Boot 3.x, Spring Data JPA

- Agent: Go 1.2x (Native Binary)

- Database: PostgreSQL (Supabase)

- Environment: Garuda Linux (Arch-based)

- Tools: Maven, Git, Postman, JDBC

## Development Roadmap
[x] Phase 1: Core Backend API & Go Agent Ingestion.

[x] Phase 2: Automated 48-hour Data Retention Logic.

[ ] Phase 3: User Authentication & JWT Integration.

[ ] Phase 4: Real-time Dashboard (React Frontend).

[ ] Phase 5: Row-Level Security (RLS) Policies for multi-tenant data isolation.

## Implementation Details (For Evaluators)
  1. Database Constraints: Calculated storage limits at 500MB allow for ~50 concurrent devices in a 48-hour sliding window.

  2. Concurrency: The Go agent utilizes standard library tickers to ensure precise timing with minimal resource drift.
     
  3. Transaction Management: All persistence logic is wrapped in (@Transactional) boundaries to ensure atomicity between metric logging and device status updates.
