# 🩺 eDokitaCare - Complete Study Guide

**Student:** Ishimwe Egide (26661)  
**Project:** Digital Healthcare Management System for Rwanda  
**Course:** Web Technology (2024/2025)

---

## 📖 Part 1: Project Overview

### Q1: What is eDokitaCare?

**Answer:**  
eDokitaCare is a **digital healthcare platform** that connects patients, doctors, and administrators in Rwanda. It helps patients book appointments online, find doctors by specialization, and access medical records digitally.

**Problems it solves:**
- ✅ Rural areas have limited healthcare access
- ✅ Long waiting times at hospitals
- ✅ Difficulty tracking medical records
- ✅ Poor doctor-patient communication

---

### Q2: What are the main features of your project?

**Answer:**

**For Patients:**
- Search doctors by specialization (cardiology, pediatrics, etc.)
- Book appointments online
- View appointment status (PENDING, APPROVED, COMPLETED)
- Access prescriptions and medical history

**For Doctors:**
- View appointment requests
- Approve or reject appointments
- Create digital prescriptions
- View patient medical history

**For Admins:**
- Manage all users (patients, doctors)
- Monitor all appointments
- Manage location data (provinces, districts, sectors)

---

### Q3: What activities happen in this system?

**Answer:**

**Registration Flow:**
1. User registers (chooses role: PATIENT or DOCTOR)
2. System creates user account
3. System creates patient/doctor profile
4. User can now login

**Appointment Flow:**
1. Patient searches for doctor by specialization
2. Patient books appointment (status: PENDING)
3. Doctor receives request
4. Doctor approves appointment (status: APPROVED)
5. Doctor completes appointment (status: COMPLETED)
6. Doctor creates prescription

**Medical Record Flow:**
1. Doctor examines patient
2. Doctor creates medical record (diagnosis, test results)
3. Record saved to patient's history
4. Patient can view anytime

---

## 🌱 Part 2: Core Technologies

### Q4: What is Spring Boot?

**Short Answer:**  
Spring Boot is a **Java framework** that makes building web applications **easy and fast**. It provides auto-configuration, embedded server (Tomcat), and ready-to-use features.

**Why use it?**
- ✅ No complex XML configuration
- ✅ Built-in server (no need to install Tomcat)
- ✅ Automatic setup of common features
- ✅ Production-ready (health checks, metrics)

**Role in eDokitaCare:**  
Builds the backend API that handles all requests (register user, book appointment, get doctors, etc.)

---

### Q5: What is JPA (Java Persistence API)?

**Short Answer:**  
JPA is a **specification** that tells Java how to **save objects to database** and **retrieve them back**. It converts Java objects ↔ Database tables automatically.

**Why use it?**
- ✅ No need to write SQL queries
- ✅ Java objects automatically become database tables
- ✅ Relationships handled automatically (One-to-One, One-to-Many)

**Example:**
```java
@Entity  // This becomes a table
public class Patient {
    @Id  // Primary key
    private Long patientId;
    
    private String fullName;  // Becomes a column
}
```

**Role in eDokitaCare:**  
Manages all 7 tables (User, Patient, Doctor, Appointment, Prescription, MedicalRecord, Location) without writing SQL.

---

### Q6: What is Hibernate?

**Short Answer:**  
Hibernate is the **implementation** of JPA. It's the actual tool that does the work.

**Relationship:**
- JPA = Interface (what to do)
- Hibernate = Implementation (how to do it)

**What it does:**
- Converts Java objects to SQL queries
- Creates tables automatically
- Handles relationships between tables
- Manages transactions

---

### Q7: What is Maven (MVN)?

**Short Answer:**  
Maven is a **build tool** and **dependency manager**. It downloads libraries your project needs and builds the application.

**Why use it?**
- ✅ Automatic library download (Spring Boot, PostgreSQL driver, etc.)
- ✅ Manages library versions
- ✅ Builds JAR file for deployment
- ✅ Runs tests

**File:** `pom.xml` - Lists all dependencies

**Example:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

---

### Q8: What dependencies did you use and their roles?

**Answer:**

| Dependency | Role |
|------------|------|
| **spring-boot-starter-web** | Creates REST API endpoints |
| **spring-boot-starter-data-jpa** | Database operations (CRUD) |
| **postgresql** | PostgreSQL database driver |
| **lombok** | Reduces boilerplate code (getters/setters) |
| **spring-boot-starter-validation** | Input validation (@NotNull, @Email) |
| **spring-boot-devtools** | Auto-restart during development |

---

## 🏗️ Part 3: Project Architecture & Layers

### Q9: What layers does your project have?

**Answer:**

**4-Layer Architecture:**

```
1. Controller Layer (@RestController)
   ↓ Receives HTTP requests
   
2. Service Layer (@Service)
   ↓ Business logic & validation
   
3. Repository Layer (@Repository)
   ↓ Database operations
   
4. Model/Entity Layer (@Entity)
   ↓ Database tables
```

**Flow Example (Book Appointment):**
```
User sends POST request
   ↓
AppointmentController receives it
   ↓
AppointmentService validates (patient exists? doctor exists?)
   ↓
AppointmentRepository saves to database
   ↓
Response sent back to user
```

---

### Q10: What is the role of each layer?

**Answer:**

**1. Model/Entity Layer** (`@Entity`)
- **Role:** Represents database tables as Java classes
- **Location:** `com.edokitacare.model` package
- **Example:** Patient.java, Doctor.java, Appointment.java

**What it does:**
```java
@Entity
@Table(name = "patient")
public class Patient {
    @Id
    private Long patientId;
    private String fullName;
    // This becomes a table with columns
}
```

---

**2. Repository Layer** (`@Repository`)
- **Role:** Talks to database (CRUD operations)
- **Location:** `com.edokitacare.repository` package
- **Example:** PatientRepository.java

**What it does:**
```java
public interface PatientRepository extends JpaRepository<Patient, Long> {
    // Automatically gets: save(), findById(), findAll(), delete()
    
    // Custom queries
    Patient findByUser_UserId(Long userId);
    List<Patient> findByGender(Gender gender);
}
```

**Provides:**
- `save()` - Create/Update
- `findById()` - Read by ID
- `findAll()` - Read all
- `delete()` - Delete
- Custom queries using method names

---

**3. Service Layer** (`@Service`)
- **Role:** Business logic, validation, coordination
- **Location:** `com.edokitacare.service` package
- **Example:** AppointmentService.java

