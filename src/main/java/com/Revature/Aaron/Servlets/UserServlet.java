package com.Revature.Aaron.Servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Revature.Aaron.Database.DatabaseAccess;
import com.Revature.Aaron.Objects.Application;
import com.Revature.Aaron.Processes.Commands;

public class UserServlet extends HttpServlet {  
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html");
        ArrayList<Application> apps = DatabaseAccess.getApplicationsFromDB();

        PrintWriter out = resp.getWriter();
        out.println("<h1>Browse the applications list and choose what you want to do with it</h1>");
        String form;

        for (Application app : apps) {
            form = "<form action=\"user\" method=\"POST\">\n";
            form += app.getName() + " " + app.getVersion();
            form += "<input type=\"submit\" value=\"download\" name=\"" + app.getName() + "_install_link\" id=\"" + app.getName() + app.getAuthorLastName() + "_install\" />\n";
            form += "<input type=\"hidden\" id=\"appName\" name=\"appName\" value=\"" + app.getName() + "\" />\n";
            form += "<input type=\"hidden\" id=\"authorLastName\" name=\"authorLastName\" value=\"" + app.getAuthorLastName() + "\"/>\n";
            form += "<input type=\"hidden\" id=\"githubURL\" name=\"githubURL\" value=\"" + app.getGithubURL() + "\"/>\n";
            form += "</form>\n";
            form += "<p>" + app.getDescription() + "</p>\n";
            form += "<p>By: " + app.getAuthorFirstName() + " " + app.getAuthorLastName() + "</p>\n";
            form += "<p>Last updated: " + app.getVersionDate().toString() + "</p>\n";

            out.println(form);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Enumeration<String> en = req.getParameterNames();
        while(en.hasMoreElements()) {
            String param = en.nextElement();
            System.out.println(param);
        }
        String directory = "C:/Users/downw/OneDrive/Desktop/practice/";
        String gitURL = req.getParameter("githubURL");
        String test = Commands.executeCommand("git clone " + gitURL, directory);
        int dotGitIndex = gitURL.indexOf(".git");
        int lastSlashIndex = gitURL.lastIndexOf("/");
        String projectName = gitURL.substring(lastSlashIndex + 1, dotGitIndex);
        String test2 = Commands.executeCommand("mvn clean package", directory + projectName + "/");
        System.out.println("hello");
        System.out.println(test);
        System.out.println(test2);
        doGet(req, resp);
    }
}