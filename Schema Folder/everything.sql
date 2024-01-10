CREATE TABLE EMPLOYEE (
	SSN				INTEGER		    NOT NULL,
	FirstName		VARCHAR(50)		NOT NULL,
	LastName		VARCHAR(50)		NOT NULL,
	Sex				VARCHAR(10)		NOT NULL,
	Phone			VARCHAR(10)		NOT NULL,
	Email			VARCHAR(150)	NOT NULL,
	JobStartingDate	DATE			NOT NULL,
	Birthday		DATE			NOT NULL,	
	Address			VARCHAR(150)	NOT NULL,	
	Salary			INTEGER		    NOT NULL,	
PRIMARY KEY (SSN));

CREATE TABLE PROFESSOR (
	ProfId			INTEGER 	 NOT NULL,
	ManagerId		INTEGER,
	Profession		VARCHAR(50)	NOT NULL,
	FOREIGN KEY		(ProfId) REFERENCES EMPLOYEE(SSN) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY		(ManagerId) REFERENCES PROFESSOR(ProfId) ON DELETE SET NULL ON UPDATE CASCADE,
PRIMARY KEY (ProfId));

CREATE TABLE AUXILIARY_STAFF (
	EmployeeId		INTEGER NOT NULL,
	Profession		VARCHAR(50)	NOT NULL,
	FOREIGN KEY		(EmployeeId) REFERENCES EMPLOYEE(SSN) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY (EmployeeId));

CREATE TABLE PROJECT (
	ProfessorId		INTEGER NOT NULL,
	Name			VARCHAR(50)	NOT NULL,	
	Field			VARCHAR(50)	NOT NULL,
	Type			VARCHAR(50)	NOT NULL,
	Information		VARCHAR(500) NOT NULL,
	FOREIGN KEY		(ProfessorId) REFERENCES PROFESSOR(ProfId) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY (Name, ProfessorId));

CREATE TABLE COURSE (
	CourseId		INTEGER	NOT NULL,
	Name			VARCHAR(50)	NOT NULL,	
	Semester		VARCHAR(2)	NOT NULL,
PRIMARY KEY (CourseId));

CREATE TABLE STUDENT (
    StudentId   INTEGER AUTO_INCREMENT 	NOT NULL,
    FirstName   VARCHAR(50)     		NOT NULL,
    LastName    VARCHAR(50)     		NOT NULL,
    FatherName  VARCHAR(50)     		NOT NULL,
    Sex         VARCHAR(10),
    Semester    TINYINT        			DEFAULT 1,
    Email       VARCHAR(150)   			NOT NULL,
    Phone       VARCHAR(10)    			NOT NULL,
    Birthday    DATE           			NOT NULL,
    EntryDate   DATE       				NOT NULL,
    Address     VARCHAR(255)   			NOT NULL,
    PRIMARY KEY (StudentId)
) AUTO_INCREMENT = 1054;

CREATE TABLE TEACHES (
	ProfId			INTEGER		NOT NULL,
	CourseId		INTEGER		NOT NULL,
	FOREIGN KEY		(ProfId) 	REFERENCES PROFESSOR (ProfId) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY		(CourseId) 	REFERENCES COURSE (CourseId) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY (ProfId, CourseId));

CREATE TABLE ATTENDS (
	Grade			REAL		DEFAULT -1,
	StudentId		INTEGER		NOT NULL,
	CourseId		INTEGER		NOT NULL,
	FOREIGN KEY		(StudentId) REFERENCES STUDENT (StudentId) ON DELETE CASCADE ON UPDATE CASCADE,
	FOREIGN KEY		(CourseId) 	REFERENCES COURSE (CourseId) ON DELETE CASCADE ON UPDATE CASCADE,
PRIMARY KEY (StudentId, CourseId));

