# Network Monitoring & Troubleshooting System

A Java Spring Boot-based network monitoring application that tracks host availability, response time, monitoring history, network KPIs, and connectivity-related incidents through REST APIs and a web dashboard.

## Overview

The Network Monitoring & Troubleshooting System is designed to provide a simple way to monitor network hosts and identify connectivity failures.

The application performs reachability checks for registered hosts, records monitoring results, calculates network KPIs, and automatically creates incidents when a host becomes unreachable. When connectivity is restored, the corresponding open incident is automatically closed.

## Features

* Register and manage monitored hosts
* Monitor host reachability using IP addresses
* Detect `UP` and `DOWN` network status
* Measure host response time
* Store monitoring history in MySQL
* Calculate network KPIs:

    * Total checks
    * Availability percentage
    * Failure count
    * Average response time
* Automatically create incidents for unreachable hosts
* Prevent duplicate open incidents for the same host
* Automatically close open incidents when connectivity is restored
* Dashboard for host status and overall monitoring information
* REST APIs for monitoring and data access
* Postman-based API testing

## Technology Stack

| Technology      | Usage                           |
| --------------- | ------------------------------- |
| Java 17         | Application development         |
| Spring Boot     | Backend framework               |
| Spring Data JPA | Database persistence            |
| Hibernate       | ORM                             |
| MySQL           | Relational database             |
| REST APIs       | Backend communication           |
| HTML            | Frontend structure              |
| CSS             | Frontend styling                |
| JavaScript      | Dashboard interaction           |
| Postman         | API testing                     |
| Maven           | Build and dependency management |
| Git & GitHub    | Version control                 |

## System Architecture

```text
                    ┌─────────────────────────┐
                    │     Web Dashboard       │
                    │   HTML/CSS/JavaScript   │
                    └────────────┬────────────┘
                                 │
                                 │ REST APIs
                                 ▼
                    ┌─────────────────────────┐
                    │     Spring Boot         │
                    │       Controller        │
                    └────────────┬────────────┘
                                 │
                                 ▼
                    ┌─────────────────────────┐
                    │      Service Layer      │
                    │ Network Monitoring Logic│
                    └────────────┬────────────┘
                                 │
                    ┌────────────┴────────────┐
                    │                         │
                    ▼                         ▼
          ┌──────────────────┐      ┌──────────────────┐
          │ Host Reachability│      │ Incident & KPI   │
          │     Checking     │      │    Processing    │
          └──────────────────┘      └────────┬─────────┘
                                             │
                                             ▼
                                  ┌─────────────────────┐
                                  │        MySQL         │
                                  │     Database         │
                                  └─────────────────────┘
```

## Main Components

### Monitored Hosts

Stores the hosts registered for monitoring.

Information includes:

* Host ID
* Host name
* IP address
* Description
* Active status
* Creation timestamp

### Monitoring Results

Stores the result of each connectivity check.

Information includes:

* Host
* Status (`UP` / `DOWN`)
* Response time
* Error message
* Check timestamp

### Network Incidents

Tracks connectivity-related incidents.

The incident lifecycle is:

```text
Host Unreachable
       ↓
Incident Created
       ↓
     OPEN
       ↓
Connectivity Restored
       ↓
    CLOSED
```

## KPI Monitoring

The application calculates monitoring metrics for each host.

### Availability

```text
Availability % = (UP Checks / Total Checks) × 100
```

### Failure Count

The number of monitoring checks that resulted in a `DOWN` status.

### Average Response Time

The average response time across monitoring results that contain a response-time value.

## REST API Endpoints

### Host Management

| Method | Endpoint          | Description             |
| ------ | ----------------- | ----------------------- |
| POST   | `/api/hosts`      | Add a monitored host    |
| GET    | `/api/hosts`      | Get all monitored hosts |
| PUT    | `/api/hosts/{id}` | Update a monitored host |

### Monitoring