**What it does:**
```java
@Service
public class AppointmentService {
    
    public Appointment bookAppointment(Appointment appointment) {
        // 1. Validate patient exists
        // 2. Validate doctor exists
        // 3. Set status to PENDING
        // 4. Save to database
        return appointmentRepository.save(appointment);
    }
}
```

**Responsibilities:**
- Validate input
- Apply business rules
- Coordinate between repositories
- Handle transactions

---

**4. Controller Layer** (`@RestController`)
- **Role:** Handles HTTP requests/responses
- **Location:** `com.edokitacare.controller` package
- **Example:** PatientController.java

**What it does:**
```java
@RestController
@RequestMapping("/api/patients")
public class PatientController {
    
    @GetMapping("/{id}")
    public Patient getPatient(@PathVariable Long id) {
        return patientService.findById(id);
    }
    
    @PostMapping
    public Patient createPatient(@RequestBody Patient patient) {
        return patientService.save(patient);
    }
}
```

**Handles:**
- GET requests (retrieve data)
- POST requests (create data)
- PUT requests (update data)
- DELETE requests (delete data)

---

### Q11: What is DTO and why use it?

**Short Answer:**  
DTO (Data Transfer Object) is a **simple object** that carries data between layers. It prevents sending too much or sensitive data to the user.

