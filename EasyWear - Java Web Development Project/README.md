# Sales Reporting and Management Application

## Overview
This application is a web-based sales management and reporting tool designed for **EasyWear**'s retail network.
It provides functionality to manage sales transactions, generate reports, and interact with a remote SQL database. The application is accessible both locally (via VPN) and through a public internet link.

## Project Repository
[GitHub Repository](https://github.com/NoaLugashi/EasyWear-Store-Manager)

## Students
1. **Noa Lugashi**
2. **Ofir Itzkovich**
3. **Orichai Attias**
4. **Shaked Sabag**

## Application Details
- **JAR Location**: `EasyWear - Java Web Development Project \ Spring \ demo \ target`
- **Codebase**: Java classes, interfaces, and core logic are stored in `Spring\demo\src\main\java\com\example\demo`.
- **Resources**: HTML, JavaScript, and static resources are located in `Spring\demo\src\main\resources`.
- **Database**: The application connects to a remote SQL Server that is not accessible via the internet.
- **Local Mode Features**: Logs and chat menu options work only when running locally and will be presented during the project demonstration.

## Users

### Admin User
- **Username**: `roy_admin`
- **Capabilities**: Full access to all system features.

### Employee User
- **Username**: `roy`
- **Capabilities**: Limited to employee-level functionalities.

### Shared Password
- **Password for both users**: `Roy123!`

## Deployment Instructions

### Prerequisites
1. **Java**: JDK 17 or higher.
2. **Spring Boot**: Ensure dependencies are configured via Maven.
3. **SQL Server**: Valid remote database credentials.

### Public Deployment
- The application is accessible at: [https://f43f-185-164-16-241.ngrok-free.app/](https://f43f-185-164-16-241.ngrok-free.app/).

## Usage
1. Clone the repository:
   ```sh
   git clone https://github.com/shaked6133/HIT-Web-Dev-Java.git
   cd HIT-Web-Dev-Java
   ```
2. Build the project:
   ```sh
   mvn clean install
   ```
3. Run the application:
   ```sh
   mvn spring-boot:run
   ```

## Contribution Guidelines
- Fork the repository and create a feature branch.
- Submit a pull request with a detailed description of changes.
- Follow the project's code style guidelines.

## License
This project is licensed under the MIT License. See `LICENSE` for details.