| Method | Endpoint                       | Description             |
| ------ | ------------------------------ | ----------------------- |
| POST   | `/api/monitoring/check/{id}`   | Check host reachability |
| GET    | `/api/monitoring/history/{id}` | Get monitoring history  |
| GET    | `/api/monitoring/kpi/{id}`     | Get host KPI summary    |

### Incidents

| Method | Endpoint              | Description              |
| ------ | --------------------- | ------------------------ |
| GET    | `/api/incidents/{id}` | Get incidents for a host |

### Dashboard

| Method | Endpoint                 | Description                   |
| ------ | ------------------------ | ----------------------------- |
| GET    | `/api/dashboard/summary` | Get overall dashboard summary |

## Example Monitoring Flow

A host can be monitored using:

```text
POST /api/monitoring/check/{id}
```

If the host is reachable:

```text
Status: UP
```

If the host is unreachable:

```text
Status: DOWN
Error: Host is not reachable
```

An incident is automatically created with:

```text
Incident Type: HOST_UNREACHABLE
Status: OPEN
```

When the host becomes reachable again, the open incident is automatically changed to:

```text
Status: CLOSED
```

## Database

The application uses MySQL with the following main tables:

```text
monitored_hosts
       │
       ├───────────────┐
       │               │
       ▼               ▼
monitoring_results   network_incidents
```

Database name:

```text
network_monitoring_db
```

## Running the Application Locally

### Prerequisites

Make sure the following are installed:

* Java 17 or later
* MySQL 8
* Maven (optional because Maven Wrapper is included)
* IntelliJ IDEA or another Java IDE
* Postman (optional for API testing)

### 1. Clone the repository

```bash
git clone https://github.com/yashshree7/network-monitoring-system.git
```

### 2. Open the project

Open the cloned project in IntelliJ IDEA.

### 3. Create the database

In MySQL:

```sql
CREATE DATABASE network_monitoring_db;
```

### 4. Configure database credentials

Configure your local MySQL password using the application's local environment configuration.

The application expects:

```text
DB_PASSWORD
```

to contain the MySQL password.

Do not commit database passwords or other secrets to GitHub.

### 5. Run the application

Using Maven Wrapper:

**Windows**

```bash
mvnw.cmd spring-boot:run
```

Or run `NetworkMonitoringSystemApplication` directly from IntelliJ IDEA.

### 6. Open the dashboard

After the application starts, open:

```text
http://localhost:8080/
```

## API Testing

The REST APIs were tested using Postman, including:

* Host registration
* Host retrieval
* Host updates
* Reachability checks
* UP/DOWN detection
* Monitoring history
* KPI calculations
* Incident creation
* Duplicate incident prevention
* Incident recovery and closure
* Dashboard summary

## Project Structure

```text
network-monitoring-system
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.example.networkmonitoringsystem
│   │   │       ├── controller
│   │   │       ├── dto
│   │   │       ├── entity
│   │   │       ├── repository
│   │   │       └── service
│   │   │
│   │   └── resources
│   │       ├── application.properties
│   │       └── static
│   │           ├── index.html
│   │           ├── script.js
│   │           └── style.css
│   │
│   ├── test
│   │
│   └── ...
│
├── pom.xml
├── .gitignore
├── mvnw
├── mvnw.cmd
└── README.md
```

## Key Learning Outcomes

Through this project, I worked with:

* Java and object-oriented programming
* Spring Boot application development
* REST API design
* Spring Data JPA and Hibernate
* MySQL database integration
* Network host reachability concepts
* Response-time and availability monitoring
* Incident lifecycle management
* KPI calculation and interpretation
* Frontend-to-backend integration
* API testing using Postman
* Git and GitHub version control

## Future Enhancements

Potential improvements include:

* Scheduled automatic monitoring
* Historical KPI charts
* Advanced filtering and reporting
* Email or notification alerts
* Authentication and role-based access
* Pagination for monitoring history
* Swagger/OpenAPI documentation
* Docker containerization
* CI/CD pipeline
* Cloud deployment

## Author

**Yashshree Shah**

B.E. Computer Engineering | 2024

Interested in Java, backend development, networking, cloud technologies, and software engineering.
