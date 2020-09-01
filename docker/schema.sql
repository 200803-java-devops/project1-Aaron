CREATE TABLE applications (
    application_name VARCHAR,
    author_username VARCHAR,
    author_first_name VARCHAR,
    author_last_name VARCHAR,
    app_description VARCHAR,
    app_url VARCHAR,
    app_version VARCHAR,
    app_version_date TIMESTAMP,
    PRIMARY KEY (application_name, author_username)
);

INSERT INTO applications VALUES ('Project0-Aaron','aadown', 'Aaron', 'Downward', 'CLalendar - a slim CLI calendar and scheduling app', 'https://github.com/200803-java-devops/project0-Aaron.git', '1.0.0', '2020-08-17 17:10:50');
INSERT INTO applications VALUES ('Project1-Aaron','aadown', 'Aaron', 'Downward', 'AADUT - An Automated Application Development and Update Tool', 'https://github.com/200803-java-devops/project1-Aaron.git', '1.0.0', '2020-08-17 17:10:50');
INSERT INTO applications VALUES ('http-server','mehrab', 'Mehrab', 'Rahman', 'a simple http server', 'https://github.com/200803-java-devops/http-server.git', '1.0.0', '2020-08-17 17:10:50');

CREATE TABLE users (
    username VARCHAR,
    user_password VARCHAR,
    first_name VARCHAR,
    last_name VARCHAR,
    email VARCHAR,
    user_role VARCHAR,
    PRIMARY KEY (username)
);

INSERT INTO users VALUES ('user', '1234', 'Test', 'User', 'downward150@gmail.com', 'USER');
INSERT INTO users VALUES ('dev', '1234', 'Test', 'Developer', 'downward150@gmail.com', 'DEV');
INSERT INTO users VALUES ('manager', '1234', 'Test', 'Manager', 'downward150@gmail.com', 'MANAGER');
INSERT INTO users VALUES ('aadown', '1234', 'Aaron', 'Downward', 'downward150@gmail.com', 'DEV');
INSERT INTO users VALUES ('mehrab', '1234', 'Mehrab', 'Rahman', 'downward150@gmail.com', 'DEV');
