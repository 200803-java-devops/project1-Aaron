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
import com.Revature.Aaron.Objects.Application;
import com.Revature.Aaron.Utils.Commands;
import com.Revature.Aaron.Utils.MySessionUtils;

public class ApplicationEdit extends HttpServlet {
    
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
        String appJarFileName = "";
        Application app = null;
        RequestDispatcher rd;

        //coming from manual update link or edit link
        if (addButton == null) {
            app = DatabaseAccess.applicationFromDB(username, appName);
            appDescription = app.getDescription();
            appURL = app.getGithubURL();
            version = app.getVersion();
            appJarFileName = app.getJarFileName();
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
            Boolean serverAddSuccess = ApplicationEdit.cloneAndPackageProject(appName, appURL, username, out);
                if (serverAddSuccess) {
                    Boolean updateSuccess = DatabaseAccess.updateAppTimestampInDB(username, appName);
                    if (updateSuccess) {
                        out.println("Successful update");
                        MySessionUtils.updateSessionAppMaps(session);
                    } else {
                        out.println("Unable to update application");         
                    }
                } else {
                    out.println("<p>Problem pulling project from GitHub and packaging for distribution. Contact server manager for help.</p>");
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
            "Compiled project jar file name (your-app.jar): <input type = \"text\" name = \"appJarFileName\" value = " + appJarFileName + ">\n" +
            "<br />\n" +
            "<input type = \"submit\" value = \"Submit\" name = \"submitButton\" id = \"submitButton\"/>\n" +
            "<input type = \"submit\" value = \"Cancel\" name = \"cancelButton\" id = \"cancelButton\"/>\n" +
            "<input type=\"hidden\" id=\"editType\" name=\"editType\" value=\"" + editType + "\"/>\n" +
            "<input type=\"hidden\" id=\"oldAppName\" name=\"oldAppName\" value=\"" + appName + "\"/>\n" +
            "</form>");
    }

    public static boolean cloneAndPackageProject(String appName, String gitURL, String appAuthor, PrintWriter out) {
        
        String projectDir = "app-projects/gitProjects";
        String compileDir = "app-projects/compiledProjects";
        
        String output = Commands.executeCommand("git clone " + gitURL, projectDir);
        if (output == null) {
            out.println("Problem cloning GitHub repository. Contact server manager for help");
            return false;
        }
        int dotGitIndex = gitURL.indexOf(".git");
        int lastSlashIndex = gitURL.lastIndexOf("/");
        String projectName = gitURL.substring(lastSlashIndex + 1, dotGitIndex);
        output = Commands.executeCommand("mv -f " + projectName + "/ ../compiledProjects/", projectDir);
        if (output == null) {
            out.println("Problem moving project. Contact server manager for help");
            return false;
        }
        output = Commands.executeCommand("mvn clean package", compileDir + "/" + projectName + "/");
        if (output == null) {
            out.println("Problem packaging project. Contact server manager for help");
            return false;
        }
        return true;
    }
}