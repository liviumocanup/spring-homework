CREATE TABLE employee
(
    id            NUMBER(6) PRIMARY KEY,
    first_name    VARCHAR2(20) NOT NULL,
    last_name     VARCHAR2(25) NOT NULL,
    department    NUMBER(4) REFERENCES department (id),
    email         VARCHAR2(25) UNIQUE NOT NULL,
    phone_number  VARCHAR2(20) UNIQUE NOT NULL,
    salary        NUMBER(8, 2) CHECK (salary >= 1.0)
);

CREATE SEQUENCE employee_seq INCREMENT BY 1 MINVALUE 1 MAXVALUE 99999 NOCYCLE;

CREATE OR REPLACE TRIGGER employee_trigger
    BEFORE INSERT ON employee
    FOR EACH ROW

BEGIN
    SELECT employee_seq.NEXTVAL
    INTO   :new.id
    FROM   dual;
END;

COMMENT ON TABLE employee IS 'employee table. References with department.';
COMMENT ON COLUMN employee.id IS 'Primary key of employees table.';
COMMENT ON COLUMN employee.first_name IS 'First name of the employee. A not null column.';
COMMENT ON COLUMN employee.last_name IS 'Last name of the employee. A not null column.';
COMMENT ON COLUMN employee.department IS 'Department id where employee works; foreign key to id column of the department table';
COMMENT ON COLUMN employee.email IS 'Email of the employee.';
COMMENT ON COLUMN employee.phone_number IS 'Phone number of the employee.';
COMMENT ON COLUMN employee.salary IS 'Monthly salary of the employee. Must be greater than 1.0';