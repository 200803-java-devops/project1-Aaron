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

public class DownloadInfo extends HttpServlet {

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
        String appName = req.getParameter("appName");
        String appAuthor = req.getParameter("appAuthorUsername");
        String appURL = req.getParameter("githubURL");
        String appJarFileName = req.getParameter("appJarFileName");
        String updateButton = req.getParameter("updateButton");
        String userOS = "";
        String appFinalDirectory = "";
        String autoUpdate = "";
        String autoUpdateCheck = "";
        String buttonHiddenField = "<input type=\"hidden\" id=\"downloadButton\" name=\"downloadButton\" value=\"downloadButton\"/>\n";
        String[] selected = {"","","",""};

        if (updateButton != null) {
            String[] appInfo = DatabaseAccess.getUserSpecificAppInfoFromDB(username, appName, appAuthor);
            userOS = appInfo[3];
            switch (userOS) {
                case "windows":
                    selected[0] = " selected";
                    break;
                case "mac":
                    selected[1] = " selected";
                    break;
                case "linux":
                    selected[2] = " selected";
                    break;
                case "unix":
                    selected[3] = " selected";
                    break;
            }
            appFinalDirectory = appInfo[4];
            autoUpdate = appInfo[5].toLowerCase();
            if (autoUpdate != null) {
                if (autoUpdate.equals("true")) {
                    autoUpdateCheck = " checked = \"checked\"";
                }
            }
            buttonHiddenField = "<input type=\"hidden\" id=\"updateButton\" name=\"updateButton\" value=\"updateButton\"/>\n";
        }

        out.println("<h1>Enter the configuration information for your " + appName + " download</h1>");
        out.println("<form action = \"download\" method = \"POST\">\n" + 
            "<label for = \"userOS\">Operating System: </label>\n" +
            "<select name = \"userOS\" id = \"userOS\">\n" +
            "<option value = \"windows\"" + selected[0] + ">Windows</option>\n" +
            "<option value = \"mac\"" + selected[1] + ">macOS</option>\n" +
            "<option value = \"linux\"" + selected[2] + ">Linux</option>\n" +
            "<option value = \"unix\"" + selected[3] + ">Other Unix-based OS</option>\n" +
            "</select>\n" +
            "<br />\n" +
            "Desired directory for application install: <input type = \"text\" name = \"appFinalDirectory\" value = " + appFinalDirectory + ">\n" +
            "<br />\n" +
            "Enable auto-update: <input type = \"checkbox\" name = \"autoUpdate\"" + autoUpdateCheck + "/>\n" +
            "<br />\n" +
            "<input type = \"submit\" value = \"Submit\" name = \"submitButton\" id = \"submitButton\"/>\n" +
            "<input type=\"hidden\" id=\"appName\" name=\"appName\" value=\"" + appName + "\"/>\n" +
            "<input type=\"hidden\" id=\"appAuthorUsername\" name=\"appAuthorUsername\" value=\"" + appAuthor + "\"/>\n" +
            "<input type=\"hidden\" id=\"appJarFileName\" name=\"appJarFileName\" value=\"" + appJarFileName + "\"/>\n" +
            "<input type=\"hidden\" id=\"appURL\" name=\"appURL\" value=\"" + appURL + "\"/>\n" +
            buttonHiddenField +
            "</form>");
        out.print("<p><a href=\"user\">Return to User Portal</a></p>");
    }
    
}