**Why use it?**
- ✅ Hides sensitive data (passwords, internal IDs)
- ✅ Reduces data sent (only send what's needed)
- ✅ Prevents circular references (Patient → Appointment → Patient loop)

**Example:**
```java
// Instead of sending full User object (with password!)
public class UserDTO {
    private Long userId;
    private String username;
    private String email;
    // No password field!
}
```

**When to use:**
- Login responses (don't send password back)
- API responses (only send needed fields)
- Forms (only accept certain fields)

---

## 🗄️ Part 4: Database Tables & Creation

### Q12: How do you create database tables?

**Answer:**

**Method 1: JPA Auto-Create (What we use)**

**Step 1:** Configure `application.properties`
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/edokitacare_db
spring.datasource.username=postgres
spring.datasource.password=yourpassword

# AUTO-CREATE TABLES
spring.jpa.hibernate.ddl-auto=update
```

**Step 2:** Create Entity class
```java
@Entity
@Table(name = "patient")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;
    
    @Column(nullable = false, length = 100)
    private String fullName;
}
```

**Step 3:** Run application - Table created automatically!

---

### Q13: How to check if tables were created?

**Answer:**

**Method 1: PostgreSQL (pgAdmin)**
1. Open pgAdmin
2. Navigate: Servers → PostgreSQL → Databases → edokitacare_db → Schemas → public → Tables
3. See all 7 tables: users, patient, doctor, appointment, prescription, medical_record, location

**Method 2: SQL Query**
```sql
SELECT table_name 
FROM information_schema.tables 
WHERE table_schema = 'public';
```

**Method 3: Spring Boot Console**
```properties
# Enable in application.properties
spring.jpa.show-sql=true
```
Console shows:
```sql
Hibernate: create table patient (patient_id bigint not null, full_name varchar(100), primary key (patient_id))
```

---

### Q14: Show example of table creation in code

**Answer:**

**Example: Patient Table**

```java
@Entity  // 1. Mark as database table
@Table(name = "patient")  // 2. Table name
public class Patient {
    
    // 3. Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;
    
    // 4. Regular columns
    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;
    
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    private Integer age;
    
    @Column(columnDefinition = "TEXT")
    private String medicalHistory;
    
    // 5. Foreign Key (Relationship)
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
    
    // 6. Reverse relationship
    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments;
}
```

**What Hibernate Creates:**
```sql
CREATE TABLE patient (
    patient_id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    gender VARCHAR(20),
    age INTEGER,
    medical_history TEXT,
    location_id BIGINT REFERENCES location(location_id)
);
```

---

## 🔗 Part 5: Database Relationships

### Q15: What relationships exist in eDokitaCare?

**Answer:**

**4 Types of Relationships:**

1. **One-to-One (1:1)**
   - User ↔ Patient
   - User ↔ Doctor

2. **One-to-Many (1:N)**
   - Patient → Appointments
   - Doctor → Appointments
   - Location → Users

3. **Many-to-One (N:1)**
   - Appointment → Patient
   - Appointment → Doctor

4. **Self-Referencing (1:N)**
   - Location → Location (parent-child)

---

### Q16: Explain One-to-One relationship with example

**Short Answer:**  
**One user account** has exactly **one patient profile** (or one doctor profile).

**Real Example:**
```
User: john_doe (userId=5)
  ↓ (has one patient profile)
Patient: Jean Mugabo (patientId=1, userId=5)
```

**Code Implementation:**

```java
// User Entity (Parent)
@Entity
public class User {
    @Id
    private Long userId;
    
    @OneToOne(mappedBy = "user")
    private Patient patient;  // One user has one patient
}

// Patient Entity (Child)
@Entity
public class Patient {
    @Id
    private Long patientId;
    
    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;  // One patient has one user
}
```

**Database:**
```
users table              patient table
├── user_id: 5          ├── patient_id: 1
├── username: john_doe  ├── user_id: 5 (FK)
                        └── full_name: Jean Mugabo
```

**Why unique = true?**  
Ensures one user can't have multiple patient profiles.

---

### Q17: Explain One-to-Many relationship with example

**Short Answer:**  
**One patient** can have **many appointments**.

**Real Example:**
```
Patient: Jean Mugabo (patientId=1)
  ↓ (has many appointments)
  ├── Appointment 1: Cardiology (appointmentId=10)
  ├── Appointment 2: Dentistry (appointmentId=11)
  └── Appointment 3: General (appointmentId=12)
```

**Code Implementation:**

```java
// Patient Entity (One side)
@Entity
public class Patient {
    @Id
    private Long patientId;
    
    @OneToMany(mappedBy = "patient")
    private List<Appointment> appointments;  // One patient, many appointments
}

// Appointment Entity (Many side)
@Entity
public class Appointment {
    @Id
    private Long appointmentId;
    
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;  // Many appointments, one patient
}
```

**Database:**
```
patient table            appointment table
├── patient_id: 1       ├── appointment_id: 10
└── full_name: Jean     ├── patient_id: 1 (FK)
                        ├── appointment_id: 11
                        ├── patient_id: 1 (FK)
                        ├── appointment_id: 12
                        └── patient_id: 1 (FK)
```

---

### Q18: Explain Self-Referencing relationship (Location)

**Short Answer:**  
A **location can have a parent location** and **child locations**. This creates a hierarchy.

**Real Example:**
```
Kigali (Province, locationId=1, parent=NULL)
  ↓
  ├── Gasabo (District, locationId=2, parent=1)
  │     ↓
  │     └── Remera (Sector, locationId=3, parent=2)
  │           ↓
  │           └── Rukiri (Cell, locationId=4, parent=3)
  │
  ├── Kicukiro (District, locationId=5, parent=1)
  └── Nyarugenge (District, locationId=6, parent=1)
```

**Code Implementation:**

```java
@Entity
public class Location {
    @Id
    private Long locationId;
    
    private String province;
    private String district;
    private String sector;
    
    @Enumerated(EnumType.STRING)
    private LocationType type;  // PROVINCE, DISTRICT, SECTOR, CELL, VILLAGE
    
    // Self-reference: Parent location
    @ManyToOne
    @JoinColumn(name = "parent_location_id")
    private Location parentLocation;
    
    // Self-reference: Child locations
    @OneToMany(mappedBy = "parentLocation")
    private List<Location> childLocations;
}
```

**Database:**
```
location table
├── location_id: 1 | province: Kigali    | type: PROVINCE | parent_location_id: NULL
├── location_id: 2 | district: Gasabo    | type: DISTRICT | parent_location_id: 1
├── location_id: 3 | sector: Remera      | type: SECTOR   | parent_location_id: 2
└── location_id: 4 | cell: Rukiri        | type: CELL     | parent_location_id: 3
```

**How it works:**
1. Province has `parent_location_id = NULL` (it's at the top)
2. District has `parent_location_id = 1` (parent is Kigali)
3. Sector has `parent_location_id = 2` (parent is Gasabo)
4. Each parent can have multiple children

---

### Q19: How does the location hierarchy flow work?

**Answer:**

**Flow: Creating Complete Hierarchy**

**Step 1: Create Province (Root)**
```json
POST /api/locations
{
  "province": "Kigali",
  "type": "PROVINCE",
  "code": "KGL"
}
```
Result: `location_id=1, parent_location_id=NULL`

**Step 2: Create District (Child of Province)**
```json
POST /api/locations
{
  "district": "Gasabo",
  "type": "DISTRICT",
  "code": "GAS",
  "parentLocationId": 1
}
```
Result: `location_id=2, parent_location_id=1`  
**Inherits:** province="Kigali" from parent

**Step 3: Create Sector (Child of District)**
```json
POST /api/locations
{
  "sector": "Remera",
  "type": "SECTOR",
  "parentLocationId": 2
}
```
Result: `location_id=3, parent_location_id=2`  
**Inherits:** province="Kigali", district="Gasabo" from parent

**Step 4: Create Cell (Child of Sector)**
```json
POST /api/locations
{
  "cell": "Rukiri",
  "type": "CELL",
  "parentLocationId": 3
}
```
Result: `location_id=4, parent_location_id=3`  
**Inherits:** province="Kigali", district="Gasabo", sector="Remera"

**Step 5: Create Village (Child of Cell)**
```json
POST /api/locations
{
  "village": "Amahoro",
  "type": "VILLAGE",
  "parentLocationId": 4
}
```
Result: `location_id=5, parent_location_id=4`  
**Inherits:** Full hierarchy from all parents

**Code Flow (Service Layer):**
```java
@Service
public class LocationService {
    
    public Location createLocation(Location location) {
        // If has parent, inherit fields
        if (location.getParentLocation() != null) {
            Location parent = locationRepository.findById(
                location.getParentLocation().getLocationId()
            ).orElseThrow();
            
            // Inherit from parent
            location.setProvince(parent.getProvince());
            location.setDistrict(parent.getDistrict());
            location.setSector(parent.getSector());
            location.setCell(parent.getCell());
        }
        
        return locationRepository.save(location);
    }
}
```

---

## 🔄 Part 6: Activity Flows

### Q20: How does user registration work? (Flow in words)

**Answer:**

**Actors:** User (Patient or Doctor)

**Flow:**

1. **User fills registration form**
   - Chooses username, email, password
   - Selects role (PATIENT or DOCTOR)
   - Clicks "Register"

2. **Request goes to Controller**
   - `POST /api/auth/register`
   - Controller receives JSON data
   - Calls UserService

3. **Service validates**
   - Check if username exists
   - Check if email exists
   - If duplicate → Error: "Username already exists"
   - If valid → Continue

4. **Service encrypts password**
   - Original: "MyPass123"
   - Encrypted: "$2a$10$xYz..."

5. **Service saves to database**
   - Creates User record
   - Generates user_id automatically

6. **Service creates profile**
   - If role=PATIENT → Create empty Patient profile
   - If role=DOCTOR → Create empty Doctor profile

7. **Response sent back**
   - Success: "User registered successfully"
   - User can now login

**Layer Communication:**
```
User Form
  ↓
Controller (receives data)
  ↓
Service (validates, encrypts password)
  ↓
Repository (saves to database)
  ↓
Database (stores user + profile)
```

---

### Q21: How does appointment booking work? (Flow in words)

**Answer:**

**Actors:** Patient, Doctor

**Flow:**

1. **Patient searches for doctor**
   - Enters specialization: "cardiology"
   - GET `/api/doctors/search?specialization=cardio`
   - System returns list of cardiologists

2. **Patient selects doctor**
   - Clicks "Book Appointment"
   - Fills form:
     - Date/Time: "2025-01-20 10:00 AM"
     - Illness: "Chest pain and shortness of breath"

3. **Request goes to Controller**
   - POST `/api/appointments`
   - JSON body with patientId, doctorId, dateTime, illnessDescription

4. **Service validates**
   - Check patient exists
   - Check doctor exists
   - Check if patient already has pending appointment
   - If validation fails → Error

5. **Service creates appointment**
   - Set status = PENDING
   - Save to database

6. **Doctor sees appointment request**
   - GET `/api/appointments/doctor/{doctorId}?status=PENDING`
   - List of pending appointments appears

7. **Doctor approves**
   - Clicks "Approve"
   - PUT `/api/appointments/{id}` with status=APPROVED

8. **Appointment day arrives**
   - Doctor examines patient
   - Clicks "Complete"
   - Status changes to COMPLETED

9. **Doctor creates prescription**
   - POST `/api/prescriptions`
   - Includes medicine, dosage, notes

10. **Patient views prescription**
    - GET `/api/prescriptions/patient/{patientId}`
    - Sees all prescriptions

**Layer Communication:**
```
Patient clicks "Book"
  ↓
AppointmentController.createAppointment()
  ↓
AppointmentService.bookAppointment()
  - Validates patient exists
  - Validates doctor exists
  - Sets status = PENDING
  ↓
AppointmentRepository.save()
  ↓
Database stores appointment
  ↓
Response: "Appointment booked successfully"
```

---

### Q22: Show appointment flow with code locations

**Answer:**

**Step-by-Step with Code:**

**1. Patient Request**
```
POST http://localhost:8080/api/appointments
Body: { "patientId": 1, "doctorId": 2, "illnessDescription": "Chest pain" }
```

**2. Controller receives request**
```java
// File: AppointmentController.java
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    
    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        // Receives JSON, converts to Appointment object
        Appointment created = appointmentService.bookAppointment(appointment);
        return ResponseEntity.ok(created);
    }
}
```

**3. Service validates and processes**
```java
// File: AppointmentService.java
@Service
public class AppointmentService {
    
