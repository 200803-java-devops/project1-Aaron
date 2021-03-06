CREATE TABLE applications (
    application_name VARCHAR,
    author_username VARCHAR,
    author_first_name VARCHAR,
    author_last_name VARCHAR,
    app_description VARCHAR,
    app_url VARCHAR,
    app_version VARCHAR,
    app_version_date TIMESTAMP,
    app_jar_file_name VARCHAR,
    PRIMARY KEY (application_name, author_username)
);

INSERT INTO applications VALUES ('Project0-Aaron','aadown', 'Aaron', 'Downward', 'CLalendar - a slim CLI calendar and scheduling app', 'https://github.com/200803-java-devops/project0-Aaron.git', '1.0.0', '2020-08-17 17:10:50', 'project0-aaron-1.0.0.jar');
INSERT INTO applications VALUES ('Project1-Aaron','aadown', 'Aaron', 'Downward', 'AADUT - An Automated Application Development and Update Tool', 'https://github.com/200803-java-devops/project1-Aaron.git', '1.0.0', '2020-08-17 17:10:50', 'project1-aaron-0.1.0-jar-with-dependencies.jar');
INSERT INTO applications VALUES ('http-server','mehrab', 'Mehrab', 'Rahman', 'a simple http server', 'https://github.com/200803-java-devops/http-server.git', '1.0.0', '2020-08-17 17:10:50', 'http-server-1.0.0.jar');

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

CREATE TABLE user_downloaded_applications (
    username VARCHAR,
    application_name VARCHAR,
    application_author_username VARCHAR,
    app_version_date TIMESTAMP,
    user_OS VARCHAR,
    user_application_final_directory VARCHAR,
    app_auto_update BOOLEAN,
    PRIMARY KEY (username, application_name, application_author_username)
);

INSERT INTO user_downloaded_applications VALUES ('user', 'Project0-Aaron', 'aadown', '2020-08-17 17:10:50', 'windows', 'C:\Users\downw\Practice', 'FALSE');
INSERT INTO user_downloaded_applications VALUES ('aadown', 'Project0-Aaron', 'aadown', '2020-08-17 17:10:50', 'windows', 'C:\Users\downw\Practice', 'FALSE');
INSERT INTO user_downloaded_applications VALUES ('aadown', 'Project1-Aaron', 'aadown', '2020-08-17 17:10:50', 'windows', 'C:\Users\downw\Practice', 'FALSE');
