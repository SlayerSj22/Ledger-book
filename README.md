🚀 Built with Spring Boot + React
📊 SaaS-style ledger management system

# LedgerBook

LedgerBook is a simple SaaS-style ledger management application designed to help businesses track customer accounts, payments, and outstanding balances efficiently.

The application allows users to manage parties (customers), create accounts (bills), and maintain ledger entries for payments and purchases.

---

## 🚀 Features

### Authentication

* Secure login using JWT authentication
* Multi-user SaaS-style data isolation
* Each user only sees their own parties, accounts, and ledger entries

### Party Management

* Create and manage customers
* Search parties by name, father name, or village
* View customer profile and transaction history

### Account Management

* Create accounts (bills) for a customer
* Track total bill, credits, debits, and pending balance
* View open and closed accounts
* Pagination for large account lists

### Ledger Entries

* Add payments (credit)
* Add purchases or adjustments (debit)
* Edit ledger entries
* Delete ledger entries
* Automatic recalculation of pending balance

### Dashboard

* Visual dashboard with statistics
* Total pending amount
* Open and closed accounts
* Recent account activity

---

## 🧱 Tech Stack

### Backend

* Java
* Spring Boot
* Spring Security
* JWT Authentication
* Spring Data JPA
* Hibernate
* PostgreSQL
* Maven

### Frontend

* React
* React Router
* Axios
* Tailwind CSS
* React Toastify

---

## 📂 Project Structure

```
ledgerbook
│
├── backend
│   ├── controller
│   ├── service
│   ├── repository
│   ├── model
│   ├── dto
│   ├── security
│   └── exception
│
├── frontend
│   ├── components
│   ├── pages
│   ├── routes
│   ├── services
│   └── hooks
```

---

## 🔐 Default Admin Login

Use the following credentials to log in:

Email:

```
admin@ledger.com
```

Password:

```
admin123
```

---

## ⚙️ Running the Project

### Backend

1. Navigate to backend folder

```
cd backend
```

2. Run Spring Boot

```
mvn spring-boot:run
```

Server will start at:

```
http://localhost:8000
```

---

### Frontend

1. Navigate to frontend folder

```
cd frontend
```

2. Install dependencies

```
npm install
```

3. Run React app

```
npm run dev
```

Frontend runs at:

```
http://localhost:5173
```

---

## 📊 API Overview

### Authentication

```
POST /auth/login
```

---

### Party

```
POST   /party
GET    /party/{id}
GET    /party
GET    /party/search
DELETE /party/{id}
```

---

### Account

```
POST   /account
GET    /account/pending
GET    /account/{id}
GET    /account/{id}/status
GET    /account/party/{partyId}
DELETE /account/{id}
```

---

### Ledger

```
POST   /ledger/add
GET    /ledger/account/{accountId}
GET    /ledger/entry/{entryId}
PUT    /ledger/entry/{entryId}
DELETE /ledger/entry/{entryId}
```

---

## 🏗 SaaS Data Isolation

Data is isolated per user using this hierarchy:

```
User
 └── Party
      └── Account
           └── LedgerEntry
```

Queries filter data using the authenticated user's email extracted from JWT.

---

## 📈 Future Improvements

* Dashboard analytics charts
* Export ledger reports
* Multi-tenant subscription support
* Mobile responsive UI improvements
* Payment reminders

---

## © Copyright

Copyright © 2026 LedgerBook.

All Rights Reserved.

Unauthorized copying, modification, or distribution of this software is strictly prohibited without prior written permission.
