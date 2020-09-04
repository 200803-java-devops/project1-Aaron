package com.Revature.Aaron.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Revature.Aaron.Database.DatabaseAccess;
import com.Revature.Aaron.Objects.Application;
import com.Revature.Aaron.Utils.MySessionUtils;

public class DeveloperServlet extends HttpServlet {
       
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if (session != null) {
            doPost(req, resp);
        } else {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("Redirected to login page");
        RequestDispatcher rd = req.getRequestDispatcher("index.html");
        rd.include(req, resp);
        out.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        HttpSession session = req.getSession();

        String username = (String) session.getAttribute("username");
        @SuppressWarnings("unchecked")
        HashMap<String, Application> applications = (HashMap<String, Application>) session.getAttribute("contributedApps");
        if (applications == null) {
            applications = DatabaseAccess.applicationsByUsernameFromDB(username);
            session.setAttribute("contributedApps", applications);
        }
        String firstName = (String) session.getAttribute("userFirstName");
        String lastName = (String) session.getAttribute("userLastName");
        String role = (String) session.getAttribute("userRole");
        String appName;
        String appVersion;
        LocalDateTime updateDate;
        String appJarFileName;
        String cancelButton = req.getParameter("cancelButton");
        String editType = req.getParameter("editType");

        if (editType != null) {
            if (cancelButton == null) {
                appName = req.getParameter("appName");
                String appDescription = req.getParameter("appDescription");
                appVersion = req.getParameter("version");
                String appURL = req.getParameter("appURL");
                appJarFileName = req.getParameter("appJarFileName");
                Boolean deleteSuccess = true;
                Boolean updateSuccess = false;
                Boolean serverAddSuccess = ApplicationEdit.cloneAndPackageProject(appName, appURL, username, out);
                if (!serverAddSuccess) {
                    out.println("<p>Problem pulling project from GitHub and packaging for distribution. Contact server manager for help.</p>");
                } else {
                    if (editType.equals("edit")) {
                        String oldAppName = req.getParameter("oldAppName");
                        deleteSuccess = DatabaseAccess.deleteApplicationFromDB(oldAppName, username);
                    }
                    if (deleteSuccess) {
                        updateSuccess = DatabaseAccess.addApplicationToDB(appName, username, firstName, lastName, appDescription, appURL, appVersion, appJarFileName);
                    }
                    if (editType.equals("edit")) {
                        if (updateSuccess) {
                            out.println("<p>Successfully updated your application on the server</p>");
                            MySessionUtils.updateSessionAppMaps(session);
                            applications = (HashMap<String, Application>) session.getAttribute("contributedApps");
                        } else {
                            out.println("<p>Unable to update your application on the server. Contact server manager if problem persists</p>");
                        }
                    } else if (editType.equals("add")) {
                        if (updateSuccess) {
                            out.println("<p>Successfully added your application to the server</p>");
                            MySessionUtils.updateSessionAppMaps(session);
                            applications = (HashMap<String, Application>) session.getAttribute("contributedApps");
                        } else {
                            out.println("<p>Unable to add your application to the server. Contact server manager if problem persists</p>");
                        }
                    }
                }
            }
        }
        out.println("<h1>Developer Tools</h2>");
        out.println("<p><a href=\"user\">Go to User Tools</a></p>");
        if (role.equals("manager")) {
            out.println("<p><a href=\"manager\">Go to Manager Tools</a></p>");
        }
        if (applications.size() > 0) {
            out.println("<h2>Contributed Applications</h2>");
            for (Application app : applications.values()) {
                appName = app.getName();
                appVersion = app.getVersion();
                updateDate = app.getVersionDate();

                out.println("<form action = \"applicationEdit\" method = \"POST\">\n" + 
                    "<pre>" + appName + "\t" + appVersion + "\n" + "Last updated in server: " + updateDate + "<pre>\n" +
                    "<input type = \"submit\" value = \"edit app info\" name = \"editButton\" id = \"editButton\"/>\n" +
                    "<input type = \"submit\" value = \"manual update\" name = \"updateButton\" id = \"updateButton\"/>\n" +
                    "<input type=\"hidden\" id=\"appName\" name=\"appName\" value=\"" + appName + "\"/>\n" +
                    "</form>");
            }
        }
        out.println("<h2>Developer Options</h2>");
        out.println("<form action = \"applicationEdit\" method = \"POST\">\n" +
            "<input type = \"submit\" value = \"add new app\" name = \"addButton\" id = \"addButton\"/>\n" +
            "</form>");
    }
}