INSERT INTO EMPLOYEE (SSN, FirstName, LastName, Sex, Phone, Email, JobStartingDate, Birthday, Address, Salary)
VALUES
    (19900001, 'John', 'Doe', 'Male', '1234567890', 'john.doe@example.com', '2023-01-15', '1990-05-15', '123 Main St', 50000),
    (19920002, 'Alice', 'Johnson', 'Female', '9876543210', 'alice.johnson@example.com', '2023-02-15', '1992-08-20', '456 Oak St', 55000),
    (19880003, 'Bob', 'Williams', 'Male', '5678901234', 'bob.williams@example.com', '2023-02-08', '1988-07-10', '789 Pine St', 48000),
    (19950004, 'Eva', 'Smith', 'Female', '3456789012', 'eva.smith@example.com', '2023-03-20', '1995-11-02', '101 Cedar St', 52000),
    (19910005, 'James', 'Anderson', 'Male', '7890123456', 'james.anderson@example.com', '2023-04-15', '1991-09-25', '202 Birch St', 53000),
    (19930006, 'Sophia', 'Clark', 'Female', '2345678901', 'sophia.clark@example.com', '2023-01-10', '1993-12-05', '303 Oak St', 51000),
    (19890007, 'Michael', 'Taylor', 'Male', '8901234567', 'michael.taylor@example.com', '2023-03-15', '1989-04-15', '505 Pine St', 54000),
    (19940008, 'Olivia', 'Miller', 'Female', '4567890123', 'olivia.miller@example.com', '2023-05-22', '1994-06-30', '707 Cedar St', 49000),
    (19920009, 'Daniel', 'Harris', 'Male', '1238904567', 'daniel.harris@example.com', '2023-06-08', '1992-09-30', '909 Main St', 47000),
    (19960010, 'Ava', 'Martin', 'Female', '6789012345', 'ava.martin@example.com', '2023-07-12', '1996-08-08', '111 Oak St', 56000),
    (19900011, 'Liam', 'Jones', 'Male', '9012345678', 'liam.jones@example.com', '2023-05-11', '1990-11-18', '404 Pine St', 48000),
    (19930012, 'Emma', 'White', 'Female', '3456789012', 'emma.white@example.com', '2023-12-15', '1993-04-03', '606 Cedar St', 52000),
    (19950013, 'Jackson', 'Moore', 'Male', '2345678901', 'jackson.moore@example.com', '2023-11-20', '1995-07-20', '808 Main St', 51000),
    (19920014, 'Avery', 'Johnson', 'Female', '7890123456', 'avery.johnson@example.com', '2023-10-05', '1992-10-15', '909 Oak St', 49000),
    (19880015, 'Elijah', 'Davis', 'Male', '4567890123', 'elijah.davis@example.com', '2023-10-25', '1988-12-30', '101 Cedar St', 53000),
    (19940016, 'Mia', 'Wilson', 'Female', '8901234567', 'mia.wilson@example.com', '2023-09-25', '1994-03-25', '202 Birch St', 50000),
    (19960017, 'James', 'Brown', 'Male', '1234567890', 'james.brown@example.com', '2023-08-10', '1996-05-05', '303 Oak St', 54000),
    (19890018, 'Scarlett', 'Taylor', 'Female', '5678901234', 'scarlett.taylor@example.com', '2023-11-05', '1989-09-10', '404 Pine St', 55000),
    (19910019, 'Logan', 'Clark', 'Male', '2345678901', 'logan.clark@example.com', '2023-07-20', '1991-08-22', '505 Pine St', 47000),
    (19970020, 'Abigail', 'Smith', 'Female', '8901234567', 'abigail.smith@example.com', '2023-06-30', '1997-01-28', '606 Cedar St', 56000),
    (19930021, 'Ethan', 'Harris', 'Male', '4567890123', 'ethan.harris@example.com', '2023-05-12', '1993-11-15', '707 Oak St', 51000),
    (19950022, 'Mia', 'Martin', 'Female', '2345678901', 'mia.martin@example.com', '2023-12-30', '1995-07-02', '808 Main St', 49000),
    (19920023, 'Noah', 'Miller', 'Male', '7890123456', 'noah.miller@example.com', '2023-04-18', '1992-02-22', '909 Pine St', 53000),
    (19880024, 'Aria', 'Johnson', 'Female', '4567890123', 'aria.johnson@example.com', '2023-03-15', '1988-09-10', '101 Birch St', 52000),
    (19940025, 'Ethan', 'Wilson', 'Male', '1234567890', 'ethan.wilson@example.com', '2023-07-14', '1994-06-05', '303 Cedar St', 50000),
    (19960026, 'Ava', 'Taylor', 'Female', '6789012345', 'ava.taylor@example.com', '2023-12-12', '1996-08-18', '404 Oak St', 55000),
    (19900027, 'Liam', 'Anderson', 'Male', '9012345678', 'liam.anderson@example.com', '2023-11-22', '1990-01-30', '505 Main St', 47000),
    (19930028, 'Ella', 'Clark', 'Female', '3456789012', 'ella.clark@example.com', '2023-06-25', '1993-10-15', '606 Pine St', 49000),
    (19950029, 'Jackson', 'Brown', 'Male', '2345678901', 'jackson.brown@example.com', '2023-09-10', '1995-12-28', '707 Oak St', 51000),
    (19920030, 'Avery', 'Taylor', 'Female', '7890123456', 'avery.taylor@example.com', '2023-08-20', '1992-03-12', '808 Cedar St', 54000);