    @Transactional
    public Appointment bookAppointment(Appointment appointment) {
        // Validate patient
        Patient patient = patientRepository.findById(appointment.getPatient().getPatientId())
            .orElseThrow(() -> new RuntimeException("Patient not found"));
        
        // Validate doctor
        Doctor doctor = doctorRepository.findById(appointment.getDoctor().getDoctorId())
            .orElseThrow(() -> new RuntimeException("Doctor not found"));
        
        // Set initial status
        appointment.setStatus(AppointmentStatus.PENDING);
        
        // Save
        return appointmentRepository.save(appointment);
    }
}
```

**4. Repository saves**
```java
// File: AppointmentRepository.java
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    // save() method provided automatically
}
```

**5. Database stores**
```sql
INSERT INTO appointment (patient_id, doctor_id, date_time, illness_description, status)
VALUES (1, 2, '2025-01-20 10:00:00', 'Chest pain', 'PENDING');
```

**6. Response back**
```json
{
  "appointmentId": 10,
  "patientId": 1,
  "doctorId": 2,
  "dateTime": "2025-01-20T10:00:00",
  "illnessDescription": "Chest pain",
  "status": "PENDING"
}
```

---

## 📊 Part 7: ER Diagram Explanation

### Q23: What is an ER Diagram?

**Short Answer:**  
ER (Entity-Relationship) Diagram shows **database tables**, their **columns**, and **relationships** visually.

**Components:**
- **Entities** (Rectangles): Tables (Patient, Doctor, Appointment)
- **Attributes** (Inside rectangles): Columns (patientId, fullName, age)
- **Relationships** (Lines connecting): Foreign keys (Patient → Appointments)
- **Cardinality** (1:1, 1:N, N:1): How many related records

---

### Q24: Describe the relationships in your ER diagram

**Answer:**

**Your ER Diagram has:**

**1. User ↔ Patient (1:1)**
```
User (1) ─────(1:1)───── Patient (1)
```
- One user account has one patient profile
- user_id in Patient table references User

**2. User ↔ Doctor (1:1)**
```
User (1) ─────(1:1)───── Doctor (1)
```
- One user account has one doctor profile
- user_id in Doctor table references User

**3. Patient → Appointments (1:N)**
```
Patient (1) ─────(1:N)───── Appointment (N)
```
- One patient has many appointments
- patient_id in Appointment references Patient

**4. Doctor → Appointments (1:N)**
```
Doctor (1) ─────(1:N)───── Appointment (N)
```
- One doctor has many appointments
- doctor_id in Appointment references Doctor

**5. Appointment → Prescriptions (1:N)**
```
Appointment (1) ─────(1:N)───── Prescription (N)
```
- One appointment can have multiple prescriptions
- appointment_id in Prescription references Appointment

**6. Location → Users/Patients/Doctors (1:N)**
```
Location (1) ─────(1:N)───── Patient (N)
Location (1) ─────(1:N)───── Doctor (N)
```
- One location has many patients/doctors
- location_id in Patient/Doctor references Location

**7. Location → Location (1:N Self-Reference)**
```
Location (1) ─────(1:N)───── Location (N)
```
- Province has many Districts
- District has many Sectors
- parent_location_id references location_id

---

### Q25: What tables are in your system?

**Answer:**

**7 Main Tables:**

1. **users** - Authentication (username, email, password, role)
2. **patient** - Patient profiles (fullName, age, bloodType, allergies)
3. **doctor** - Doctor profiles (specializations, experienceYears, clinicName)
4. **appointment** - Bookings (patientId, doctorId, dateTime, status)
5. **prescription** - Medicines (illness, medicineList, dosage, notes)
6. **medical_record** - History (diagnosis, testResults, notes)
7. **location** - Rwanda hierarchy (province, district, sector, cell, village)

---

### Q26: What enums do you have? Give examples.

**Answer:**

**5 Enums:**

**1. Role** (User types)
```
- ADMIN (system administrator)
- DOCTOR (healthcare provider)
- PATIENT (service seeker)
```

**Example:** User with role=PATIENT can book appointments but cannot approve them.

**2. Gender**
```
- MALE
- FEMALE
- OTHER
```

**Example:** Patient with gender=MALE

**3. BloodType**
```
- A_POSITIVE (A+)
- A_NEGATIVE (A-)
- B_POSITIVE (B+)
- B_NEGATIVE (B-)
- AB_POSITIVE (AB+)
- AB_NEGATIVE (AB-)
- O_POSITIVE (O+)
- O_NEGATIVE (O-)
```

**Example:** Patient with bloodType=O_POSITIVE can donate to many blood types.

**4. AppointmentStatus** (Workflow stages)
```
- PENDING (waiting for doctor approval)
- APPROVED (doctor accepted)
- COMPLETED (appointment finished)
- CANCELLED (patient cancelled)
- REJECTED (doctor declined)
```

**Example Flow:**
```
Patient books → PENDING
Doctor approves → APPROVED
Appointment done → COMPLETED
```

**5. LocationType** (Hierarchy levels)
```
- PROVINCE (top level: Kigali, Northern, etc.)
- DISTRICT (Gasabo, Kicukiro, etc.)
- SECTOR (Remera, Kimironko, etc.)
- CELL (smaller unit)
- VILLAGE (smallest unit)
```

**Example:** Location with type=DISTRICT is child of PROVINCE and parent of SECTOR.

---

## 🧪 Part 8: API Testing with Postman

### Q27: What is an API?

**Short Answer:**  
API (Application Programming Interface) is a **way for different software to talk to each other**. It's like a menu in a restaurant - you see options (endpoints) and order (send request).

**In eDokitaCare:**
- Frontend (React/Angular) sends request to API
- API processes request
- API sends back data (JSON)

**Example:**
```
Frontend: "Give me all doctors"
API: [Doctor 1, Doctor 2, Doctor 3]
```

---

### Q28: Why use APIs?

**Answer:**

**Benefits:**
- ✅ **Separation:** Frontend and backend are separate (can use React, Angular, Vue)
- ✅ **Reusability:** Same API works for web app, mobile app, desktop app
- ✅ **Standardization:** REST API is industry standard
- ✅ **Security:** Control who can access what data
- ✅ **Scalability:** Can handle many users

**Real Example:**
```
Mobile App (iOS) ──┐
Web App (React)  ──┼──→ Same API ──→ Database
Desktop App      ──┘
```

All three apps use the same backend API!

---

### Q29: What is Postman?

**Short Answer:**  
Postman is a **tool to test APIs**. You can send requests (GET, POST, PUT, DELETE) and see responses without building a frontend.

**Why use it?**
- ✅ Test APIs before building frontend
- ✅ Check if endpoints work correctly
- ✅ See response data
- ✅ Save requests for reuse
- ✅ Share collections with team

---

### Q30: How to set up Postman environment?

**Answer:**

**Step 1: Create Environment**
1. Open Postman
2. Click "Environments" (left sidebar)
3. Click "+" to create new environment
4. Name it: `eDokitaCare Dev`

**Step 2: Add Variable**
- Variable: `baseUrl`
- Initial Value: `http://localhost:8080`
- Current Value: `http://localhost:8080`
- Click "Save"

