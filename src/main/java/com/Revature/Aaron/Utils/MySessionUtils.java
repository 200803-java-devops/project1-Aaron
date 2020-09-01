package com.Revature.Aaron.Utils;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import com.Revature.Aaron.Database.DatabaseAccess;
import com.Revature.Aaron.Objects.Application;

public class MySessionUtils {

	public static void updateSessionAppMaps(HttpSession session) {
        String username = (String) session.getAttribute("username");
        HashMap<String, Application> contributedApps = DatabaseAccess.applicationsByUsernameFromDB(username);
        session.setAttribute("contributedApps", contributedApps);
        HashMap<String, Application> downloadedApps = DatabaseAccess.getUserSpecificAppsFromDB(username, false);
        session.setAttribute("downloadedApps", downloadedApps);
        HashMap<String, Application> availableApps = DatabaseAccess.getUserSpecificAppsFromDB(username, true);
        session.setAttribute("availableApps", availableApps);
	}
    
}