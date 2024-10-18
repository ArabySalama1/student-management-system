CREATE TABLE course (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_name VARCHAR(100),
    description VARCHAR(255),
    start_date DATE,
    end_date DATE
);