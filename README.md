# Java-Ecommerce-Microservices

This project is a Java-based e-commerce application designed using a microservices architecture. It leverages Spring Boot, Docker, Kafka, Keycloak, and several other technologies to create a scalable and robust system.

## Table of Contents

- [Overview](#overview)
- [Microservices](#microservices)
- [Technologies Used](#technologies-used)
- [Setup and Installation](#setup-and-installation)
- [Running Tests](#running-tests)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Contributing](#contributing)
- [License](#license)

## Overview

This project implements a full-fledged e-commerce application with separate microservices for managing products, customers, orders, carts, payments, notifications, and more. The services communicate with each other using REST APIs and Kafka for messaging.

## Microservices

- **Customer Service**: Manages customer data.
- **Product Service**: Manages product data.
- **Order Service**: Handles orders.
- **Cart Service**: Manages shopping carts.
- **Payment Service**: Processes payments.
- **Notification Service**: Sends notifications.
- **API Gateway**: Routes requests to the appropriate microservice.
- **Eureka Discovery Server**: Service discovery.
- **Zipkin**: Distributed tracing.
- **Keycloak**: Identity and access management.

## Technologies Used

- **Java**
- **Spring Boot**
- **Spring Cloud**
- **Spring Data JPA**
- **PostgreSQL**
- **Docker**
- **Kafka**
- **Keycloak**
- **JUnit & Mockito**: For unit and integration testing.
- **H2 Database**: For testing.

## Setup and Installation

### Prerequisites

- Docker and Docker Compose installed on your machine.
- Java 17 installed.
- Maven installed.

### Clone the Repository

```bash
git clone https://github.com/dungpham2502/Java-Ecommerce-Microservices.git
cd Java-Ecommerce-Microservices

### Technologies Used