INSERT INTO PROFESSOR (ProfId, ManagerId, Profession)
VALUES
    (19900001, NULL, 'Computer Hardware and Architecture'),
    (19920002, 19900001, 'Signals and Communications'),
    (19880003, 19900001, 'Applications and Foundations of Computer Science'),
    (19950004, 19900001, 'Energy'),
    (19910005, 19900001, 'Software and Information System Engineering'),
    (19930006, 19900001, 'Software and Information System Engineering'),
    (19890007, 19900001, 'Energy'),
    (19940008, 19900001, 'Applications and Foundations of Computer Science'),
    (19920009, 19900001, 'Computer Hardware and Architecture'),
    (19960010, 19900001, 'Energy'),
    (19900011, 19900001, 'Software and Information System Engineering'),
    (19930012, 19900001, 'Applications and Foundations of Computer Science'),
    (19950013, 19900001, 'Signals and Communications'),
    (19920014, 19900001, 'Signals and Communications'),
    (19880015, 19900001, 'Energy'),
    (19940016, 19900001, 'Applications and Foundations of Computer Science'),
    (19960017, 19900001, 'Software and Information System Engineering'),
    (19890018, 19900001, 'Signals and Communications'),
    (19910019, 19900001, 'Applications and Foundations of Computer Science'),
    (19970020, 19900001, 'Software and Information System Engineering');

INSERT INTO PROJECT (ProfessorId, Name, Field, Type, Information)
VALUES
    (19900001, 'Project 1', 'Comp. Architecture & Hardware', 'Research', 'Description 1'),
    (19920002, 'Project 2', 'Signals & Communications', 'Development', 'Description 2'),
    (19880003, 'Project 3', 'Apps & Foundations of C.S.', 'Education', 'Description 3'),
    (19950004, 'Project 4', 'Energy', 'Research', 'Description 4'),
    (19910005, 'Project 5', 'Software & Info. Sys. Eng.', 'Development', 'Description 5'),
    (19930006, 'Project 6', 'Software & Info. Sys. Eng.', 'Development', 'Description 6'),
    (19890007, 'Project 7', 'Energy', 'Research', 'Description 7'),
    (19940008, 'Project 8', 'Apps & Foundations of C.S.', 'Education', 'Description 8'),
    (19920009, 'Project 9', 'Comp. Architecture & Hardware', 'Development', 'Description 9'),
    (19960010, 'Project 10', 'Energy', 'Research', 'Description 10'),
    (19900011, 'Project 11', 'Software & Info. Sys. Eng.', 'Development', 'Description 11'),
    (19930012, 'Project 12', 'Apps & Foundations of C.S.', 'Education', 'Description 12'),
    (19950013, 'Project 13', 'Signals & Communications', 'Development', 'Description 13'),
    (19920014, 'Project 14', 'Software & Info. Sys. Eng.', 'Development', 'Description 14'),
    (19880015, 'Project 15', 'Energy', 'Research', 'Description 15');

INSERT INTO auxiliary_staff (EmployeeID, profession)
VALUES
    (19880024, 'Secretariat'),
    (19900027, 'IT Support Specialist'),
    (19920023, 'Lab Technician'),
    (19920030, 'Cleaning Staff'),
    (19930021, 'IT Support Specialist'),
    (19930028, 'Secretariat'),
    (19940025, 'Secretariat'),
    (19950022, 'Cleaning Staff'),
    (19950029, 'Student Advisor'),
    (19960026, 'Facilities Manager');

