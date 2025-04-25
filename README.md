## Backend API for DTS Coding Challenge
This is the backend for a Case Manager app written for the DTS Coding Challenge. It manages the creation, retrieval, update, and deletion of Tasks.  

This is a RESTful HTTP API.
It was developed using TDD, with an MVC structure.

This application is written with Spring Boot, Java 21, an in memory H2 database, and Hibernate ORM for entity management. 
Tests are written with JUnit and Mockito.
Maven is used for dependency management.
Swagger API documentation is generated using springdoc-openapi.

### Requirements
Java 21

### Running locally
Make sure you have the requirements listed above on your machine.
Compile and run the Java application using IntelliJ or the command line.

### API Swagger documentation
This project uses springdoc-openapi to auto generate Swagger documentation.  
When the application is running locally open http://localhost:8080/swagger-ui/index.html to view Swagger API documentation.
