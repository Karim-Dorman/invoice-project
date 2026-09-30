# Invoice Application

A full-stack invoice management application built with Angular and Spring Boot.

## Application Flow

1. A new user registers using Firebase Authentication.
2. After registration, the user is redirected to the Profile page.
3. The user completes company, client, and billing information.
4. Returning users with an existing profile are redirected to the Invoice Dashboard after login.
5. The user enters the billing period, hours worked, and description.
6. The application calculates the invoice amount.
7. The backend generates a PDF invoice and emails it to the client.
8. Previously created invoices appear in the Invoice History section.

## Features

- User registration and login with Firebase Authentication
- Firebase ID-token validation in the Spring Boot backend
- Protected Angular routes and backend endpoints
- Employee, company, client, and billing profile
- Automatic redirection for first-time users
- Invoice creation and updating
- Automatic invoice amount calculation
- PDF invoice generation
- Email delivery with a PDF attachment
- Invoice history and sent status

## Technologies

### Frontend

- Angular
- TypeScript
- Reactive Forms
- Firebase Authentication
- HTTP interceptor for Firebase tokens
- Bootstrap/CSS

### Backend

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL
- Firebase Admin SDK
- JavaMail
- OpenHTMLtoPDF

## Project Structure

- `invoice-frontend` – Angular frontend
- `invoice-backend` – Spring Boot backend

## Security

Private credentials are not included in this project.

The following values must be configured before running the application:

- Firebase frontend configuration
- Firebase service-account JSON file
- MySQL username and password
- Email address and Google App Password

## Running the Backend

1. Create the MySQL database:

```sql
CREATE DATABASE invoice_db;
```

2. Configure the database and email values in:

```text
invoice-backend/src/main/resources/application.properties
```

3. Add your Firebase service-account file to:

```text
invoice-backend/src/main/resources/firebase-service-account.json
```

4. Start the Spring Boot application. It runs at:

```text
http://localhost:8081
```

## Running the Frontend

1. Add your Firebase configuration to:

```text
invoice-frontend/src/app/app.config.ts
```

2. Open a terminal inside `invoice-frontend` and run:

```bash
npm install
ng serve
```

3. Open the application at:

```text
http://localhost:4200
```