INSERT INTO Course (CourseId, Name, Semester) 
VALUES
    (0101, 'Course 1', 1),
    (0102, 'Course 2', 1),
    (0201, 'Course 3', 2),
    (0202, 'Course 4', 2),
    (0301, 'Course 5', 3),
    (0302, 'Course 6', 3),
    (0401, 'Course 7', 4),
    (0402, 'Course 8', 4),
    (0501, 'Course 9', 5),
    (0502, 'Course 10', 5),
    (0601, 'Course 11', 6),
    (0602, 'Course 12', 6),
    (0701, 'Course 13', 7),
    (0702, 'Course 14', 7),
    (0801, 'Course 15', 8),
    (0802, 'Course 16', 8),
    (0901, 'Course 17', 9),
    (0902, 'Course 18', 9),
    (1001, 'Course 19', 10),
    (1002, 'Course 20', 10),
    (0103, 'Course 21', 1),
    (0104, 'Course 22', 1),
    (0203, 'Course 23', 2),
    (0204, 'Course 24', 2),
    (0303, 'Course 25', 3),
    (0304, 'Course 26', 3),
    (0403, 'Course 27', 4),
    (0404, 'Course 28', 4),
    (0503, 'Course 29', 5),
    (0504, 'Course 30', 5),
    (0603, 'Course 31', 6),
    (0604, 'Course 32', 6),
    (0703, 'Course 33', 7),
    (0704, 'Course 34', 7),
    (0803, 'Course 35', 8),
    (0804, 'Course 36', 8),
    (0903, 'Course 37', 9),
    (0904, 'Course 38', 9),
    (1003, 'Course 39', 10),
    (1004, 'Course 40', 10),
    (0105, 'Course 41', 1),
    (0106, 'Course 42', 1),
    (0205, 'Course 43', 2),
    (0206, 'Course 44', 2),
    (0305, 'Course 45', 3),
    (0306, 'Course 46', 3),
    (0405, 'Course 47', 4),
    (0406, 'Course 48', 4),
    (0505, 'Course 49', 5),
    (0506, 'Course 50', 5);

