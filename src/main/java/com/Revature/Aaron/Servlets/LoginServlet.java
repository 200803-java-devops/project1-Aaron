package com.Revature.Aaron.Servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Revature.Aaron.Database.DatabaseAccess;

public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("Redirected to login page");
        RequestDispatcher rd = req.getRequestDispatcher("index.html");
        rd.include(req, resp);
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        session.invalidate();
        
        resp.setContentType("text/html");  
        PrintWriter out = resp.getWriter();  
          
        String username = req.getParameter("username");  
        String pass = req.getParameter("userpass"); 
        RequestDispatcher rd;
        String firstName;
        String lastName;
        String email;
        String role;

        if(req.getParameter("newUser") != null) {
            firstName = req.getParameter("firstName");
            lastName = req.getParameter("lastName");
            email = req.getParameter("email");
            String userCheck = req.getParameter("user");
            role = (userCheck != null) ? "USER" : "DEV";

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
            String[] userInfo = DatabaseAccess.getUserInfoFromDB(username);
            firstName = userInfo[0];
            lastName = userInfo[1];
            email = userInfo[2];
            role = userInfo[3].toLowerCase();
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

            session = req.getSession();
            session.setAttribute("username", username);
            session.setAttribute("userFirstName", firstName);
            session.setAttribute("userLastName", lastName);
            session.setAttribute("userEmail", email);
            session.setAttribute("userRole", role);

            out.println("<p>Welcome " + username + "!</p>");
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