CREATE TABLE department
(
    id       NUMBER(4),
    name     VARCHAR2(30) NOT NULL,
    location VARCHAR2(30) NOT NULL,

    CONSTRAINT department_id_pk PRIMARY KEY (id)
);

CREATE SEQUENCE department_seq START WITH 1 INCREMENT BY 1 MINVALUE 1 MAXVALUE 99999 NOCYCLE;

CREATE OR REPLACE TRIGGER dept_before_insert
    BEFORE INSERT ON department
    FOR EACH ROW

BEGIN
    SELECT department_seq.NEXTVAL
    INTO   :new.id
    FROM   dual;
END;

COMMENT ON TABLE department IS 'Department table that shows details of department where employees work.';
COMMENT ON COLUMN department.id IS 'Primary key column of department table.';
COMMENT ON COLUMN department.name IS 'A not null column that shows name of a department.';
COMMENT ON COLUMN department.location IS 'Location where a department is located.';