**Step 3: Select Environment**
- Top-right dropdown: Select "eDokitaCare Dev"

**Step 4: Use in Requests**
```
Instead of: http://localhost:8080/api/patients
Use: {{baseUrl}}/api/patients
```

**Benefits:**
- ✅ Change URL once (dev → production)
- ✅ All requests update automatically
- ✅ No need to edit each request

---

## 🎯 Part 9: Testing Endpoints

### Q31: How to get the endpoint URL in your code?

**Answer:**

**Look at Controller annotations:**

```java
@RestController
@RequestMapping("/api/patients")  // ← Base path
public class PatientController {
    
    @GetMapping  // ← Method path (empty = base path)
    public List<Patient> getAllPatients() {
        // Endpoint: GET http://localhost:8080/api/patients
    }
    
    @GetMapping("/{id}")  // ← Method path with parameter
    public Patient getById(@PathVariable Long id) {
        // Endpoint: GET http://localhost:8080/api/patients/5
    }
    
    @PostMapping  // ← POST to base path
    public Patient create(@RequestBody Patient patient) {
        // Endpoint: POST http://localhost:8080/api/patients
    }
    
    @GetMapping("/page")  // ← Custom path
    public Page<Patient> getPage() {
        // Endpoint: GET http://localhost:8080/api/patients/page
    }
}
```

**Formula:**
```
Endpoint = Base URL + @RequestMapping + @GetMapping/@PostMapping
         = http://localhost:8080 + /api/patients + /{id}
         = http://localhost:8080/api/patients/5
```

---

### Q32: How to get JSON body for POST/PUT requests?

**Answer:**

**Method 1: From Entity Class**

Look at your entity fields:

```java
@Entity
public class Patient {
    private Long patientId;      // Don't include in POST (auto-generated)
    private String fullName;     // ← Include
    private Gender gender;       // ← Include
    private Integer age;         // ← Include
    private String bloodType;    // ← Include
    private String contact;      // ← Include
}
```

**Create JSON from fields:**
```json
{
  "fullName": "Jean Mugabo",
  "gender": "MALE",
  "age": 28,
  "bloodType": "O_POSITIVE",
  "contact": "+250788123456"
}
```

**Method 2: From API Documentation**

Your API docs show request body examples for each endpoint.

**Method 3: Check Error Messages**

Send empty POST request → Error tells you required fields:
```json
{
  "error": "fullName is required",
  "field": "fullName"
}
```

---

### Q33: Test: Create a Patient (POST)

**Answer:**

**Step 1: Create Request in Postman**
- Click "New" → "HTTP Request"
- Method: `POST`
- URL: `{{baseUrl}}/api/patients`

**Step 2: Set Headers**
- Click "Headers" tab
- Add: `Content-Type: application/json`

**Step 3: Add Body**
- Click "Body" tab
- Select "raw"
- Select "JSON" from dropdown
- Paste JSON:

```json
{
  "user": {
    "username": "patient_bob",
    "email": "bob@example.com",
    "password": "SecurePass123",
    "role": "PATIENT"
  },
  "fullName": "Bob Fils",
  "gender": "MALE",
  "age": 30,
  "medicalHistory": "Asthma",
  "allergies": "Peanuts",
  "bloodType": "O_POSITIVE",
  "contact": "+250788123456"
}
```

**Where did this JSON come from?**

**From Controller:**
```java
@PostMapping
public Patient createPatient(@RequestBody Patient patient) {
    // @RequestBody expects Patient object as JSON
}
```

**From Patient Entity:**
```java
@Entity
public class Patient {
    // These fields become JSON properties
    private User user;
    private String fullName;
    private Gender gender;
    private Integer age;
    private String medicalHistory;
    private String allergies;
    private BloodType bloodType;
    private String contact;
}
```

**Step 4: Send Request**
- Click "Send"

**Expected Response:**
```json
{
  "patientId": 1,
  "fullName": "Bob Fils",
  "gender": "MALE",
  "age": 30,
  "medicalHistory": "Asthma",
  "allergies": "Peanuts",
  "bloodType": "O_POSITIVE",
  "contact": "+250788123456",
  "user": {
    "userId": 5,
    "username": "patient_bob",
    "email": "bob@example.com",
    "role": "PATIENT"
  }
}
```

