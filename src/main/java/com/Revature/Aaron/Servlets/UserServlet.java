package com.Revature.Aaron.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Revature.Aaron.Database.DatabaseAccess;
import com.Revature.Aaron.Objects.Application;

public class UserServlet extends HttpServlet {  
    
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        HttpSession session = req.getSession();

        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("userRole");
        @SuppressWarnings("unchecked")
        HashMap<String, Application> downloadedApps = (HashMap<String, Application>) session.getAttribute("downloadedApps");
        if (downloadedApps == null) {
            downloadedApps = DatabaseAccess.getUserSpecificAppsFromDB(username, false);
            session.setAttribute("downloadedApps", downloadedApps);
        }
        @SuppressWarnings("unchecked")
        HashMap<String, Application> availableApps = (HashMap<String, Application>) session.getAttribute("availableApps");
        if (availableApps == null) {
            availableApps = DatabaseAccess.getUserSpecificAppsFromDB(username, true);
            session.setAttribute("availableApps", availableApps);
        }
        String form;

        out.println("<h1>User Tools</h1>");
        if (role.equals("dev")) {
            out.println("<p><a href=\"developer\">Go to developer tools</a></p>");
        }
        if (downloadedApps.size() > 0) {
            out.println("<h2>Currently downloaded applications</h2>");
            for (Application app : downloadedApps.values()) {
                form = appFormFormatter(app.getName(), app.getVersion(), app.getAuthorUsername(), app.getGithubURL(), app.getDescription(), app.getVersionDate(), "downloaded");
                out.println(form);
            }            
        }
        if (availableApps.size() > 0) {
            out.println("<h2>Applications available on server</h2>");
            for (Application app : availableApps.values()) {
                form = appFormFormatter(app.getName(), app.getVersion(), app.getAuthorUsername(), app.getGithubURL(), app.getDescription(), app.getVersionDate(), "available");
                out.println(form);
            }   
        } else {
            out.println("<h2>No applications available for you to download from the server</h2>");
        }
        out.close();
    }

    private static String appFormFormatter(String appName, String appVersion, String appAuthorUsername, String appURL, String appDescription, LocalDateTime appVersionDate, String context) {
        String form = "<form action = \"download\" method = \"POST\">\n";
        form += appName + " " + appVersion;
        if (context.equals("downloaded")) {
            form += "<input type = \"submit\" value = \"manual update/download again\" name = \"updateButton\" id = \"updateButton\"/>\n";
        } else {
            form += "<input type = \"submit\" value = \"download\" name = \"downloadButton\" id = \"downloadButton\"/>\n";
        }
        form += "<input type = \"hidden\" id = \"appName\" name = \"appName\" value = \"" + appName + "\" />\n";
        form += "<input type = \"hidden\" id = \"appAuthorUsername\" name = \"appAuthorUsername\" value = \"" + appAuthorUsername + "\"/>\n";
        form += "<input type = \"hidden\" id = \"githubURL\" name = \"githubURL\" value = \"" + appURL + "\"/>\n";
        form += "</form>\n";
        form += "<p>" + appDescription + "</p>\n";
        form += "<p>By: " + appAuthorUsername + "</p>\n";
        form += "<p>Last updated: " + appVersionDate.toString() + "</p>\n";

        return form;
    }
}