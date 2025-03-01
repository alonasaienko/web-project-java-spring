# Jacobi-Solver

## Description
A web-based calculator for solving systems of linear algebraic equations (SLAE). The application supports authentication, result history for authenticated users, and a tab system for organizing calculations.

## Features
- Solve SLAE with a user-friendly interface  
- Authentication system with JWT  
- Calculation history (available for authenticated users)  
- Tab-based navigation:
  - **Current results**
  - **Processing**
  - **History** (restricted for unauthenticated users)  

## Technologies Used
- **Frontend**: Vue.js (Vite)  
- **Backend**: Spring Boot  
- **Authentication**: JWT  
- **Database**: PostgreSQL  

## Installation

### Backend
1. Clone the repository:
   ```sh
   git clone https://github.com/alonasaienko/web-project-java-spring.git

2. Build and run the Spring Boot application:
  ```sh
  cd backend
  mvn clean install
  mvn spring-boot:run

