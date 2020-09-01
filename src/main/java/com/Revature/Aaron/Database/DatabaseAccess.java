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

	public static boolean validateLogin(String name, String pass) {
        String query = "SELECT * FROM users WHERE username = ? AND user_password = ?";
        PreparedStatement statement;
        ResultSet rs = null;
        Boolean status = false;
        try {
            statement = ConnectionUtil.getConnection().prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, pass);
            rs = statement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
            return status;
        }

        try {
            status = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return status;
        }

		return status;
    }
    
    private static ResultSet getUserFromDB(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        PreparedStatement statement;
        ResultSet rs = null;
        try {
            statement = ConnectionUtil.getConnection().prepareStatement(query);
            statement.setString(1, username);
            rs = statement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
            return rs;
        }
        return rs;
    }

	public static String getUserRoleFromDB(String username) {
        ResultSet rs = getUserFromDB(username);
        String role = "";
        if (rs == null) {
            return role;
        }
        try {
            if (rs.next()) {
                role = rs.getString("user_role").toLowerCase();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }       
        return role;
	}

	public static boolean checkUserInDB(String username) {
        ResultSet rs = getUserFromDB(username);
        Boolean userExists = false;
        if (rs == null) {
            return userExists;
        }
        try {
            if (rs.next()) {
                userExists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }       
        return userExists;
	}

	public static boolean addUserToDB(String username, String pass, String firstName, String lastName, String email, String role) {
        String userInsert = "INSERT INTO users VALUES(?, ?, ?, ?, ?, ?);";
        PreparedStatement statement;
        try {
            statement = ConnectionUtil.getConnection().prepareStatement(userInsert);
            statement.setString(1, username);
            statement.setString(2, pass);
            statement.setString(3, firstName);
            statement.setString(4, lastName);
            statement.setString(5, email);
            statement.setString(6, role);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}