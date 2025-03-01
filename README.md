# Swift-data-service
## Table of contents:
* [General info](#general-info)
* [Project structure](#project-structure)
* [Endpoints description](#endpoints-description)
* [Requirements](#requirements)
* [Setup](#setup)
* [Tests](#tests)
### General info
The aim of this project is to create a Java Spring Boot application. The application processes SWIFT data from a provided CSV file, stores it in MySQL database and provides access to the data via RESTful endpoints. The project includes unit and integration tests, and all components are containerized using Docker, making them accessible at localhost:8080.
### Project structure

    com.bankdata.swiftmanager/
    ├── controller/          # Contains controller classes that handle HTTP requests and return responses.
    ├── dto/                 # Data Transfer Objects - classes used to transfer data between application layers.
    ├── exception/           # Exception handling, including custom classes for managing application errors.
    ├── model/               # Data model classes that represent the structure of database tables.
    ├── repository/          # Repositories for interacting with the database.
    ├── response/            # Response structures returned by the API.
    ├── service/             # Business logic of the application, implementation of services.
    ├── util/                # Utility classes and methods (DataLoader, CSVParser).
    ├── SwiftManagerApplication  # Main application class that runs the Spring Boot application.

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
If any errors occur while accessing the endpoints, the following response structure is used:\
```
{
"errors": list,
"message": string
}
```
### Used technologies
- Java 21
- SpringBoot:
  - Spring Data JPA - for implementation of JPA-based repositories,
  - Spring Web - for building REST API,
- MySQL Connector - for connecting app to MySQL db;
- OpenCSV - for parsing data from CSV to MySQL db;
- Gradle - build tool;
- Lombock - java library that simplifies code by generating boilerplate code like getters, setters, constructors;
### Requirements
Before running the application make sure you have Docker Compose installed. If you encounter any issues during installation or setup process, refer to the [Docker Compose documentation](https://docs.docker.com/compose/install/) for guidance.
### Setup
1. First, clone this repository.
   ```sh
   git clone https://github.com/natix-x/Swift-data-service.git
   ```
2. Go to project directory.
   ```sh
   cd SwiftManager
   ```
3. Run the application with Docker Compose.
    ```sh
   docker compose up
   ```
    Once running, the endpoints are accessible at localhost:8080.
4. Stopping and cleaning up.\
    To stop the application and remove containers:
    ```sh
   docker compose down 
   ```
   To remove all containers and volumes:
    ```sh
   docker compose down -v
   ```
### Tests
For testing below tools were used:
- JUnit & SpringBoot Test - for unit & integration tests, ensuring individual components and their interactions work as expected;
- H2 Database - an in-memory database for testing purposes;
- Postman - for testing and validating the API endpoints, ensuring proper request/response handling
- Test Containers - for providing lightweight, throwaway instance of MySQL database, enabling realistic integration tests with containerized environments

If you want to run tests make sure that you have Java 21 and gradle installed. For running integration tests you also have to have docker installed and running. Then go to project directory (SwiftManager) and run the following commands to execute:
- Unit tests:
```sh
# Linux/macOS  
./gradlew test  

# Windows  
gradlew test 
```
- Integration tests:
```sh
# Linux/macOS  
./gradlew integrationTest  

# Windows  
gradlew integrationTest 
```
- All tests:
```sh
# Linux/macOS  
./gradlew check  

# Windows  
gradlew check  
```
Test reports will be available in the build/reports/ directory.

You can also easily test the API endpoints by running a predefined collection in Postman. Remember to firstly run the application. Then follow these steps:
1. Download the Postman Collection:
    - Get the predefined collection from the [file](postman/SWIFTManagerAPI.postman_collection.json).
2. Import the Collection into Postman:
    - Open Postman.
    - Click on the "Import" button.
    - Choose downloaded file to import it.
For detailed instructions on how to import data into postman, refer to [Postman Importating Guide](https://learning.postman.com/docs/getting-started/importing-and-exporting/importing-data/).
