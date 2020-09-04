package com.Revature.Aaron.Servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ManagerServlet extends HttpServlet {

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
        PrintWriter out = resp.getWriter();
        out.println("<h1>Server Manager Tools</h2>");
        out.println("<p><a href=\"user\">Go to User Tools</a></p>");
        out.println("<p><a href=\"developer\">Go to Developer Tools</a></p>");
        out.close();
    }
}
