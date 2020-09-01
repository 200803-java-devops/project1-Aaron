package com.Revature.Aaron.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Revature.Aaron.Database.DatabaseAccess;

public class SubmittedApplicationServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        resp.setContentType("text/html");

        String firstName = req.getParameter("first_name");
        String lastName = req.getParameter("last_name");
        String appName = req.getParameter("app_name");
        String appDescription = req.getParameter("app_description");
        String appURL = req.getParameter("github_url");
        String version = "1.0";
        LocalDateTime versionDate = LocalDateTime.now();

        String message;
        boolean insertSuccess = DatabaseAccess.addApplicationToDB(firstName, lastName, appName, appDescription, appURL, version, versionDate);
        
        if (insertSuccess) {
            message = "Thank you, " + firstName + " " + lastName + ", for submitting your application: " + appName;
        } else {
            message = "Problem adding your application to the database, contact support for help";
        }

        PrintWriter out = resp.getWriter();
        out.println("<h1>" + message + "</h1>");
    }
}