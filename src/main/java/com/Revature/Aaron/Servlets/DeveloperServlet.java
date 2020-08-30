package com.Revature.Aaron.Servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeveloperServlet extends HttpServlet {
       
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        resp.setContentType("text/html");

        PrintWriter out = resp.getWriter();
        out.println("<h1>Enter the information for the application you would like to submit</h1>");
        out.println("<form action = \"developer/submitted\" method = \"POST\">\n" + 
                "First Name: <input type = \"text\" name = \"first_name\">\n" +
                "<br />\n" + 
                "Last Name: <input type = \"text\" name = \"last_name\" />\n" +
                "<br />\n" +
                "Application Name: <input type = \"text\" name = \"app_name\">\n" +
                "<br />\n" +
                "Short Description: <input type = \"text\" name = \"app_description\">\n" +
                "<br />\n" +
                "GitHub Repository URL: <input type = \"text\" name = \"github_url\">\n" +
                "<br />\n" +
                "<input type = \"submit\" value = \"Submit\" />\n" +
                "</form>");
    }
}