**Status:** `200 OK` or `201 CREATED`

---

### Q34: Test: Get All Patients (GET)

**Answer:**

**Step 1: Create Request**
- Method: `GET`
- URL: `{{baseUrl}}/api/patients`

**Step 2: Send**
- Click "Send"
- No body needed for GET requests

**Expected Response:**
```json
[
  {
    "patientId": 1,
    "fullName": "Bob Fils",
    "gender": "MALE",
    "age": 30
  },
  {
    "patientId": 2,
    "fullName": "Marie Uwase",
    "gender": "FEMALE",
    "age": 25
  }
]
```

**Status:** `200 OK`

**Where is this endpoint?**
```java
@GetMapping  // GET /api/patients
public List<Patient> getAllPatients() {
    return patientService.findAll();
}
```

---

### Q35: Test: Get Patient by ID (GET with Path Variable)

**Answer:**

**Step 1: Create Request**
- Method: `GET`
- URL: `{{baseUrl}}/api/patients/1`  ← 1 is the patient ID

**Step 2: Send**
- Click "Send"

**Expected Response:**
```json
{
  "patientId": 1,
  "fullName": "Bob Fils",
  "gender": "MALE",
  "age": 30,
  "bloodType": "O_POSITIVE"
}
```

**Where is this endpoint?**
```java
@GetMapping("/{id}")  // {id} is path variable
public Patient getById(@PathVariable Long id) {
    // id = 1 from URL
    return patientService.findById(id);
}
```

**Try different IDs:**
- `{{baseUrl}}/api/patients/1` → Patient 1
- `{{baseUrl}}/api/patients/2` → Patient 2
- `{{baseUrl}}/api/patients/999` → 404 Not Found

---

### Q36: Test: Update Patient (PUT)

**Answer:**

**Step 1: Create Request**
- Method: `PUT`
- URL: `{{baseUrl}}/api/patients/1`  ← Update patient with ID 1

**Step 2: Set Headers**
- `Content-Type: application/json`

**Step 3: Add Body**
```json
{
  "fullName": "Bob Fils Updated",
  "age": 31,
  "contact": "+250788999999"
}
```

**Note:** Only include fields you want to update!

**Step 4: Send**

**Expected Response:**
```json
{
  "patientId": 1,
  "fullName": "Bob Fils Updated",
  "age": 31,
  "contact": "+250788999999"
}
```

**Where is this endpoint?**
```java
@PutMapping("/{id}")
public Patient updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
    // id = 1, patient = updated data
    return patientService.update(id, patient);
}
```

---

### Q37: Test: Delete Patient (DELETE)

**Answer:**

**Step 1: Create Request**
- Method: `DELETE`
- URL: `{{baseUrl}}/api/patients/1`

**Step 2: Send**
- No body needed

**Expected Response:**
- Status: `200 OK` or `204 No Content`
- Body: Empty or success message

**Where is this endpoint?**
```java
@DeleteMapping("/{id}")
public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
    patientService.deleteById(id);
    return ResponseEntity.noContent().build();
}
```

**Verify deletion:**
- Send GET `{{baseUrl}}/api/patients/1`
- Should get: `404 Not Found`

---

## 📄 Part 10: Pagination, Sorting & Filtering

### Q38: What is pagination and why use it?

**Short Answer:**  
Pagination divides large data into **pages** instead of loading everything at once.

**Example:**
```
Without Pagination:
- Query returns 10,000 patients
- Slow! Takes 5 seconds
- Too much data

With Pagination:
- Page 1: Patients 1-10
- Page 2: Patients 11-20
- Fast! Takes 0.1 seconds
- User-friendly
```

**Benefits:**
- ✅ Faster response
- ✅ Less memory usage
- ✅ Better user experience
- ✅ Reduced database load

---

### Q39: Test: Paginated Patients

**Answer:**

**Endpoint:** `GET {{baseUrl}}/api/patients/page`

**Query Parameters:**
- `page` - Page number (starts at 0)
- `size` - Items per page
- `sort` - Sort field

**Example 1: First page, 10 items**
```
GET {{baseUrl}}/api/patients/page?page=0&size=10
```

**Example 2: Second page, 5 items**
```
GET {{baseUrl}}/api/patients/page?page=1&size=5
```

**Response:**
```json
{
  "content": [
    { "patientId": 1, "fullName": "Bob Fils" },
    { "patientId": 2, "fullName": "Marie Uwase" }
  ],
  "totalElements": 45,     // Total patients in database
  "totalPages": 5,         // Total pages (45 ÷ 10 = 5)
  "number": 0,             // Current page number
  "size": 10,              // Items per page
  "first": true,           // Is this the first page?
  "last": false            // Is this the last page?
}
```

**Where is this endpoint?**
```java
@GetMapping("/page")
public Page<Patient> getPage(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size
) {
    Pageable pageable = PageRequest.of(page, size);
    return patientRepository.findAll(pageable);
}
```

---

### Q40: What is sorting and how to test it?

**Short Answer:**  
Sorting arranges data in order (A-Z, newest first, oldest first, etc.)

**Test Sorting:**

**Sort by name (ascending):**
```
GET {{baseUrl}}/api/patients/page?page=0&size=10&sort=fullName
```
Result: Alice, Bob, Charlie...

**Sort by name (descending):**
```
GET {{baseUrl}}/api/patients/page?page=0&size=10&sort=-fullName
```
Result: Zachary, Yvonne, Xavier...

**Sort by age (oldest first):**
```
GET {{baseUrl}}/api/patients/page?page=0&size=10&sort=-age
```
Result: 80 years, 75 years, 65 years...

**Multiple sorts (name, then age):**
```
GET {{baseUrl}}/api/patients/page?sort=fullName&sort=-age
```
Result: Sort by name first, then by age within same names

**Code:**
```java
Sort sort = Sort.by("fullName").ascending();
// or
Sort sort = Sort.by("age").descending();
```

---

### Q41: What is filtering and how to test it?

**Short Answer:**  
Filtering shows only data that matches certain conditions.

**Test Filtering:**

**Filter by gender:**
```
GET {{baseUrl}}/api/patients/page?gender=MALE&page=0&size=10
```
Result: Only male patients

**Filter by blood type:**
```
GET {{baseUrl}}/api/patients/page?bloodType=O_POSITIVE&page=0&size=10
```
Result: Only O+ patients

