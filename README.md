# CPIT305 – Integrated HR Management System

A collaborative Java desktop application developed for CPIT305 to manage core HR operations through an integrated GUI, database, networking, and multithreading architecture.

## Main Features
- Employee management
- Contract management
- Leave request management
- Login and registration
- HR dashboard and reports
- MySQL database integration
- TCP client/server networking
- Multithreaded server handling
- Activity logging

## Technical Components
- **Java / OOP**
- **Java Swing GUI**
- **MySQL**
- **JDBC**
- **TCP Sockets**
- **Multithreading**
- **NetBeans / Ant**

## Project Structure
- `src/gui` – desktop user interfaces
- `src/database` – database connection and automatic database/table setup
- `src/network` – TCP client/server communication and multithreaded request handling
- `src/utils` – logging utilities
- `lib` – required Java libraries

## Database
The application uses a local MySQL database named `hr_system`.
`DBSetup.java` creates the required database and tables when the application starts.

Before running, update the local MySQL username/password in:
- `src/database/DBConnection.java`
- `src/database/DBSetup.java`

## Networking & Multithreading
`HRServer` runs a TCP server on port `5000`. Each client connection is handled by a separate `ClientHandler` thread. `HRClient` sends application notifications to the server and receives acknowledgements.

## How to Run
1. Install Java and MySQL.
2. Open the project in NetBeans.
3. Configure the MySQL credentials in `DBConnection.java` and `DBSetup.java`.
4. Run `network.HRServer` if you want networking notifications enabled.
5. Run the main application (`gui.GUI`).

## Team Project
This repository represents the integrated final version of a collaborative academic project.
