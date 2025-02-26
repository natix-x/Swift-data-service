# Swift-data-service
## Table of contents:
* [General info](#general-info)
* [Endpoints description](#endpoints-description)
* [Project structure](#project-structure)
* [Requirements](#requirements)
* [Setup](#setup)
### General info
The aim of this project is to create a Java Spring Boot application. The application processes SWIFT data from a provided CSV file, stores it in MySQL database and provides access to the data via RESTful endpoints. The project includes unit and integration tests, and all components are containerized using Docker, making them accessible at localhost:8080.
### Endpoints description
* Endpoint 1: Retrieve details of a single SWIFT code whether for a headquarters or branches.\
    **GET: /v1/swift-codes/{swift-code}:**\
    Response Structure for headquarter swift code:
    ```
    {
    "address": string,
    "bankName": string,
    "countryISO2": string,
    "countryName": string,
    "isHeadquarter": bool,
    "swiftCode": string
    “branches”: [
    {
    "address": string,
    "bankName": string,
    "countryISO2": string,
    "isHeadquarter": bool,
    "swiftCode": string
    },
    {
    "address": string,
    "bankName": string,
    "countryISO2": string,
    "isHeadquarter": bool,
    "swiftCode": string
    }, . . .
    ]
    }
    ```

    Response Structure for branch swift code: 
    ```
    {
    "address": string,
    "bankName": string,
    "countryISO2": string,
    "countryName": string,
    "isHeadquarter": bool,
    "swiftCode": string
    }
    ```
* Endpoint 2: Return all SWIFT codes with details for a specific country (both headquarters and branches).\
**GET:  /v1/swift-codes/country/{countryISO2code}:**\
Response Structure:
    ```
    {
    "countryISO2": string,
    "countryName": string,
    "swiftCodes": [
        {
            "address": string,
    		 "bankName": string,
    		 "countryISO2": string,
    		 "isHeadquarter": bool,
    		 "swiftCode": string
        },
        {
            "address": string,
    		 "bankName": string,
    		 "countryISO2": string,
    		 "isHeadquarter": bool,
    		 "swiftCode": string
        }, . . .
    ]
    }
    ```
* Endpoint 3: Adds new SWIFT code entries to the database for a specific country.\
**POST:  /v1/swift-codes/:**\
Request Structure:

    ```
    {
    "address": string,
    "bankName": string,
    "countryISO2": string,
    "countryName": string,
    “isHeadquarter”: bool,
    "swiftCode": string,
    }
    ```
    Response Structure:
    ```
    {
    "message": string,
    }
    ```
* Endpoint 4: Deletes swift-code data if swiftCode matches the one in the database.\
**DELETE:  /v1/swift-codes/{swift-code}:**\
Response Structure:
    ```
    {
    "message": string,
    }
    ```
### Project structure
### Requirements
1. For running the application:\
Before running the application make sure you have Docker Compose installed. If youencounter any issues during installation or setup process, refer to the [Docker Compuse documentation](#https://docs.docker.com/compose/install/) for guidane.\
2. For development & exploaration:\
- Java 21
- SpringBoot:
  - Spring Data JPA
  - Spring Web
  - MySQL Connector
- Gradle

### Setup
1. First, clone this repository.
   ```sh
   https://github.com/natix-x/Swift-data-service.git
   ```
2. Go to project directory.
   ```sh
   cd SwiftManager
   ```
3. Run the application with Docker Compose.
    ```sh
   docker-compose-up
   ```
    Once running, the endpoints are accessible at localhost:8080.
4. Stopping and cleaning up.
    To stop the application and remove containers:
    ```sh
   docker-compose down 
   ```
   To remove all containers and volumes:
    ```sh
   docker-compose down -v
   ```
