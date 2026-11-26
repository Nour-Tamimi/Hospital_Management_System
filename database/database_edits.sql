
-- Migration V1: Link user accounts to doctor records
-- Run once after importing the original HospitalDB.sql
-- Do NOT run this migration more than once.

USE HospitalDB;

-- 1. Add DoctorID to Users
ALTER TABLE Users
ADD COLUMN DoctorID INT NULL UNIQUE,
ADD CONSTRAINT fk_users_doctor
FOREIGN KEY (DoctorID)
REFERENCES Doctor(DoctorID);

-- 2. Link the demo doctor account to DoctorID = 1
UPDATE Users
SET DoctorID = 1
WHERE Username = 'doctor1';
