# Transaction Spring Boot application!

Welcome to our Spring Boot application! This application serves made for test task

## Overview

It includes:

- A RESTful API endpoint for handling CRUD operations on entities.
- Integration with a MySql database for data persistence.
- Fetch currency data from https://api.twelvedata.com
- Swagger UI integration for API documentation.

## Prerequisites

Before running this application, ensure that you have the following installed:

- Java Development Kit (JDK) 8 or higher
- Maven build tool
- MySQL database server (optional, you can configure another database if needed)

## Getting Started

1. **Clone the repository:**

   ```bash
   git clone https://github.com/bolatdias/test.git
   ```

2. **Navigate to the project directory:**

   ```bash
   cd test
   ```

3. **Build the project:**

   ```bash
   mvn clean install
   ```

4. **Run the application:**

   ```bash
   java -jar target/demo-0.0.1-SNAPSHOT.jar
   ```

   The application will start running on `http://localhost:8080`.

## Configuration

You can configure the application settings by modifying the `application.properties` file located in the `src/main/resources` directory.

Example configuration:

```properties
spring.application.name=demo

## Server Properties
server.port= 8080

## Spring DATASOURCE (DataSourceAutoConfiguration & DataSourceProperties)
spring.datasource.url=jdbc:mysql://localhost:3306/bank?useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false
spring.datasource.username=root
spring.datasource.password=root

## Hibernate Properties
# The SQL dialect makes Hibernate generate better SQL for the chosen database
spring.jpa.hibernate.ddl-auto = none

spring.jpa.show-sql=true
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# Initialize the datasource with available DDL and DML scripts
spring.sql.init.mode=never
spring.jpa.defer-datasource-initialization= false

## App Properties
app.cors.allowedOrigins = http://localhost:3000
app.service.url=https://api.twelvedata.com
app.service.apiKey=


## Flyway
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true

```

## API Documentation

Swagger UI is integrated into this application for easy API documentation and testing. You can access the Swagger UI by navigating to `http://localhost:8080/swagger-ui/index.html` after starting the application.

