package com.Revature.Aaron.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Revature.Aaron.Database.DatabaseAccess;
import com.Revature.Aaron.Objects.Application;

public class ApplicationEdit extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        HttpSession session = req.getSession();

        String addButton = req.getParameter("addButton");
        String editButton = req.getParameter("editButton");
        String appName = req.getParameter("appName");
        if (appName == null) {
            appName = "";
        }
        String username = (String) session.getAttribute("username");
        String appDescription = "";
        String appURL = "";
        String version = "";
        String editType = "add";
        Application app = null;
        RequestDispatcher rd;

        //coming from manual update link or edit link
        if (addButton == null) {
            app = DatabaseAccess.applicationFromDB(username, appName);
            appDescription = app.getDescription();
            appURL = app.getGithubURL();
            version = app.getVersion();
            editType = "edit";
        }
        //coming from manual update link
        if (addButton == null && editButton == null) {
            rd = req.getRequestDispatcher("developer");
            if (app == null) {
                out.println("problem retrieving/finding app in db");
                rd.include(req, resp);
                out.close();
                return;
            }
            Boolean updateSuccess = DatabaseAccess.updateAppTimestampInDB(username, appName);
            if (updateSuccess) {
                out.println("Successful update");
                ArrayList<Application> apps = DatabaseAccess.applicationsByUsernameFromDB(username);
                session.setAttribute("contributedApps", apps);
            } else {
                out.println("Unable to update application");         
            }
            rd.include(req, resp);
            out.close();
            return;
        }

        out.println("<h1>Enter the information for the application you would like to submit</h1>");
        out.println("<form action = \"developer\" method = \"POST\">\n" + 
            "Application Name: <input type = \"text\" name = \"appName\" value = " + appName + ">\n" +
            "<br />\n" +
            "Short Description: <input type = \"text\" name = \"appDescription\" value = " + appDescription + ">\n" +
            "<br />\n" +
            "Version: <input type = \"text\" name = \"version\" value = " + version + ">\n" +
            "<br />\n" +
            "GitHub Repository URL: <input type = \"text\" name = \"appURL\" value = " + appURL + ">\n" +
            "<br />\n" +
            "<input type = \"submit\" value = \"Submit\" name = \"submitButton\" id = \"submitButton\"/>\n" +
            "<input type = \"submit\" value = \"Cancel\" name = \"cancelButton\" id = \"cancelButton\"/>\n" +
            "<input type=\"hidden\" id=\"editType\" name=\"editType\" value=\"" + editType + "\"/>\n" +
            "<input type=\"hidden\" id=\"oldAppName\" name=\"oldAppName\" value=\"" + appName + "\"/>\n" +
            "</form>");
    }
}