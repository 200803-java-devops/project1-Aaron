package com.Revature.Aaron.Servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Revature.Aaron.Database.DatabaseAccess;

public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");  
        PrintWriter out = resp.getWriter();  
          
        String username = req.getParameter("username");  
        String pass = req.getParameter("userpass"); 
        RequestDispatcher rd;

        if(req.getParameter("newUser") != null) {
            String firstName = req.getParameter("firstName");
            String lastName = req.getParameter("lastName");
            String email = req.getParameter("email");
            String userCheck = req.getParameter("user");
            String role = (userCheck != null) ? "USER" : "DEV";

            if (DatabaseAccess.checkUserInDB(username)) {
                out.print("That username is already taken, please choose another");
                rd = req.getRequestDispatcher("newUser.html");
                rd.include(req, resp);
                out.close();
                return;
            }

            DatabaseAccess.addUserToDB(username, pass, firstName, lastName, email, role);
        }

        if(DatabaseAccess.validateLogin(username, pass)){  
            String role = DatabaseAccess.getUserRoleFromDB(username);
            switch (role) {
                case "user":
                    rd = req.getRequestDispatcher("user");
                    break;
                case "dev":
                    rd = req.getRequestDispatcher("developer");
                    break;
                case "manager":
                    rd = req.getRequestDispatcher("manager");
                    break;
                default:
                    out.print("Something went wrong retrieving your role");
                    rd = req.getRequestDispatcher("index.html");
                    rd.include(req, resp);
                    out.close();
                    return;
            }  
            rd.forward(req, resp);
        }  
        else{  
            out.print("Sorry username or password error");  
            rd = req.getRequestDispatcher("index.html");  
            rd.include(req, resp);  
        }  
            
        out.close();  
    }
    
}