INSERT INTO student (FirstName, LastName, FatherName, Sex, Semester, Email, Phone, Birthday, EntryDate, Address) 
VALUES  
    ('Kimbra', 'Raggett', 'Hermie', 'Female', 8, 'hraggett0@flickr.com', '1616200219', '1997-11-04', '2021-02-03', '7046 Manufacturers Center'),
    ('Carlyn', 'Larrington', 'Fran', 'Female', 4, 'flarrington1@mit.edu', '3207262382', '1998-01-30', '2022-01-17', '95 Grayhawk Point'),
    ('Chlo', 'Colls', 'Jeramey', 'Female', 5, 'jcolls2@theglobeandmail.com', '7746867955', '1987-02-27', '2022-10-30', '4323 Di Loreto Trail'),
    ('Danita', 'Castell', 'Wain', 'Female', 3, 'wcastell3@auda.org.au', '5676202554', '1996-02-26', '2023-07-28', '631 Sundown Avenue'),
    ('Anneliese', 'Aires', 'Welbie', 'Female', 7, 'waires4@taobao.com', '4177254585', '1983-03-28', '2022-10-26', '42 Spaight Crossing'),
    ('Waldemar', 'Lunnon', 'Andres', 'Male', 1, 'alunnon5@loc.gov', '6752694954', '2001-05-27', '2020-07-17', '9 Harper Lane'),
    ('Noelani', 'Dainter', 'Elwin', 'Female', 9, 'edainter6@opensource.org', '3114357731', '1985-12-06', '2020-11-24', '6 Dapin Junction'),
    ('Porty', 'Grindle', 'Lucas', 'Male', 4, 'lgrindle7@ovh.net', '2801184950', '1993-01-19', '2023-11-20', '6741 Bunting Drive'),
    ('Wallache', 'Cotgrove', 'Cody', 'Male', 1, 'ccotgrove8@skyrock.com', '7795984961', '2004-08-21', '2022-11-17', '29649 Clemons Alley'),
    ('Julianne', 'Tweddell', 'Ari', 'Female', 9, 'atweddell9@economist.com', '8583155107', '1998-02-18', '2023-03-04', '68773 Dapin Terrace'),
    ('Brenden', 'Bratcher', 'Carlie', 'Male', 1, 'cbratchera@apache.org', '5604105557', '1981-12-03', '2022-06-06', '15 Oak Valley Alley'),
    ('Evin', 'Filkov', 'Vladamir', 'Male', 2, 'vfilkovb@usa.gov', '9523064701', '1996-05-12', '2021-02-13', '74847 Grayhawk Trail'),
    ('Barclay', 'Devon', 'Felicio', 'Male', 2, 'fdevonc@youku.com', '4445357830', '1988-02-06', '2020-12-30', '2184 Havey Drive'),
    ('Berne', 'Greenig', 'Richart', 'Male', 10, 'rgreenigd@woothemes.com', '7862996336', '1981-10-18', '2020-09-08', '372 Ridge Oak Junction'),
    ('Jany', 'Valasek', 'Terence', 'Female', 10, 'tvalaseke@spotify.com', '2531382616', '1990-03-14', '2020-06-25', '788 School Street'),
    ('Sayer', 'Jewel', 'Jerome', 'Male', 1, 'jjewelf@imgur.com', '6022970288', '1982-05-30', '2020-12-25', '2 Warbler Hill'),
    ('Caterina', 'Garnsworthy', 'Tally', 'Female', 3, 'tgarnsworthyg@java.com', '8555499011', '1991-08-13', '2023-03-20', '2843 Boyd Parkway'),
    ('Vikky', 'Monument', 'Reed', 'Female', 2, 'rmonumenth@yahoo.com', '5924206200', '1983-07-10', '2022-03-18', '4 Summit Street'),
    ('Flore', 'Ullyott', 'Falito', 'Female', 5, 'fullyotti@wisc.edu', '2994077407', '2001-04-08', '2022-08-17', '26393 Leroy Plaza'),
    ('Shea', 'Percy', 'Robers', 'Male', 7, 'rpercyj@topsy.com', '4935897723', '1999-07-15', '2020-11-14', '1037 Welch Court'),
    ('Barnie', 'Verrick', 'Billie', 'Male', 3, 'bverrickk@dailymail.co.uk', '8441263245', '1991-12-19', '2023-09-06', '57544 Melody Drive'),
    ('Mathilde', 'Wison', 'Newton', 'Female', 6, 'nwisonl@newyorker.com', '4973796960', '2000-08-22', '2023-04-14', '1 New Castle Alley'),
    ('Adda', 'Gambles', 'Tailor', 'Female', 8, 'tgamblesm@bbc.co.uk', '1199843525', '1983-06-11', '2021-01-18', '6 Westridge Terrace'),
    ('Philippine', 'Fish', 'Hobey', 'Female', 10, 'hfishn@patch.com', '7394117280', '1998-03-18', '2022-02-11', '4 Prentice Lane'),
    ('Sharline', 'Trenholme', 'Vladamir', 'Female', 2, 'vtrenholmeo@about.me', '8610755602', '1987-05-17', '2023-08-19', '65 Arizona Drive'),
    ('Leilah', 'Rubinfeld', 'Mill', 'Female', 4, 'mrubinfeldp@umich.edu', '3954154260', '1983-10-08', '2022-08-16', '33 Sachtjen Terrace'),
    ('Julina', 'Creelman', 'Giles', 'Female', 4, 'gcreelmanq@indiegogo.com', '4873233676', '1984-04-15', '2022-06-07', '7 Riverside Alley'),
    ('Rivy', 'Martine', 'Austin', 'Female', 6, 'amartiner@cbc.ca', '1739166020', '1982-02-16', '2022-08-17', '16781 Monument Parkway'),
    ('Ranice', 'Jordan', 'Chariot', 'Female', 2, 'cjordans@feedburner.com', '1914209685', '1986-03-10', '2022-06-26', '7516 Westridge Park'),
    ('Crosby', 'Aasaf', 'Sky', 'Male', 5, 'saasaft@cocolog-nifty.com', '9048231279', '2004-01-19', '2023-11-23', '3424 Thackeray Junction'),
    ('Cristobal', 'Elwell', 'Gaylor', 'Male', 9, 'gelwellu@bloomberg.com', '6111778846', '1988-01-13', '2020-06-07', '30 Tomscot Plaza'),
    ('Ricky', 'Andreaccio', 'Morgun', 'Male', 6, 'mandreacciov@cornell.edu', '2950059492', '1991-09-16', '2022-12-31', '8 Bobwhite Center'),
    ('Gelya', 'Lasham', 'Shelden', 'Female', 3, 'slashamw@google.cn', '1606818601', '1983-03-17', '2023-05-25', '55 Center Center'),
    ('Linnie', 'Meus', 'Sim', 'Female', 2, 'smeusx@github.com', '2031291917', '1998-09-15', '2023-04-13', '2 Paget Parkway'),
    ('Chrysler', 'Axtens', 'Lancelot', 'Female', 4, 'laxtensy@alexa.com', '3987600754', '1984-12-24', '2021-04-25', '89 Columbus Way'),
    ('Pail', 'Wehnerr', 'Kilian', 'Male', 6, 'kwehnerrz@360.cn', '9268873641', '1997-05-05', '2021-07-13', '42 Bunker Hill Place'),
    ('Simmonds', 'Bottomore', 'Merrel', 'Male', 1, 'mbottomore10@businessinsider.com', '1881317743', '1991-06-07', '2023-01-13', '905 Trailsway Way'),
    ('Alisun', 'Arson', 'Tymon', 'Female', 5, 'tarson11@webmd.com', '5953053233', '2000-08-11', '2022-09-15', '8 Lunder Place'),
    ('Cyrillus', 'Glascott', 'Orion', 'Male', 10, 'oglascott12@hibu.com', '6124189893', '1981-04-11', '2022-02-20', '94304 Derek Point'),
    ('Ronalda', 'Wimp', 'Nickey', 'Female', 4, 'nwimp13@booking.com', '9565202742', '1985-06-29', '2022-09-28', '4 Longview Avenue'),
    ('Ada', 'Demoge', 'Denny', 'Female', 4, 'ddemoge14@independent.co.uk', '7913390485', '1989-08-30', '2023-12-20', '938 Forest Run Hill'),
    ('Chrissie', 'Lomaz', 'Arte', 'Female', 8, 'alomaz15@liveinternet.ru', '5207172707', '1990-04-20', '2021-07-06', '39535 Cardinal Way'),
    ('Gonzales', 'Zambon', 'Byran', 'Male', 10, 'bzambon16@gmpg.org', '2636101614', '2000-09-26', '2023-04-30', '73950 Bunting Plaza'),
    ('Stevy', 'Dell Casa', 'Leopold', 'Male', 9, 'ldellcasa17@blogspot.com', '6222941986', '1998-05-19', '2021-11-01', '4862 Banding Road'),
    ('Caesar', 'Dabell', 'Chan', 'Male', 2, 'cdabell18@cisco.com', '4162932305', '1995-01-17', '2022-06-21', '268 Express Parkway'),
    ('Udale', 'Fishpoole', 'Floyd', 'Male', 9, 'ffishpoole19@geocities.com', '2400312762', '2001-11-19', '2022-10-04', '91658 Melvin Street'),
    ('Glenn', 'O''Dougherty', 'Pate', 'Female', 5, 'podougherty1a@aol.com', '9900558600', '1996-06-20', '2020-05-14', '76812 Crowley Crossing'),
    ('Edgar', 'Daykin', 'Malachi', 'Male', 5, 'mdaykin1b@uiuc.edu', '2642375506', '1990-04-11', '2020-03-14', '42 Di Loreto Crossing'),
    ('Hilary', 'Lowndes', 'Hugues', 'Female', 7, 'hlowndes1c@yelp.com', '7218822931', '1994-06-15', '2021-11-01', '919 Canary Road'),
    ('Jesus', 'Gerard', 'Krishna', 'Male', 5, 'kgerard1d@paypal.com', '4908844683', '1988-02-16', '2023-08-10', '1 Mariners Cove Trail');

