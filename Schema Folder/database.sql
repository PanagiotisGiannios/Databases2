CREATE TABLE EMPLOYEE (
	SSN				VARCHAR(10)		NOT NULL,
	FirstName		VARCHAR(50)		NOT NULL,
	LastName		VARCHAR(50)		NOT NULL,
	Sex				VARCHAR(10)		NOT NULL,
	Phone			VARCHAR(10)		NOT NULL,
	Email			VARCHAR(150)	NOT NULL,
	JobStartingDate	DATETIME		DEFAULT 	CURRENT_TIMESTAMP,
	Birthday		DATE			NOT NULL,	
	Address			VARCHAR(150)	NOT NULL,	
	Salary			VARCHAR(10)		NOT NULL,	
PRIMARY KEY (SSN));

CREATE TABLE PROFESSOR (
	ProfId			VARCHAR(10) NOT NULL,
	ManagerId		VARCHAR(10),
	Profession		VARCHAR(50)	NOT NULL,
	FOREIGN KEY		(ProfId) REFERENCES EMPLOYEE(SSN) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY		(ManagerId) REFERENCES PROFESSOR(ProfId) ON DELETE SET NULL ON UPDATE CASCADE,
PRIMARY KEY (ProfId));

CREATE TABLE AUXILIARY_STAFF (
	EmployeeId		VARCHAR(10) NOT NULL,
	Profession		VARCHAR(32)	NOT NULL,
	FOREIGN KEY		(EmployeeId) REFERENCES EMPLOYEE(SSN) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY (EmployeeId));

CREATE TABLE PROJECT (
	ProfessorId		VARCHAR(10) NOT NULL,
	Name			VARCHAR(32)	NOT NULL,	
	Field			VARCHAR(50)	NOT NULL,
	Type			VARCHAR(32)	NOT NULL,
	Information		VARCHAR(500) NOT NULL,
	FOREIGN KEY		(ProfessorId) REFERENCES PROFESSOR(ProfId) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY (Name, ProfessorId));

CREATE TABLE COURSE (
	CourseId		VARCHAR(10)	NOT NULL,
	FirstName		VARCHAR(32)	NOT NULL,	
	Semester		VARCHAR(2)	NOT NULL,
	SupervisorId	VARCHAR(10),
	FOREIGN KEY		(SupervisorId) REFERENCES PROFESSOR (ProfId) ON DELETE SET NULL ON UPDATE CASCADE,
PRIMARY KEY (CourseId));

CREATE TABLE STUDENT (
	StudentId		VARCHAR(10)		NOT NULL,
	FirstName		VARCHAR(32)		NOT NULL,
	LastName		VARCHAR(32)		NOT NULL,	
	FatherName		VARCHAR(32)		NOT NULL,
	Sex				VARCHAR(10),
	Semester		VARCHAR(10)		DEFAULT	'1',
	Email			VARCHAR(255)	NOT NULL,		
	Phone			VARCHAR(10)		NOT NULL,
	Birthday		DATE			NOT NULL,
	EntryDate		DATETIME		DEFAULT 	CURRENT_TIMESTAMP,
	Address			VARCHAR(255)	NOT NULL,
PRIMARY KEY (StudentId));

CREATE TABLE BOOK (
	BookId			VARCHAR(10)	NOT NULL,
	FirstName		VARCHAR(32)	NOT NULL,
	Author			VARCHAR(32)	NOT NULL,	
	Genre			VARCHAR(32)	NOT NULL,
PRIMARY KEY (BookId));

CREATE TABLE TAKES_FOR (
	StudentId		VARCHAR(10)		NOT NULL,
	CourseId		VARCHAR(10)		NOT NULL,
	BookId			VARCHAR(10)		NOT NULL,
	FOREIGN KEY		(StudentId) REFERENCES STUDENT (StudentId) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY		(CourseId) 	REFERENCES COURSE (CourseId) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY		(BookId) 	REFERENCES BOOK (BookId) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY (StudentId, CourseId, BookId));

CREATE TABLE TEACHES (
	ProfId			VARCHAR(10)		NOT NULL,
	CourseId		VARCHAR(10)		NOT NULL,
	FOREIGN KEY		(ProfId) 	REFERENCES PROFESSOR (ProfId) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY		(CourseId) 	REFERENCES COURSE (CourseId) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY (ProfId, CourseId));

CREATE TABLE ATTENDS (
	Grade			VARCHAR(10)	DEFAULT '0',
	StudentId		VARCHAR(10)	NOT NULL,
	CourseId		VARCHAR(10)	NOT NULL,
	FOREIGN KEY		(StudentId) REFERENCES STUDENT (StudentId) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY		(CourseId) 	REFERENCES COURSE (CourseId) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY (StudentId, CourseId));