**Filter by name (search):**
```
GET {{baseUrl}}/api/patients/page?fullName=Jean&page=0&size=10
```
Result: Patients with "Jean" in their name

**Multiple filters:**
```
GET {{baseUrl}}/api/patients/page?gender=MALE&bloodType=O_POSITIVE&page=0&size=10
```
Result: Male patients with O+ blood

**Where in code?**

**Repository:**
```java
// Filtering by gender
Page<Patient> findByGender(Gender gender, Pageable pageable);

// Filtering by multiple conditions
Page<Patient> findByGenderAndBloodType(Gender gender, BloodType bloodType, Pageable pageable);
```

**Controller:**
```java
@GetMapping("/page")
public Page<Patient> getPage(
    @RequestParam(required = false) Gender gender,
    @RequestParam(required = false) BloodType bloodType,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size
) {
    // Apply filters if provided
}
```

---

### Q42: Combine pagination, sorting, and filtering

**Answer:**

**Complex Query:**
```
GET {{baseUrl}}/api/patients/page?gender=MALE&bloodType=O_POSITIVE&sort=fullName&sort=-age&page=0&size=10
```

**What this does:**
1. **Filter:** Only MALE patients with O_POSITIVE blood
2. **Sort:** By fullName (ascending), then age (descending)
3. **Paginate:** Show first 10 results

**Response:**
```json
{
  "content": [
    { "patientId": 5, "fullName": "Alex Smith", "age": 45, "gender": "MALE", "bloodType": "O_POSITIVE" },
    { "patientId": 12, "fullName": "Alex Johnson", "age": 32, "gender": "MALE", "bloodType": "O_POSITIVE" }
  ],
  "totalElements": 25,
  "totalPages": 3
}
```

---

## 🗂️ Part 11: PostgreSQL Database

### Q43: How to check your database in PostgreSQL?

**Answer:**

**Method 1: pgAdmin (GUI)**

**Step 1:** Open pgAdmin
**Step 2:** Navigate tree:
```
Servers
  └── PostgreSQL 15
       └── Databases
            └── edokitacare_db
                 └── Schemas
                      └── public
                           └── Tables
```

**Step 3:** Right-click on table → "View/Edit Data" → "All Rows"

**See:**
- users
- patient
- doctor
- appointment
- prescription
- medical_record
- location

---

**Method 2: SQL Query Tool**

**Step 1:** Right-click database → "Query Tool"

**Step 2:** Run queries:

```sql
-- View all patients
SELECT * FROM patient;

-- View all doctors
SELECT * FROM doctor;

-- View appointments with patient and doctor names
SELECT 
    a.appointment_id,
    p.full_name AS patient_name,
    d.full_name AS doctor_name,
    a.date_time,
    a.status
FROM appointment a
JOIN patient p ON a.patient_id = p.patient_id
JOIN doctor d ON a.doctor_id = d.doctor_id;

-- Count patients by gender
SELECT gender, COUNT(*) 
FROM patient 
GROUP BY gender;

-- View location hierarchy
SELECT 
    l.location_id,
    l.province,
    l.district,
    l.sector,
    l.type,
    l.parent_location_id
FROM location l
ORDER BY l.type, l.province, l.district;
```

---

### Q44: How to verify data was created?

**Answer:**

**After POST request in Postman:**

**Step 1:** Send POST request
```
POST {{baseUrl}}/api/patients
Body: { "fullName": "Test Patient", ... }
```

**Step 2:** Check response - note the ID
```json
{
  "patientId": 10,  // ← New patient ID
  "fullName": "Test Patient"
}
```

**Step 3:** Verify in PostgreSQL
```sql
SELECT * FROM patient WHERE patient_id = 10;
```

**Or verify with GET request:**
```
GET {{baseUrl}}/api/patients/10
```

**Should return the same patient data!**

---

## 📦 Part 12: Project Packages

### Q45: What packages does your project have?

**Answer:**

**Package Structure:**
```
com.edokitacare
├── model/          (Entities - database tables)
├── repository/     (Database access)
├── service/        (Business logic)
├── controller/     (REST API endpoints)
├── dto/            (Data Transfer Objects)
├── exception/      (Error handling)
├── config/         (Configuration classes)
└── EdokitaCareApplication.java (Main class)
```

---

### Q46: What is the role of each package?

**Answer:**

**1. model package** (`com.edokitacare.model`)

**Role:** Define database tables as Java classes

**Contains:**
- User.java
- Patient.java
- Doctor.java
- Appointment.java
- Prescription.java
- MedicalRecord.java
- Location.java

**What it does:**
- Marks classes with @Entity
- Defines table columns
- Defines relationships

**Example:**
```java
package com.edokitacare.model;

@Entity
public class Patient {
    @Id
    private Long patientId;
    private String fullName;
}
```

---

**2. repository package** (`com.edokitacare.repository`)

**Role:** Database access layer

**Contains:**
- UserRepository.java
- PatientRepository.java
- DoctorRepository.java
- AppointmentRepository.java
- PrescriptionRepository.java
- MedicalRecordRepository.java
- LocationRepository.java

**What it does:**
- Extends JpaRepository
- Provides CRUD methods
- Custom query methods (findBy, existsBy)

**Example:**
```java
package com.edokitacare.repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Patient findByUser_UserId(Long userId);
    List<Patient> findByGender(Gender gender);
}
```

---

**3. service package** (`com.edokitacare.service`)

**Role:** Business logic and validation

**Contains:**
- UserService.java
- PatientService.java
- DoctorService.java
- AppointmentService.java
- PrescriptionService.java
- MedicalRecordService.java
- LocationService.java

**What it does:**
- Validates input
- Applies business rules
- Coordinates repositories
- Handles transactions

**Example:**
```java
package com.edokitacare.service;

@Service
public class PatientService {
    
    @Autowired
    private PatientRepository patientRepository;
    
    public Patient save(Patient patient) {
        // Validation
        if (patient.getAge() < 0) {
            throw new RuntimeException("Age cannot be negative");
        }
        
        // Save
        return patientRepository.save(patient);
    }
}
```

---

**4. controller package** (`com.edokitacare.controller`)

**Role:** Handle HTTP requests/responses

**Contains:**
- UserController.java
- PatientController.java
- DoctorController.java
- AppointmentController.java
- PrescriptionController.java
- MedicalRecordController.java
- LocationController.java