INSERT INTO teaches (ProfID, CourseID) 
VALUES
    (19890018, 101),
    (19880015, 101),
    (19890007, 101),
    (19900001, 102),
    (19900011, 102),
    (19910005, 103),
    (19910019, 103),
    (19920002, 104),
    (19920009, 104),
    (19920014, 104),
    (19930006, 105),
    (19930012, 105),
    (19940008, 106),
    (19940016, 106),
    (19950004, 201),
    (19950013, 201),
    (19960010, 202),
    (19960017, 202),
    (19970020, 203),
    (19890018, 204),
    (19880015, 204),
    (19890007, 205),
    (19900001, 206),
    (19900011, 206),
    (19910005, 301),
    (19910019, 301),
    (19920002, 302),
    (19920009, 302),
    (19920014, 303),
    (19930006, 303),
    (19930012, 304),
    (19940008, 305),
    (19940016, 305),
    (19950004, 401),
    (19950013, 401),
    (19960010, 402),
    (19960017, 402),
    (19970020, 403),
    (19890018, 404),
    (19880015, 404),
    (19890007, 405),
    (19900001, 406),
    (19900011, 501),
    (19910005, 501),
    (19910019, 502),
    (19920002, 502),
    (19920009, 503),
    (19920014, 504),
    (19930006, 504),
    (19930012, 505),
    (19940008, 506),
    (19940016, 506),
    (19950004, 601),
    (19950013, 601),
    (19960010, 602),
    (19960017, 602),
    (19970020, 603),
    (19890018, 603),
    (19880015, 604),
    (19890007, 701),
    (19900001, 701),
    (19900011, 702),
    (19910005, 702),
    (19910019, 703),
    (19920002, 704),
    (19920009, 704),
    (19920014, 801),
    (19930006, 801),
    (19930012, 802),
    (19940008, 802),
    (19940016, 803),
    (19950004, 803),
    (19950013, 804),
    (19960010, 804),
    (19960017, 901),
    (19970020, 901),
    (19890018, 902),
    (19880015, 902),
    (19890007, 903),
    (19900001, 903),
    (19900011, 904),
    (19910005, 904),
    (19910019, 1001),
    (19920002, 1001),
    (19920009, 1002),
    (19920014, 1002),
    (19930006, 1003),
    (19930012, 1003),
    (19940008, 1004),
    (19940016, 1004),
    (19920002, 903),
    (19950013, 204),
    (19970020, 401),
    (19940008, 401),
    (19890018, 401),
    (19960017, 401),
    (19930012, 401);

