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

INSERT INTO applications VALUES ('Aaron', 'Downward', 'Sample App', 'This is a filler for the database', 'github.com/mysampleapp', '1.0.0', '2020-08-17 17:10:50');