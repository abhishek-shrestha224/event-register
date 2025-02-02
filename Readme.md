# Event Register Backend

This is the backend for the Event Register system, built using Spring Boot.

## Features

-   Basic User authentication
-   Event creation and management
-   Badge generation
-   Secure API endpoints

## Installation Guide

### Prerequisites

Ensure you have the following installed:

-   Java 8
-   Maven
-   PostgreSQL (or another database if configured)
-   Git (optional)
-   Node js and pnpm

### Steps to Install and Run

1. **Clone the repository** :

    ```sh
    git clone <repository_url>

    ```

2. **Configure the database**:

    - navigate to backend

    ```sh
    cd event-register-backend
    ```

    - Open or `application.yml` in `src/main/resources`
    - Update the following environment variables:
        - DB_USER
        - DB_SECRET
        - DB_URL
        - MAIL_PORT
        - MAIL_USERNAME
        - MAIL_SERCRET

3. **Build and Run the Application**:

    ```sh
    mvn clean install
    mvn spring-boot:run
    ```

    The application will start on `http://localhost:8080`

4. **Setup the frontend code**:
    - Navigate to the frontend
    ```sh
     cd event-register-backend
    ```
    ```sh
    pnpm i
    pnpm run dev
    ```
    The frontend will start on `http://localhost:3000`

## API Documentation

-   The API endpoints are defined in the controllers (`/backend/src/main/java/world/hello/event_register/controller`)
