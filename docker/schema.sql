CREATE TABLE applications (
    author_first_name VARCHAR,
    author_last_name VARCHAR,
    application_name VARCHAR,
    app_description VARCHAR,
    app_url VARCHAR,
    app_version VARCHAR,
    app_version_date TIMESTAMP,
    PRIMARY KEY (author_last_name, application_name)
);

INSERT INTO applications VALUES ('Aaron', 'Downward', 'Project0-Aaron', 'CLalendar - a slim CLI calendar and scheduling app', 'https://github.com/200803-java-devops/project0-Aaron.git', '1.0.0', '2020-08-17 17:10:50');
INSERT INTO applications VALUES ('Aaron', 'Downward', 'Project1-Aaron', 'AADUT - An Automated Application Development and Update Tool', 'https://github.com/200803-java-devops/project1-Aaron.git', '1.0.0', '2020-08-17 17:10:50');
INSERT INTO applications VALUES ('Mehrab', 'Rahman', 'http-server', 'a simple http server', 'https://github.com/200803-java-devops/http-server.git', '1.0.0', '2020-08-17 17:10:50');