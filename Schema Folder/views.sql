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