**What it does:**
- Defines API endpoints
- Receives HTTP requests
- Calls service methods
- Returns JSON responses

**Example:**
```java
package com.edokitacare.controller;

@RestController
@RequestMapping("/api/patients")
public class PatientController {
    
    @Autowired
    private PatientService patientService;
    
    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatient(@PathVariable Long id) {
        Patient patient = patientService.findById(id);
        return ResponseEntity.ok(patient);
    }
}
```

---

**5. dto package** (`com.edokitacare.dto`)

**Role:** Data Transfer Objects (simplified data structures)

**Contains:**
- PatientDTO.java
- DoctorDTO.java
- AppointmentDTO.java

**What it does:**
- Transfers data between layers
- Hides sensitive information
- Prevents circular references

**Example:**
```java
package com.edokitacare.dto;

public class PatientDTO {
    private Long patientId;
    private String fullName;
    private String contact;
    // No password, no sensitive data
}
```

---

**6. exception package** (`com.edokitacare.exception`)

**Role:** Global error handling

**Contains:**
- GlobalExceptionHandler.java
- ResourceNotFoundException.java
- ValidationException.java

**What it does:**
- Catches exceptions
- Returns formatted error messages
- Provides consistent error responses

**Example:**
```java
package com.edokitacare.exception;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(404, ex.getMessage());
        return ResponseEntity.status(404).body(error);
    }
}
```

---

**7. config package** (`com.edokitacare.config`)

**Role:** Application configuration

**Contains:**
- SecurityConfig.java (if using Spring Security)
- CorsConfig.java (Cross-Origin Resource Sharing)

**What it does:**
- Configures security
- Enables CORS
- Customizes settings

---

### Q47: How do packages communicate?

**Answer:**

**Communication Flow:**

```
HTTP Request
    ↓
Controller (@RestController)
    ↓ calls
Service (@Service)
    ↓ calls
Repository (@Repository)
    ↓ queries
Database (PostgreSQL)
    ↓ returns data
Repository
    ↓ returns
Service
    ↓ returns
Controller
    ↓
HTTP Response
```

**Example - Get Patient by ID:**

**1. Request comes to Controller:**
```java
// PatientController.java
@GetMapping("/{id}")
public Patient getPatient(@PathVariable Long id) {
    return patientService.findById(id);  // Calls Service
}
```

**2. Service processes:**
```java
// PatientService.java
public Patient findById(Long id) {
    return patientRepository.findById(id)  // Calls Repository
        .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
}
```

**3. Repository queries database:**
```java
// PatientRepository.java (Interface - no implementation needed)
public interface PatientRepository extends JpaRepository<Patient, Long> {
    // findById() automatically provided
}
```

**4. Returns:** Database → Repository → Service → Controller → HTTP Response

---

## 🎓 Final Review Questions

### Q48: Explain the complete flow of registering a user in your system

**Short Answer:**

1. **User fills form** (username, email, password, role)
2. **POST /api/auth/register** → UserController
3. **Controller** calls UserService.register()
4. **Service validates:**
   - Check username exists? (existsByUsername)
   - Check email exists? (existsByEmail)
   - If exists → Error
5. **Service encrypts password** (BCrypt)
6. **Service saves** User to database
7. **Service creates profile:**
   - If PATIENT → Create Patient record
   - If DOCTOR → Create Doctor record
8. **Response:** "User registered successfully"

---

### Q49: Explain appointment workflow from booking to prescription

**Short Answer:**

**Step 1: Patient books appointment**
- POST /api/appointments
- Status: PENDING

**Step 2: Doctor sees request**
- GET /api/appointments/doctor/{doctorId}?status=PENDING

**Step 3: Doctor approves**
- PUT /api/appointments/{id}
- Status: PENDING → APPROVED

**Step 4: Appointment day**
- Doctor examines patient

**Step 5: Doctor completes**
- PUT /api/appointments/{id}
- Status: APPROVED → COMPLETED

**Step 6: Doctor creates prescription**
- POST /api/prescriptions
- Links to appointment

**Step 7: Patient views prescription**
- GET /api/prescriptions/patient/{patientId}

---

### Q50: What would you improve in this project?

**Answer:**

**1. Security:**
- ✅ Add JWT authentication (currently only basic auth)
- ✅ Implement role-based access control
- ✅ Encrypt sensitive medical data

**2. Features:**
- ✅ Email notifications for appointments
- ✅ File upload for medical documents
- ✅ Real-time chat between doctor and patient
- ✅ Payment integration

**3. Performance:**
- ✅ Add caching (Redis)
- ✅ Database indexing
- ✅ Optimize queries (avoid N+1 problem)

**4. Testing:**
- ✅ Add unit tests (JUnit)
- ✅ Add integration tests
- ✅ API documentation (Swagger)

---

## 📝 Quick Reference Summary

**Technologies Used:**
- Spring Boot (Backend framework)
- PostgreSQL (Database)
- JPA/Hibernate (ORM)
- Maven (Build tool)
- Postman (API testing)

**7 Main Tables:**
1. users (authentication)
2. patient (patient profiles)
3. doctor (doctor profiles)
4. appointment (bookings)
5. prescription (medicines)
6. medical_record (history)
7. location (Rwanda hierarchy)

**5 Enums:**
1. Role (ADMIN, DOCTOR, PATIENT)
2. Gender (MALE, FEMALE, OTHER)
3. BloodType (A+, A-, B+, B-, AB+, AB-, O+, O-)
4. AppointmentStatus (PENDING, APPROVED, COMPLETED, CANCELLED, REJECTED)
5. LocationType (PROVINCE, DISTRICT, SECTOR, CELL, VILLAGE)

**4 Layers:**
1. Controller (HTTP endpoints)
2. Service (Business logic)
3. Repository (Database access)
4. Model/Entity (Database tables)

**4 Relationships:**
1. One-to-One (User ↔ Patient/Doctor)
2. One-to-Many (Patient → Appointments)
3. Many-to-One (Appointment → Patient)
4. Self-Referencing (Location → Location)

---

**Good luck with your defense! 🎓**

You now understand:
- ✅ What your project does
- ✅ Technologies and their roles
- ✅ Database structure and relationships
- ✅ Code architecture and flow
- ✅ API testing with Postman
- ✅ How all parts work together
