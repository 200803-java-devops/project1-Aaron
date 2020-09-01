package com.Revature.Aaron.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.Revature.Aaron.Objects.Application;

public class DatabaseAccess {
    
    public static boolean addApplicationToDB(String appName, String username, String firstName, String lastName, String appDescription, String appURL, String version) {

        String applicationInsert = "INSERT INTO applications VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement statement;
        try {
            statement = ConnectionUtil.getConnection().prepareStatement(applicationInsert);
            statement.setString(1, appName);
            statement.setString(2, username);
            statement.setString(3, firstName);
            statement.setString(4, lastName);
            statement.setString(5, appDescription);
            statement.setString(6, appURL);
            statement.setString(7, version);
            statement.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static ArrayList<Application> allApplicationsFromDB() {
        ArrayList<Application> applications = getApplicationsFromDB("", null);
        return applications;
    }

    public static ArrayList<Application> applicationsByUsernameFromDB(String username) {
        String condition = "author_username = ?";
        String[] setValues = {username};
        ArrayList<Application> applications = getApplicationsFromDB(condition, setValues);
        return applications;
    }
    
    public static Application applicationFromDB(String username, String appName) {
        String condition = "author_username = ? AND application_name = ?";
        String[] setValues = {username, appName};
        ArrayList<Application> appArrayList = getApplicationsFromDB(condition, setValues);
        Application app = appArrayList.get(0);
        return app;
	}

    private static ArrayList<Application> getApplicationsFromDB(String condition, String[] setValues) {
        ArrayList<Application> applications = new ArrayList<Application>();
        if (condition != null) {
            if (!condition.equals("")) {

            }
        }
        String query = "SELECT * FROM applications WHERE";
        if (condition != null) {
            if (!condition.equals("")) {
                query += " " + condition;
            }
        }
        query += ";";
        PreparedStatement statement;
        ResultSet rs = null;
        try {
            statement = ConnectionUtil.getConnection().prepareStatement(query);
            if (setValues != null) {
                for (int i = 0; i < setValues.length; i++) {
                    statement.setString(i + 1, setValues[i]);
                }
            }   
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

    public static String[] getUserInfoFromDB(String username) {
        String[] userInfo = null;
        ResultSet rs = getUserFromDB(username);
        if (rs == null) {
            return userInfo;
        }
        try {
            if (rs.next()) {
                userInfo = new String[4];
                userInfo[0] = rs.getString("first_name");
                userInfo[1] = rs.getString("last_name");
                userInfo[2] = rs.getString("email");
                userInfo[3] = rs.getString("user_role");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }     
        return userInfo;
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

	public static Boolean updateAppTimestampInDB(String username, String appName) {
        String appUpdate = "UPDATE applications SET app_version_date = ? WHERE application_name = ? AND author_username = ?;";
        PreparedStatement statement;
        try {
            statement = ConnectionUtil.getConnection().prepareStatement(appUpdate);
            statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            statement.setString(2, appName);
            statement.setString(3, username);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}

	public static Boolean deleteApplicationFromDB(String appName, String username) {
        String appDelete = "DELETE FROM applications WHERE application_name = ? AND author_username = ?;";
        PreparedStatement statement;
        try {
            statement = ConnectionUtil.getConnection().prepareStatement(appDelete);
            statement.setString(1, appName);
            statement.setString(2, username);
            statement.executeUpdate();            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
		return true;
	}
}