# College Database Management System

This is a Java Swing desktop application integrated with MongoDB, designed to manage college data such as students, faculty, courses, and departments. It features secure admin login, CRUD operations, and a tabbed dashboard for ease of use.

## Features

- **Admin Login:** Secure authentication using username and password stored in MongoDB.
- **Student Module:** Manage student records including roll number, name, department, semester, and email.
- **Faculty Module:** Manage faculty details like faculty ID, name, department, and subjects taught.
- **Course Module:** Add and manage courses with fields like course ID, name, semester, credits, and assigned faculty.
- **Department Module:** Manage various departments with unique codes and names.
- **Tabbed Interface:** Built using Java Swing's JTabbedPane for modular management.
- **MongoDB Backend:** Utilizes MongoDB Java Driver (4.11.0) for database connectivity and persistence.

## Prerequisites

- Java Development Kit (JDK) 17 or later installed.
- MongoDB installed and running locally on `mongodb://localhost:27017`.
- MongoDB Java Driver JAR files located in the `lib` folder:
  - `mongodb-driver-sync-4.11.0.jar`
  - `mongodb-driver-core-4.11.0.jar`
  - `bson-4.11.0.jar`

## Setup & Running Instructions

1. **Start MongoDB server**:
mongod
2. **Create an admin user** in MongoDB shell for login:
mongosh
use collegeDB
db.admins.insertOne({ username: "admin", password: "admin123" })
3. **Compile the source code** from the `src` directory:
cd path/to/collegeDBApp/src
javac -cp ".;..\lib\mongodb-driver-sync-4.11.0.jar;..\lib\mongodb-driver-core-4.11.0.jar;..\lib\bson-4.11.0.jar" main\CollegeLauncher.java ui*.java db*.java
4. **Run the application**:
java -cp ".;..\lib\mongodb-driver-sync-4.11.0.jar;..\lib\mongodb-driver-core-4.11.0.jar;..\lib\bson-4.11.0.jar" main.CollegeLauncher
5. **Login using admin credentials** (e.g., username: `admin`, password: `admin123`).

## Usage

- The application opens a login window first for security.
- After logging in, the main dashboard appears with tabs for Students, Faculty, Courses, and Departments.
- You can add, update, delete, and view data for each module.
- Data is stored safely in MongoDB collections corresponding to each entity.

## Notes

- Semester and other integer fields are handled with safe type conversion to avoid runtime errors.
- Passwords are stored in plaintext for demonstration; it is recommended to implement hashing for production use.
- SLF4J logging warnings in the console can be safely ignored or suppressed by adding SLF4J libraries.

## Folder Structure
collegeDBApp/
├── lib/ # MongoDB Java driver jars
├── src/
│ ├── main/ # Main launcher class
│ ├── ui/ # UI modules (StudentUI, FacultyUI, etc.)
│ └── db/ # MongoDB connection helper


---

This project demonstrates desktop GUI development with Java Swing alongside NoSQL MongoDB integration, ideal for academic institutions managing their data.

Feel free to contribute improvements or reach out if you have questions!

