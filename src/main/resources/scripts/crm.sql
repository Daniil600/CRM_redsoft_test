
CREATE TABLE IF NOT EXISTS positions
(
    position_id   SERIAL PRIMARY KEY,
    position_name VARCHAR(255) UNIQUE,
    salary        DECIMAL(10, 2)
);

CREATE TABLE IF NOT EXISTS employees
(
    employee_id   SERIAL PRIMARY KEY,
    first_name    VARCHAR(255),
    last_name     VARCHAR(255),
    department_id INTEGER,
    position_id   INTEGER REFERENCES positions (position_id)
);

CREATE TABLE IF NOT EXISTS departments
(
    department_id   SERIAL PRIMARY KEY,
    department_name VARCHAR(255) UNIQUE ,
    head            INTEGER REFERENCES employees (employee_id),
    number          VARCHAR(20),
    email           VARCHAR(255)
);


ALTER TABLE employees ADD FOREIGN KEY (department_id) REFERENCES departments(department_id);