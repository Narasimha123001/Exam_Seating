📘 Exam Seating Management System
A complete Spring Boot based web application designed to automate and manage college examination seating arrangements with authentication, room allocation, and student seat lookup.

🧠 Introduction
Managing exam seating manually is error‑prone, time‑consuming, and stressful during real examinations.
This project provides a centralized system where admins can generate seating plans and students can instantly check their room and seat using their register number.

The system also integrates authentication and role‑based access control for Admin, Staff, and Students.

🎯 Objectives
Automate exam seating arrangement

Avoid adjacent same‑subject students (anti‑copy strategy)

Allow students to check seat instantly

Reduce manual workload for exam coordinators

Provide scalable architecture for thousands of students

Provide secure login using role‑based authorization

Maintain history and logs of seat allocation

❓ Why I Built This Project
In colleges, exam seating is usually done in Excel sheets or manually, which causes:

Human errors

Duplicate seating

Adjacent same department students

Last minute confusion

Heavy workload for faculty

So I built a system that:

Automatically distributes students

Follows anti‑malpractice seating rules

Is fast enough for thousands of lookups

Works like a real university system

💡 Solution Provided
This system solves the problem using:

Bucket‑based seating algorithm

Session wise shuffling

Bench distribution logic (L, M, R)

Anti‑adjacency checker

Fast lookup using caching approach

Role‑based authentication

🏗️ Architecture Overview
Modules
Auth Module (coe_auth)

Login & Registration

Roles & Permissions

JWT Security

Room Access Management

Exam Seating Module

Room Management

Student Session Mapping

Seat Allocation Engine

Seating History

Student Seat Lookup

🧮 Seating Allocation Approach
Case 1 — 3 or More Sessions
Students distributed alternately per bench:

Bench: [L, M, R]

L → DAA
M → ENG
R → DLD
Next bench repeats in rotation
If one session finishes → next session bucket replaces it.

Case 2 — 2 Sessions
Middle seat kept empty:

L → DAA
M → EMPTY
R → DLD
Case 3 — 1 Session
Two students per bench:

L → Student
M → EMPTY
R → Student
⚙️ Tech Stack
Backend
Java 21

Spring Boot 3

Spring Security (JWT)

Spring Data JPA

Hibernate

PostgreSQL / MySQL

Lombok

DevOps & Monitoring
Spring Actuator

Logging

Metrics endpoints

🔍 Features
Admin
Create rooms

Create exam sessions

Upload students

Generate seating plan

View seat history

Student
Login

Check seat using register number

View room & bench position

Security
JWT Authentication

Role based authorization

Protected APIs

📂 Project Structure
com.techtricks
│
├── auth.coe_auth
│   ├── controllers
│   ├── models
│   ├── security
│   ├── services
│
├── seating.Exam_Seating
│   ├── controllers
│   ├── services
│   ├── repositories
│   ├── models
│   ├── allocation (SeatAllocator)
│
└── ExamSeatingApplication.java
🚀 How To Run The Project
1️⃣ Clone Repo
git clone https://github.com/<your-username>/Exam_Seating.git
cd Exam_Seating
2️⃣ Configure Database
Edit:

src/main/resources/application.properties
Example:

spring.datasource.url=jdbc:mysql://localhost:3306/exam_seating
spring.datasource.username=root
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
3️⃣ Run Application
mvn spring-boot:run
Server runs at:

http://localhost:8080
📊 Actuator Endpoints
Endpoint	Purpose
/actuator/health	App status
/actuator/info	App info
/actuator/metrics	Performance
/actuator/env	Environment
/actuator/beans	Beans
/actuator/mappings	All APIs
/actuator/loggers	Change logs runtime
🔎 Student Seat Lookup Flow
Student enters Register Number

System checks cache / DB

Returns:

Room Number
Bench Number
Seat Position (L/M/R)
Session
🧠 Core Algorithms Used
Round Robin Distribution

Bucket Allocation

Anti‑adjacent placement

Rotation Checker

Adjacency Validator

🤝 Contributing
If you want to improve this project:

Fork the repository

Create a feature branch

Commit changes

Create Pull Request

Suggestions welcome:

Performance optimization

UI integration

Microservices conversion

AI‑based cheating detection

📌 Future Improvements
Redis caching for high load

Microservice architecture

Hall ticket generation

QR code seat scanning

Mobile application

Analytics dashboard

👨‍💻 Author
Narasimha Anguluri
College Examination Automation Project

⭐ Support
If this project helped you, consider giving a ⭐ on GitHub!