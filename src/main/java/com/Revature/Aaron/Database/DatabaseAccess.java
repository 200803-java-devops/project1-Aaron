package com.Revature.Aaron.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.Revature.Aaron.Objects.Application;

public class DatabaseAccess {
    
    public static boolean addApplicationToDB(String firstName, String lastName, String appName, String appDescription, String appURL, String version, LocalDateTime versionDate) {

        String applicationInsert = "INSERT INTO applications VALUES(?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement statement;
        try {
            statement = ConnectionUtil.getConnection().prepareStatement(applicationInsert);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, appName);
            statement.setString(4, appDescription);
            statement.setString(5, appURL);
            statement.setString(6, version);
            statement.setTimestamp(7, Timestamp.valueOf(versionDate));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static ArrayList<Application> getApplicationsFromDB() {
        ArrayList<Application> applications = new ArrayList<Application>();
        String query = "SELECT * FROM applications";
        PreparedStatement statement;
        ResultSet rs = null;
        try {
            statement = ConnectionUtil.getConnection().prepareStatement(query);
            rs = statement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        String firstName;
        String lastName;
        String appName;
        String appDescription;
        String appURL;
        String appVersion;
        LocalDateTime appVersionDate;
        Application app;
        try {
            while (rs.next()) {
                firstName = rs.getString("author_first_name");
                lastName = rs.getString("author_last_name");
                appName = rs.getString("application_name");
                appDescription = rs.getString("app_description");
                appURL = rs.getString("app_url");
                appVersion = rs.getString("app_version");
                appVersionDate = rs.getTimestamp("app_version_date").toLocalDateTime();
                app = new Application(firstName, lastName, appName, appDescription, appURL, appVersion, appVersionDate);
                applications.add(app);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return applications;
    }
}