# CS4350_Hospital
 You have been contracted to develop the database system that will manage the primary care operation of the hospital, but not the financial operation.  The database system must have an easy to use interface that supports all the data entry and information gathering needs of the hospital.

This is a hospital database management system developed in Java and MySQL. It is designed to manage patient information, room assignments, employee records, diagnoses, treatments, and admissions in a hospital.

## Prerequisites
Before running the project, ensure you have the following installed:
- **Java Development Kit (JDK)** version 8 or above
- **MySQL Server** (installed locally or accessible remotely)
- **MySQL Workbench** (optional, for database management)
- **JDBC Driver** for MySQL

## Setup Instructions

### 1. Set up MySQL Database
1. Install MySQL Server (if not already installed) verion 8.3 or below.
2. Install MySQL Connector/J verion 8.3.0 or lower (8.0)
3. If Mac, install MySQL Community verion 8.0 or below 
4. Create a database `myDB` and use the SQL scripts provided to create the necessary tables and insert data.
5. FOR VSCODE ONLY: Create a java project and in Referenced Libraries click '+' and connect the jar file downloaded in MySQLConnector/J
6. Paste App.java code and run the program!


USE myDB;
CREATE TABLE Patient (
    PatientID INT PRIMARY KEY,
    FirstName VARCHAR(50),
    LastName VARCHAR(50),
    EmergencyContact VARCHAR(100),
    InsurancePolicy VARCHAR(100)
);

CREATE TABLE Employee (
    EmployeeID INT PRIMARY KEY,
    FirstName VARCHAR(50),
    LastName VARCHAR(50),
    JobCategory VARCHAR(50)
);

CREATE TABLE Room (
    RoomNumber INT PRIMARY KEY,
    Occupied BOOLEAN DEFAULT FALSE
);

CREATE TABLE Diagnosis (
    DiagnosisID INT PRIMARY KEY,
    Description VARCHAR(255)
);

CREATE TABLE Treatment (
    TreatmentID INT PRIMARY KEY,
    Type VARCHAR(50),
    OrderTimestamp DATETIME,
    AdminTimestamp DATETIME
);

CREATE TABLE Admission (
    AdmissionID INT PRIMARY KEY,
    PatientID INT,
    RoomNumber INT,
    PrimaryDoctorID INT,
    DiagnosisID INT,
    AdmissionDate DATETIME,
    DischargeDate DATETIME,
    FOREIGN KEY (PatientID) REFERENCES Patient(PatientID),
    FOREIGN KEY (RoomNumber) REFERENCES Room(RoomNumber),
    FOREIGN KEY (PrimaryDoctorID) REFERENCES Employee(EmployeeID),
    FOREIGN KEY (DiagnosisID) REFERENCES Diagnosis(DiagnosisID)
);

-- INSERT INTO Statements
INSERT INTO Patient (PatientID, FirstName, LastName, EmergencyContact, InsurancePolicy)
VALUES 
    (1, 'John', 'Doe', 'Jane Doe', 'Policy123'),
    (2, 'Mary', 'Smith', 'Paul Smith', 'Policy456');

INSERT INTO Room (RoomNumber, Occupied)
VALUES 
    (1, FALSE), 
    (2, TRUE), 
    (3, FALSE);

INSERT INTO Employee (EmployeeID, FirstName, LastName, JobCategory)
VALUES 
    (101, 'Alice', 'Smith', 'Doctor'),
    (102, 'Bob', 'Johnson', 'Nurse'),
    (103, 'Carol', 'Williams', 'Technician');

INSERT INTO Diagnosis (DiagnosisID, Description)
VALUES 
    (1, 'Flu'),
    (2, 'Pneumonia');

INSERT INTO Admission (AdmissionID, PatientID, RoomNumber, PrimaryDoctorID, DiagnosisID, AdmissionDate, DischargeDate)
VALUES 
    (1, 1, 2, 101, 1, '2025-01-01', NULL);



-----SQL COMMANDS(NOT NEEDED)------
-- Step 3: SQL Queries

-- Room Utilization Queries
-- 1.1 List the rooms that are occupied, along with the associated patient names and the date the patient was admitted.
SELECT Room.RoomNumber, Patient.FirstName, Patient.LastName, Admission.AdmissionDate
FROM Room
JOIN Admission ON Room.RoomNumber = Admission.RoomNumber
JOIN Patient ON Admission.PatientID = Patient.PatientID
WHERE Room.Occupied = TRUE;

-- 1.2 List the rooms that are currently unoccupied.
SELECT RoomNumber
FROM Room
WHERE Occupied = FALSE;

-- 1.3 List all rooms in the hospital along with patient names and admission dates for those that are occupied.
SELECT Room.RoomNumber, Patient.FirstName, Patient.LastName, Admission.AdmissionDate
FROM Room
LEFT JOIN Admission ON Room.RoomNumber = Admission.RoomNumber
LEFT JOIN Patient ON Admission.PatientID = Patient.PatientID;

-- Patient Information Queries
-- 2.1 List all patients in the database, with full personal information.
SELECT * FROM Patient;

-- 2.2 List all patients currently admitted to the hospital. List only patient identification number and name.
SELECT Patient.PatientID, Patient.FirstName, Patient.LastName
FROM Admission
JOIN Patient ON Admission.PatientID = Patient.PatientID
WHERE Admission.DischargeDate IS NULL;

-- 2.3 List all patients who were discharged in a given date range. List only patient identification number and name.
SELECT Patient.PatientID, Patient.FirstName, Patient.LastName
FROM Admission
JOIN Patient ON Admission.PatientID = Patient.PatientID
WHERE Admission.DischargeDate BETWEEN '2025-01-01' AND '2025-12-31';

-- Diagnosis and Treatment Information Queries
-- 3.1 List the diagnoses given to patients, in descending order of occurrences. List diagnosis identification number, name, and total occurrences of each diagnosis.
SELECT Diagnosis.DiagnosisID, Diagnosis.Description, COUNT(*) AS TotalOccurrences
FROM Admission
JOIN Diagnosis ON Admission.DiagnosisID = Diagnosis.DiagnosisID
GROUP BY Diagnosis.DiagnosisID, Diagnosis.Description
ORDER BY TotalOccurrences DESC;

-- Employee Information Queries
-- 4.1 List all workers at the hospital, in ascending last name, first name order.
SELECT FirstName, LastName, JobCategory
FROM Employee
ORDER BY LastName ASC, FirstName ASC;

-- 4.2 List the primary doctors of patients with a high admission rate (at least 4 admissions within a one-year time frame).
SELECT PrimaryDoctorID, COUNT(*) AS AdmissionCount
FROM Admission
WHERE AdmissionDate BETWEEN '2025-01-01' AND '2025-12-31'
GROUP BY PrimaryDoctorID
HAVING AdmissionCount >= 4;t * from author order by authorlast;
