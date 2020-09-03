package com.Revature.Aaron.Servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Revature.Aaron.Database.DatabaseAccess;
import com.Revature.Aaron.Utils.Commands;
import com.Revature.Aaron.Utils.MySessionUtils;

public class ApplicationDownload extends HttpServlet {

    private static final long serialVersionUID = 1L;
    public static final String projectDirectory = new File(".").getAbsolutePath();
    public static final String mainDirectory = projectDirectory.substring(0, projectDirectory.lastIndexOf("project1-Aaron")) + "app-projects\\";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher rd;
        PrintWriter out;
        HttpSession session = req.getSession();
        String updateButton = req.getParameter("updateButton");
        String downloadButton = req.getParameter("downloadButton");
        String username = (String) session.getAttribute("username");
        String appAuthor = req.getParameter("appAuthorUsername");
        String appName = req.getParameter("appName");
        String gitURL = req.getParameter("githubURL");
        if (req.getParameter("githubURL") == null) {
            out = resp.getWriter();
            out.println("No URL provided to access the application. Contact server manager for help");
            rd = req.getRequestDispatcher("user");
            rd.include(req, resp);
            out.close();
            return;
        }
        String projectDir = mainDirectory + "gitProjects/";
        String zipDir = mainDirectory + "projectZips/";
        
        Commands.executeCommand("git clone " + gitURL, projectDir);
        int dotGitIndex = gitURL.indexOf(".git");
        int lastSlashIndex = gitURL.lastIndexOf("/");
        String projectName = gitURL.substring(lastSlashIndex + 1, dotGitIndex);
        Commands.executeCommand("mvn clean package", projectDir + projectName + "/");

        String fileName = appAuthor + "_" + projectName + ".zip";
        Commands.executeCommand("jar -cMf " + fileName + " " + projectName + "/", projectDir);
        Commands.executeCommand("mv -f " + fileName + " ../projectZips/", projectDir);

        File file = new File(zipDir + "/" + fileName);
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

        if (updateButton != null) {
            DatabaseAccess.updateTimestampInUserApp(username, appName, appAuthor);
        } else if (downloadButton != null) {
            DatabaseAccess.addUserAppToDB(username, appName, appAuthor);
        }
        MySessionUtils.updateSessionAppMaps(session);
    }
   
}