INSERT INTO Attends (StudentID, CourseID, Grade) 
VALUES
    ('1054', '101', -1), ('1054', '102', -1), ('1054', '103', -1), ('1054', '104', -1), ('1054', '105', -1),
    ('1055', '201', 5.7), ('1056', '301', 9.2), ('1056', '302', -1), ('1056', '303', 8.3), ('1056', '304', 6.5),
    ('1056', '305', 7.8), ('1057', '401', -1), ('1057', '402', -1), ('1057', '403', 3.6), ('1057', '404', -1),
    ('1057', '405', -1), ('1058', '501', 4.2), ('1058', '502', -1), ('1058', '503', -1), ('1058', '504', 6.8),
    ('1058', '505', -1), ('1059', '601', -1), ('1059', '602', -1), ('1059', '603', 7.2), ('1059', '604', -1),
    ('1059', '701', -1), ('1060', '801', 8.9), ('1060', '802', 6.4), ('1060', '803', 9.7), ('1060', '804', 7.1),
    ('1060', '901', -1), ('1061', '902', -1), ('1061', '903', -1), ('1061', '904', 5.3), ('1061', '1001', -1),
    ('1061', '1002', -1), ('1062', '1003', -1), ('1062', '1004', 4.8), ('1063', '101', 7.5), ('1063', '102', -1),
    ('1063', '103', -1), ('1063', '104', 8.1), ('1063', '105', -1), ('1064', '201', 6.2), ('1065', '301', -1),
    ('1065', '302', -1), ('1065', '303', 9.8), ('1065', '304', -1), ('1065', '305', -1), ('1066', '401', 8.7),
    ('1066', '402', -1), ('1066', '403', 3.9), ('1066', '404', -1), ('1066', '405', -1), ('1067', '501', -1),
    ('1067', '502', -1), ('1067', '503', -1), ('1067', '504', 7.7), ('1067', '505', -1), ('1068', '601', 2.8),
    ('1068', '602', 6.6), ('1068', '603', -1), ('1068', '604', -1), ('1068', '701', -1), ('1069', '801', 9.4),
    ('1069', '802', 5.6), ('1069', '803', 7.3), ('1069', '804', 8.5), ('1069', '901', -1), ('1070', '902', -1),
    ('1070', '903', -1), ('1070', '904', 4.7), ('1070', '1001', -1), ('1070', '1002', -1), ('1071', '1003', 6.9),
    ('1071', '1004', -1), ('1072', '101', -1), ('1072', '102', -1), ('1072', '103', -1), ('1072', '104', -1),
    ('1072', '105', -1), ('1073', '201', 5.1), ('1074', '301', -1), ('1074', '302', -1), ('1074', '303', -1),
    ('1074', '304', 8.2), ('1074', '305', -1), ('1075', '401', -1), ('1075', '402', -1), ('1075', '403', 6.3),
    ('1075', '404', -1), ('1075', '405', -1), ('1076', '501', -1), ('1076', '502', -1), ('1076', '503', 7.9),
    ('1076', '504', -1), ('1076', '505', -1), ('1077', '601', -1), ('1077', '602', -1), ('1077', '603', -1),
    ('1077', '604', -1), ('1077', '701', -1), ('1078', '801', -1), ('1078', '802', -1), ('1078', '803', -1),
    ('1078', '804', -1), ('1078', '901', -1), ('1079', '902', -1), ('1079', '903', -1), ('1079', '904', -1),
    ('1079', '1001', -1), ('1079', '1002', -1), ('1080', '1003', -1), ('1080', '1004', -1), ('1081', '101', -1),
    ('1081', '102', -1), ('1081', '103', -1), ('1081', '104', -1), ('1081', '105', -1), ('1082', '201', -1),
    ('1083', '301', -1), ('1083', '302', -1), ('1083', '303', -1), ('1083', '304', -1), ('1083', '305', -1),
    ('1084', '401', -1), ('1084', '402', -1), ('1084', '403', -1), ('1084', '404', -1), ('1084', '405', -1),
    ('1085', '501', -1), ('1085', '502', -1), ('1085', '503', -1), ('1085', '504', -1), ('1085', '505', -1),
    ('1086', '601', -1), ('1086', '602', -1), ('1086', '603', -1), ('1086', '604', -1), ('1086', '701', -1),
    ('1087', '801', -1), ('1087', '802', -1), ('1087', '803', -1), ('1087', '804', -1), ('1087', '901', -1),
    ('1088', '902', -1), ('1088', '903', -1), ('1088', '904', -1), ('1088', '1001', -1), ('1088', '1002', -1),
    ('1089', '1003', -1), ('1089', '1004', -1), ('1090', '101', -1), ('1090', '102', -1), ('1090', '103', -1),
    ('1090', '104', 6.2), ('1090', '105', 3.8),('1091', '201', 9.1), ('1092', '301', 4.7), ('1092', '302', 8.9),
    ('1092', '303', 5.3), ('1092', '304', 7.2), ('1092', '305', 6.8), ('1093', '401', 2.4), ('1093', '402', 9.5),
    ('1093', '403', 7.1), ('1093', '404', 4.2), ('1093', '405', 8.6), ('1094', '501', 6.7), ('1094', '502', 9.3), 
    ('1094', '503', 2.8), ('1094', '504', 8.4), ('1094', '505', 7.9), ('1095', '601', 3.6), ('1095', '602', 6.5), 
    ('1095', '603', 9.7), ('1095', '604', 4.9), ('1095', '701', 2.1), ('1096', '801', 8.2), ('1096', '802', 4.5), 
    ('1096', '803', 6.1), ('1096', '804', 9.8), ('1096', '901', 3.4), ('1097', '902', 7.6), ('1097', '903', 5.2), 
    ('1097', '904', 8.3), ('1097', '1001', 9.4), ('1097', '1002', 2.3), ('1098', '1003', 5.7), ('1098', '1004', 8.1),
    ('1099', '101', 7.3), ('1099', '102', 4.8), ('1099', '103', 6.9), ('1099', '104', 3.5), ('1099', '105', 9.2),
    ('1100', '201', 7.8), ('1100', '202', 5.6), ('1100', '203', 2.9), ('1100', '204', 8.7), ('1100', '205', 4.3),
    ('1101', '301', 6.3), ('1101', '302', 9.6), ('1101', '303', 7.4), ('1101', '304', 3.7), ('1101', '305', 5.9),
    ('1102', '401', 8.5), ('1102', '402', 2.6), ('1102', '403', 4.6), ('1102', '404', 7.7), ('1102', '405', 6.4),
    ('1103', '501', 3.2), ('1103', '502', 9.9), ('1103', '503', 6.6), ('1103', '504', 4.4), ('1103', '505', 7.5);

