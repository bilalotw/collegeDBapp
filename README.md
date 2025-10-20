@'
# College Database Management System (Java Swing + MongoDB)

A desktop application built with Java Swing and MongoDB for managing Students, Faculty, Courses, and Departments. It includes a secure Admin Login and a tabbed dashboard for intuitive navigation.

---

## Features

- Admin Login with credentials stored in MongoDB.
- Student Module: CRUD for roll, name, department, semester, email.
- Faculty Module: CRUD for faculty ID, name, department, subject.
- Course Module: CRUD for course ID, name, semester, credits, assigned faculty.
- Department Module: CRUD for department code and name.
- Tabbed Dashboard UI using Swing JTabbedPane.
- MongoDB backend with Java Driver 4.11.x.

---

## Tech Stack

- Language: Java (JDK 17+)
- GUI: Java Swing
- Database: MongoDB (localhost:27017)
- Driver JARs (place in ./lib):
  - mongodb-driver-sync-4.11.0.jar
  - mongodb-driver-core-4.11.0.jar
  - bson-4.11.0.jar

---

## Project Structure

collegeDBApp/
├─ lib/ # MongoDB driver jars
│ ├─ mongodb-driver-sync-4.11.0.jar
│ ├─ mongodb-driver-core-4.11.0.jar
│ └─ bson-4.11.0.jar
└─ src/
├─ db/
│ └─ DBConnection.java
├─ ui/
│ ├─ LoginUI.java
│ ├─ MainDashboard.java
│ ├─ StudentUI.java
│ ├─ FacultyUI.java
│ ├─ CourseUI.java
│ └─ DepartmentUI.java
└─ main/
└─ CollegeLauncher.java

text

---

## Setup

1. Install JDK 17+ and MongoDB Community Server.
2. Ensure MongoDB is running:
```
mongod
```
text
3. Place the MongoDB driver jars in the `lib` directory as shown above.
4. Seed an admin user:
```
mongosh
use collegeDB
db.admins.insertOne({ username: "admin", password: "admin123" })
```
text

---

## Build and Run (Windows CMD/PowerShell)

From the `src` directory:

Compile:
```
javac -cp ".;..\lib\mongodb-driver-sync-4.11.0.jar;..\lib\mongodb-driver-core-4.11.0.jar;..\lib\bson-4.11.0.jar" main\CollegeLauncher.java ui*.java db*.java
```
text

Run:
```
java -cp ".;..\lib\mongodb-driver-sync-4.11.0.jar;..\lib\mongodb-driver-core-4.11.0.jar;..\lib\bson-4.11.0.jar" main.CollegeLauncher
```
text

Notes:
- On Linux/macOS replace `;` with `:` in the classpath.
- If you see an SLF4J warning, it’s safe to ignore or add SLF4J jars for logging.

---

## Module Details

### Admin Login
- Collection: `admins`
- Fields: `username` (string), `password` (string; demo uses plain text)
- On success: closes LoginUI and opens MainDashboard.

### Students
- Collection: `students`
- Fields: `roll` (string), `name` (string), `dept` (string), `semester` (int/string), `email` (string)
- UI handles numeric/string semester safely by converting to string before display.

### Faculty
- Collection: `faculty`
- Fields: `id`, `name`, `dept`, `subject`

### Courses
- Collection: `courses`
- Fields: `id`, `name`, `semester`, `credits`, `faculty_id`

### Departments
- Collection: `departments`
- Fields: `code`, `name`

---

## Troubleshooting

- ClassCastException for semester:
  - Fixed by reading `Object sem = doc.get("semester")` and using `String.valueOf(sem)`.
- “Class not found” when running:
  - Verify package names match folder paths and classpath includes `lib` jars.
- Mongo connection errors:
  - Ensure `mongod` is running and URL is `mongodb://localhost:27017`.
- Window closes after login:
  - Typically caused by an exception in UI constructors; run from terminal and fix the reported line (often type casting in table model).

---

## Security Notes

- Demo stores plaintext passwords; use a secure hash (e.g., BCrypt) for real deployments.
- Consider role-based access and field validation for production.

---

## Roadmap (Optional Enhancements)

- Hash passwords and add user management UI.
- Add search across modules and export to CSV/PDF.
- Add pagination and indexes for large collections.
- Add logging via SLF4J + Logback.

---

## License

This project is provided for educational purposes. Add your preferred license if you plan to distribute.
'@ | Set-Content -NoNewline -Encoding UTF8 README.md
