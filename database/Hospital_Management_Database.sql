-- Hospital Management System Database
-- MySQL 8+ / MariaDB 10.4+
-- WARNING: This script drops and recreates the hospital database. Back up existing data first.

DROP DATABASE IF EXISTS HospitalDB;
CREATE DATABASE HospitalDB CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE HospitalDB;

CREATE TABLE Department (
    DepartmentID INT PRIMARY KEY AUTO_INCREMENT,
    DepartmentName VARCHAR(100) NOT NULL UNIQUE,
    Location VARCHAR(100),
    Phone VARCHAR(25)
) ENGINE=InnoDB;

CREATE TABLE Doctor (
    DoctorID INT PRIMARY KEY AUTO_INCREMENT,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    Specialization VARCHAR(100) NOT NULL,
    Phone VARCHAR(25),
    Email VARCHAR(120) UNIQUE,
    DepartmentID INT NOT NULL,
    CONSTRAINT fk_doctor_department FOREIGN KEY (DepartmentID)
        REFERENCES Department(DepartmentID) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE Patient (
    PatientID INT PRIMARY KEY AUTO_INCREMENT,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    DateOfBirth DATE NOT NULL,
    Gender ENUM('Male','Female','Other') NOT NULL,
    Phone VARCHAR(25),
    Email VARCHAR(120) UNIQUE,
    Address VARCHAR(255),
    BloodType ENUM('A+','A-','B+','B-','AB+','AB-','O+','O-')
) ENGINE=InnoDB;

CREATE TABLE Appointment (
    AppointmentID INT PRIMARY KEY AUTO_INCREMENT,
    PatientID INT NOT NULL,
    DoctorID INT NOT NULL,
    AppointmentDate DATETIME NOT NULL,
    Reason VARCHAR(255),
    Status ENUM('Scheduled','Completed','Cancelled') NOT NULL DEFAULT 'Scheduled',
    CONSTRAINT fk_appointment_patient FOREIGN KEY (PatientID)
        REFERENCES Patient(PatientID) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_appointment_doctor FOREIGN KEY (DoctorID)
        REFERENCES Doctor(DoctorID) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE MedicalRecord (
    RecordID INT PRIMARY KEY AUTO_INCREMENT,
    PatientID INT NOT NULL,
    DoctorID INT NOT NULL,
    AppointmentID INT NOT NULL UNIQUE,
    RecordDate DATE NOT NULL,
    Symptoms TEXT,
    Diagnosis TEXT,
    Treatment TEXT,
    Notes TEXT,
    CONSTRAINT fk_record_patient FOREIGN KEY (PatientID)
        REFERENCES Patient(PatientID) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_record_doctor FOREIGN KEY (DoctorID)
        REFERENCES Doctor(DoctorID) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_record_appointment FOREIGN KEY (AppointmentID)
        REFERENCES Appointment(AppointmentID) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- Application users. PasswordHash must contain a hash created by the application's
-- password-hashing library (for example BCrypt), never a plain-text password.
CREATE TABLE Users (
    UserID INT PRIMARY KEY AUTO_INCREMENT,
    Username VARCHAR(50) NOT NULL UNIQUE,
    PasswordHash VARCHAR(255) NOT NULL,
    FullName VARCHAR(120) NOT NULL,
    Role ENUM('Administrator','Receptionist','Doctor') NOT NULL,
    IsActive BOOLEAN NOT NULL DEFAULT TRUE,
    CreatedAt TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;


INSERT INTO Department (DepartmentID, DepartmentName, Location, Phone) VALUES
(1,'Cardiology','Building A - Floor 2','02-2741001'),
(2,'Dermatology','Building A - Floor 1','02-2741002'),
(3,'Pediatrics','Building B - Floor 1','02-2741003'),
(4,'Neurology','Building B - Floor 2','02-2741004'),
(5,'Orthopedics','Building C - Floor 1','02-2741005'),
(6,'General Medicine','Building A - Floor 3','02-2741006');

INSERT INTO Doctor (DoctorID, FirstName, LastName, Specialization, Phone, Email, DepartmentID) VALUES
(1,'Ahmad','Khalil','Cardiology','059-500-0001','doctor1@hospital.test',1),
(2,'Lina','Nasser','Cardiology','059-500-0002','doctor2@hospital.test',1),
(3,'Sami','Odeh','Dermatology','059-500-0003','doctor3@hospital.test',2),
(4,'Rana','Salem','Dermatology','059-500-0004','doctor4@hospital.test',2),
(5,'Maha','Yousef','Pediatrics','059-500-0005','doctor5@hospital.test',3),
(6,'Fadi','Haddad','Pediatrics','059-500-0006','doctor6@hospital.test',3),
(7,'Nour','Issa','Neurology','059-500-0007','doctor7@hospital.test',4),
(8,'Omar','Farah','Orthopedics','059-500-0008','doctor8@hospital.test',5),
(9,'Hala','Mansour','General Medicine','059-500-0009','doctor9@hospital.test',6),
(10,'Kareem','Awad','Cardiology','059-500-0010','doctor10@hospital.test',1);

INSERT INTO Patient (PatientID, FirstName, LastName, DateOfBirth, Gender, Phone, Email, Address, BloodType) VALUES
(1,'Tariq','Zaidan','1973-02-02','Male','059-600-0001','patient1@example.test','Bethlehem, Palestine','A+'),
(2,'Mariam','Khalil','1976-03-03','Female','059-600-0002','patient2@example.test','Bethlehem, Palestine','O+'),
(3,'Yazan','Nasser','1979-04-04','Male','059-600-0003','patient3@example.test','Bethlehem, Palestine','B+'),
(4,'Rania','Odeh','1982-05-05','Female','059-600-0004','patient4@example.test','Bethlehem, Palestine','AB+'),
(5,'Samer','Salem','1985-06-06','Male','059-600-0005','patient5@example.test','Bethlehem, Palestine','A-'),
(6,'Dima','Yousef','1988-07-07','Female','059-600-0006','patient6@example.test','Bethlehem, Palestine','O-'),
(7,'Khaled','Haddad','1991-08-08','Male','059-600-0007','patient7@example.test','Bethlehem, Palestine','B-'),
(8,'Huda','Issa','1994-09-09','Female','059-600-0008','patient8@example.test','Bethlehem, Palestine','AB+'),
(9,'Nidal','Farah','1997-10-10','Male','059-600-0009','patient9@example.test','Bethlehem, Palestine','O+'),
(10,'Salwa','Mansour','2000-11-11','Female','059-600-0010','patient10@example.test','Bethlehem, Palestine','A+'),
(11,'Firas','Awad','2003-12-12','Male','059-600-0011','patient11@example.test','Bethlehem, Palestine','B+'),
(12,'Amal','Qasem','1971-01-13','Female','059-600-0012','patient12@example.test','Bethlehem, Palestine','O-'),
(13,'Jamal','Darwish','1974-02-14','Male','059-600-0013','patient13@example.test','Bethlehem, Palestine','A-'),
(14,'Nadine','Abu Ali','1977-03-15','Female','059-600-0014','patient14@example.test','Bethlehem, Palestine','AB-'),
(15,'Bashar','Hussein','1980-04-16','Male','059-600-0015','patient15@example.test','Bethlehem, Palestine','O+'),
(16,'Reem','Najjar','1983-05-17','Female','059-600-0016','patient16@example.test','Bethlehem, Palestine','B-'),
(17,'Laith','Sabbagh','1986-06-18','Male','059-600-0017','patient17@example.test','Bethlehem, Palestine','A+'),
(18,'Sana','Barakat','1989-07-19','Female','059-600-0018','patient18@example.test','Bethlehem, Palestine','O+'),
(19,'Wael','Hamdan','1992-08-20','Male','059-600-0019','patient19@example.test','Bethlehem, Palestine','AB+'),
(20,'Mona','Saleh','1995-09-21','Female','059-600-0020','patient20@example.test','Bethlehem, Palestine','B+');

INSERT INTO Appointment (AppointmentID, PatientID, DoctorID, AppointmentDate, Reason, Status) VALUES
(1,1,1,'2026-01-02 10:00:00','Routine checkup','Completed'),
(2,2,2,'2026-02-04 11:00:00','Chest discomfort','Completed'),
(3,3,3,'2026-03-06 12:00:00','Skin irritation','Completed'),
(4,4,4,'2026-04-08 13:00:00','Follow-up visit','Completed'),
(5,5,5,'2026-05-10 14:00:00','Headache','Completed'),
(6,6,6,'2026-06-12 15:00:00','Routine checkup','Completed'),
(7,7,7,'2026-07-14 16:00:00','Chest discomfort','Completed'),
(8,8,8,'2026-08-16 09:00:00','Skin irritation','Completed'),
(9,9,9,'2026-09-18 10:00:00','Follow-up visit','Completed'),
(10,10,10,'2026-01-20 11:00:00','Headache','Completed'),
(11,11,1,'2026-02-22 12:00:00','Routine checkup','Completed'),
(12,12,2,'2026-03-24 13:00:00','Chest discomfort','Completed'),
(13,13,3,'2026-04-26 14:00:00','Skin irritation','Completed'),
(14,14,4,'2026-05-01 15:00:00','Follow-up visit','Completed'),
(15,15,5,'2026-06-03 16:00:00','Headache','Completed'),
(16,16,6,'2026-07-05 09:00:00','Routine checkup','Completed'),
(17,17,7,'2026-08-07 10:00:00','Chest discomfort','Completed'),
(18,18,8,'2026-09-09 11:00:00','Skin irritation','Completed'),
(19,19,9,'2026-01-11 12:00:00','Follow-up visit','Completed'),
(20,20,10,'2026-02-13 13:00:00','Headache','Completed'),
(21,1,1,'2026-03-15 14:00:00','Routine checkup','Scheduled'),
(22,2,2,'2026-04-17 15:00:00','Chest discomfort','Scheduled'),
(23,3,3,'2026-05-19 16:00:00','Skin irritation','Scheduled'),
(24,4,4,'2026-06-21 09:00:00','Follow-up visit','Scheduled'),
(25,5,5,'2026-07-23 10:00:00','Headache','Scheduled'),
(26,6,6,'2026-08-25 11:00:00','Routine checkup','Cancelled'),
(27,7,7,'2026-09-27 12:00:00','Chest discomfort','Cancelled'),
(28,8,8,'2026-01-02 13:00:00','Skin irritation','Cancelled'),
(29,9,9,'2026-02-04 14:00:00','Follow-up visit','Cancelled'),
(30,10,10,'2026-03-06 15:00:00','Headache','Cancelled');

INSERT INTO MedicalRecord (RecordID, PatientID, DoctorID, AppointmentID, RecordDate, Symptoms, Diagnosis, Treatment, Notes) VALUES
(1,1,1,1,'2026-01-02','Fatigue','Stable condition','Follow-up if symptoms continue','Sample training data; not real patient information'),
(2,2,2,2,'2026-02-04','Mild pain','Seasonal allergy','Medication as prescribed','Sample training data; not real patient information'),
(3,3,3,3,'2026-03-06','Skin redness','Mild dermatitis','Topical treatment','Sample training data; not real patient information'),
(4,4,4,4,'2026-04-08','Headache','Tension headache','Rest and hydration','Sample training data; not real patient information'),
(5,5,5,5,'2026-05-10','Routine screening','No acute findings','Routine monitoring','Sample training data; not real patient information'),
(6,6,6,6,'2026-06-12','Fatigue','Stable condition','Follow-up if symptoms continue','Sample training data; not real patient information'),
(7,7,7,7,'2026-07-14','Mild pain','Seasonal allergy','Medication as prescribed','Sample training data; not real patient information'),
(8,8,8,8,'2026-08-16','Skin redness','Mild dermatitis','Topical treatment','Sample training data; not real patient information'),
(9,9,9,9,'2026-09-18','Headache','Tension headache','Rest and hydration','Sample training data; not real patient information'),
(10,10,10,10,'2026-01-20','Routine screening','No acute findings','Routine monitoring','Sample training data; not real patient information'),
(11,11,1,11,'2026-02-22','Fatigue','Stable condition','Follow-up if symptoms continue','Sample training data; not real patient information'),
(12,12,2,12,'2026-03-24','Mild pain','Seasonal allergy','Medication as prescribed','Sample training data; not real patient information'),
(13,13,3,13,'2026-04-26','Skin redness','Mild dermatitis','Topical treatment','Sample training data; not real patient information'),
(14,14,4,14,'2026-05-01','Headache','Tension headache','Rest and hydration','Sample training data; not real patient information'),
(15,15,5,15,'2026-06-03','Routine screening','No acute findings','Routine monitoring','Sample training data; not real patient information'),
(16,16,6,16,'2026-07-05','Fatigue','Stable condition','Follow-up if symptoms continue','Sample training data; not real patient information'),
(17,17,7,17,'2026-08-07','Mild pain','Seasonal allergy','Medication as prescribed','Sample training data; not real patient information'),
(18,18,8,18,'2026-09-09','Skin redness','Mild dermatitis','Topical treatment','Sample training data; not real patient information'),
(19,19,9,19,'2026-01-11','Headache','Tension headache','Rest and hydration','Sample training data; not real patient information'),
(20,20,10,20,'2026-02-13','Routine screening','No acute findings','Routine monitoring','Sample training data; not real patient information');


-- DEMO ACCOUNTS: replace each placeholder with a real BCrypt hash before testing login.
-- Suggested usernames: admin, receptionist, doctor1
-- Generate the hash in Java using a BCrypt library; do not store plain-text passwords.
INSERT INTO Users (UserID, Username, PasswordHash, FullName, Role, IsActive) VALUES
(1,'admin','$2a$10$REPLACE.THIS.PLACEHOLDER.WITH.A.REAL.BCRYPT.HASH','System Administrator','Administrator',TRUE),
(2,'receptionist','$2a$10$REPLACE.THIS.PLACEHOLDER.WITH.A.REAL.BCRYPT.HASH','Front Desk Receptionist','Receptionist',TRUE),
(3,'doctor1','$2a$10$REPLACE.THIS.PLACEHOLDER.WITH.A.REAL.BCRYPT.HASH','Dr. Ahmad Khalil','Doctor',TRUE);

-- Useful verification queries
SELECT 'Department' AS TableName, COUNT(*) AS RowCount FROM Department
UNION ALL SELECT 'Doctor', COUNT(*) FROM Doctor
UNION ALL SELECT 'Patient', COUNT(*) FROM Patient
UNION ALL SELECT 'Appointment', COUNT(*) FROM Appointment
UNION ALL SELECT 'MedicalRecord', COUNT(*) FROM MedicalRecord
UNION ALL SELECT 'Users', COUNT(*) FROM Users;
