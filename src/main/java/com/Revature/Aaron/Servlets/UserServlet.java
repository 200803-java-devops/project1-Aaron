package com.Revature.Aaron.Servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
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

        String mainDirectory = "C:/Users/downw/OneDrive/Desktop/practice/";
        String projectDir = mainDirectory + "gitProjects/";
        String zipDir = mainDirectory + "projectZips/";
        String gitURL = req.getParameter("githubURL");
        
        String test = Commands.executeCommand("git clone " + gitURL, projectDir);
        int dotGitIndex = gitURL.indexOf(".git");
        int lastSlashIndex = gitURL.lastIndexOf("/");
        String projectName = gitURL.substring(lastSlashIndex + 1, dotGitIndex);
        String test2 = Commands.executeCommand("mvn clean package", projectDir + projectName + "/");

        String fileName = projectName + ".zip";
        String test3 = Commands.executeCommand("jar -cMf " + fileName + " " + projectName + "/", projectDir);
        String test4 = Commands.executeCommand("mv " + fileName + " ../projectZips/", projectDir);

        File file = new File(zipDir + "/" + projectName + ".zip");
        ServletContext ctx = getServletContext();
		InputStream fis = new FileInputStream(file);
		String mimeType = ctx.getMimeType(file.getAbsolutePath());
		resp.setContentType(mimeType != null? mimeType:"application/octet-stream");
		resp.setContentLength((int) file.length());
		resp.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		
		ServletOutputStream os = resp.getOutputStream();
		byte[] bufferData = new byte[1024];
		int read=0;
		while((read = fis.read(bufferData))!= -1){
			os.write(bufferData, 0, read);
		}
		os.flush();
		os.close();
		fis.close();
    }
}