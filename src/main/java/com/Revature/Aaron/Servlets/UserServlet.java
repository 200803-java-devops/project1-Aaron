package com.Revature.Aaron.Servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Revature.Aaron.Database.DatabaseAccess;
import com.Revature.Aaron.Objects.Application;

public class UserServlet extends HttpServlet {  
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        resp.setContentType("text/html");
        ArrayList<Application> apps = DatabaseAccess.getApplicationsFromDB();

        PrintWriter out = resp.getWriter();
        out.println("<h1>Browse the applications list and choose what you want to do with it</h1>");
        String form;

        for (Application app : apps) {
            form = "<form action=\"user\" method=\"GET\">\n";
            form += app.getName() + " " + app.getVersion();
            form += "<input type=\"submit\" value=\"install\" name=\"" + app.getName() + "_install_link\" id=\"" + app.getName() + app.getAuthorLastName() + "_install\" />\n";
            form += "</form>\n";
            form += "<p>" + app.getDescription() + "</p>\n";
            form += "<p>By: " + app.getAuthorFirstName() + " " + app.getAuthorLastName() + "</p>\n";

            out.println(form);
        }
    }
}