CREATE VIEW OV_professors AS
select *
from employee JOIN professor ON ssn=profID;

CREATE VIEW OV_AuxiliaryStaff AS
select *
from employee JOIN auxiliary_staff ON ssn=EmployeeID;

CREATE VIEW OV_CourseInfo AS
select *
from course c Left JOIN (
	Select CourseID AS CourseID_ca, AVG(CASE WHEN Grade != -1 THEN Grade END) AS AverageGrade
	FROM Attends
    GROUP BY CourseID
) AS ca ON c.CourseID = ca.CourseId_ca
LEFT JOIN (
	Select CourseID AS CourseID_t,COUNT(*) AS Amount_t
    FROM teaches
    GROUP BY CourseID
) AS t ON c.CourseID = t. CourseID_t
LEFT JOIN (
	Select CourseID AS CourseID_s, Count(*) AS Amount_s
    FROM Attends
    GROUP BY CourseID
) AS s ON c.CourseID = s.CourseId_s;

Create VIEW ov_students AS
SELECT *
FROM student s
LEFT JOIN (
    SELECT StudentID AS StudentID_sa, AVG(CASE WHEN Grade != -1 THEN Grade END) AS AverageGrade
    FROM Attends
    GROUP BY StudentID
) AS sa ON s.StudentID = sa.StudentId_sa
LEFT JOIN (
	SELECT StudentID AS StudentID_am, Count(*) AS CourseAmount
    FROM Attends
    GROUP BY StudentID
) AS am ON s.StudentId = am.StudentID_am