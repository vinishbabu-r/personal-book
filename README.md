
# 📚 Book Service – Spring Boot API

A simple Spring Boot application that allows adding books to a local database using a Google Books ID. The application retrieves book metadata from the Google Books API and stores it in a database.

---

## 🚀 Getting Started

### **Prerequisites**
Make sure you have the following installed:

- **Java 17+**
- **Maven 3.8+**
- **Git**
- Any SQL database supported by Spring Boot (H2/MySQL/PostgreSQL)

---

## ▶️ Running the Application

### **1. Clone the repository**
```bash
git clone https://github.com/vinishbabu-r/personal-book.git
cd personal-book
```

### **2. Build the project**
```bash
mvn clean install
```

### **3. Run the application**
```bash
mvn spring-boot:run
```

The service will start at:
```
http://localhost:8080
```

---

## 📘 REST API Documentation

### **Add Book by Google Book ID**

**POST**
```
/books/{googleId}
```

### ✔️ Example Request
```
POST http://localhost:8080/books/piOyzYqeZGgC
```

### ✔️ Example Response (201 Created)
```json
{
  "id": "zyTCAlFPjgYC",
  "volumeInfo": {
    "title": "Effective Java",
    "author": "Joshua Bloch",
    "pageCount": 416
  }
}
```

### 📍 Response Headers
```
Location: /books
```

---

## 🧪 Running Tests

### Run all tests:
```bash
mvn test
```

### Run a specific test class:
```bash
mvn -Dtest=BookServiceTest test
```


## 🛠 Technologies Used

- **Spring Boot 3**
- **Spring Web**
- **Spring Data JPA**
- **H2**
- **Lombok (optional)**
- **JUnit 5**
- **Mockito**
- **MockMvc**

---
