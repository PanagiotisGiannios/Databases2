CREATE TABLE EMPLOYEE (
	SSN				INTEGER		NOT NULL,
	FirstName		VARCHAR(32)		NOT NULL,
	LastName		VARCHAR(32)		NOT NULL,
	Sex				VARCHAR(10)		NOT NULL,
	Phone			VARCHAR(10)		NOT NULL,
	Email			VARCHAR(255)	NOT NULL,
	JobStartingDate	DATETIME		DEFAULT 	CURRENT_TIMESTAMP,
	Birthday		DATE			NOT NULL,	
	Address			VARCHAR(255)	NOT NULL,	
	Salary			INTEGER			NOT NULL,	
PRIMARY KEY (SSN));

CREATE TABLE PROFESSOR (
	ProfId			INTEGER 	NOT NULL,
	ManagerId		INTEGER 	,
	Profession		VARCHAR(32)	NOT NULL,
	FOREIGN KEY		(ProfId) REFERENCES EMPLOYEE(SSN) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY		(ManagerId) REFERENCES PROFESSOR(ProfId) ON DELETE SET NULL ON UPDATE CASCADE,
PRIMARY KEY (ProfId));

CREATE TABLE AUXILIARY_STAFF (
	EmployeeId		INTEGER 	NOT NULL,
	Profession		VARCHAR(32)	NOT NULL,
	FOREIGN KEY		(EmployeeId) REFERENCES EMPLOYEE(SSN) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY (EmployeeId));

CREATE TABLE SECRETARIAT (
	EmployeeId 		INTEGER NOT NULL,
	Typing_Speed	INTEGER	NOT NULL,
	FOREIGN KEY		(EmployeeId) REFERENCES EMPLOYEE(SSN) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY (EmployeeId));

CREATE TABLE MEMBER (
	EmployeeId		INTEGER 	NOT NULL,
	FirstName		VARCHAR(32)	NOT NULL,	
	Birthday		DATE		NOT NULL,
	Kinship			VARCHAR(32)	NOT NULL,
	Sex				VARCHAR(10)	NOT NULL,	
	FOREIGN KEY		(EmployeeId) REFERENCES EMPLOYEE(SSN) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY (FirstName, EmployeeId));

CREATE TABLE COURSE (
	CourseId		INTEGER		NOT NULL,
	FirstName		VARCHAR(32)	NOT NULL,	
	Semester		INTEGER		NOT NULL,
	SupervisorId	INTEGER		,
	FOREIGN KEY		(SupervisorId) REFERENCES PROFESSOR (ProfId) ON DELETE SET NULL ON UPDATE CASCADE,
PRIMARY KEY (CourseId));

CREATE TABLE STUDENT (
	StudentId		INTEGER			NOT NULL,
	FirstName		VARCHAR(32)		NOT NULL,
	LastName		VARCHAR(32)		NOT NULL,	
	FatherName		VARCHAR(32)		NOT NULL,
	Sex				VARCHAR(10),
	Semester		INTEGER			DEFAULT	1,
	Email			VARCHAR(255)	NOT NULL,		
	Phone			VARCHAR(10)		NOT NULL,
	Birthday		DATE			NOT NULL,
	EntryDate		DATETIME		DEFAULT 	CURRENT_TIMESTAMP,
	Address			VARCHAR(255)	NOT NULL,
PRIMARY KEY (StudentId));

CREATE TABLE BOOK (
	BookId			INTEGER		NOT NULL,
	FirstName		VARCHAR(32)	NOT NULL,
	Author			VARCHAR(32)	NOT NULL,	
	Genre			VARCHAR(32)	NOT NULL,
PRIMARY KEY (BookId));

CREATE TABLE TAKES_FOR (
	StudentId		INTEGER		NOT NULL,
	CourseId		INTEGER		NOT NULL,
	BookId			INTEGER		NOT NULL,
	FOREIGN KEY		(StudentId) REFERENCES STUDENT (StudentId) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY		(CourseId) 	REFERENCES COURSE (CourseId) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY		(BookId) 	REFERENCES BOOK (BookId) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY (StudentId, CourseId, BookId));

CREATE TABLE TEACHES (
	ProfId			INTEGER		NOT NULL,
	CourseId		INTEGER		NOT NULL,
	FOREIGN KEY		(ProfId) 	REFERENCES PROFESSOR (ProfId) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY		(CourseId) 	REFERENCES COURSE (CourseId) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY (ProfId, CourseId));

CREATE TABLE ATTENDS (
	Grade			INTEGER	DEFAULT 0,
	StudentId		INTEGER	NOT NULL,
	CourseId		INTEGER	NOT NULL,
	FOREIGN KEY		(StudentId) REFERENCES STUDENT (StudentId) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY		(CourseId) 	REFERENCES COURSE (CourseId) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY (StudentId, CourseId));
