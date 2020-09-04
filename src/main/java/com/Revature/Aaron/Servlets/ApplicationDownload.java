package com.Revature.Aaron.Servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
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
        HttpSession session = req.getSession();
        String updateButton = req.getParameter("updateButton");
        String username = (String) session.getAttribute("username");
        String appAuthor = req.getParameter("appAuthorUsername");
        String appName = req.getParameter("appName");
        String appURL = req.getParameter("appURL");
        int dotGitIndex = appURL.indexOf(".git");
        int lastSlashIndex = appURL.lastIndexOf("/");
        String projectName = appURL.substring(lastSlashIndex + 1, dotGitIndex);
        String appJarFileName = req.getParameter("appJarFileName");
        String userOS = req.getParameter("userOS");
        String appFinalDirectory = req.getParameter("appFinalDirectory");
        String autoUpdate = req.getParameter("autoUpdate");
        boolean autoUpdateBool = autoUpdate.equals("on");

        PrintWriter out;
        RequestDispatcher rd;

        String extraFilesPath = "extraFileProjects/" + appAuthor + "_" + appName + "/";
        String output = Commands.executeCommand("mkdir " + extraFilesPath, "app-projects");
        if (output == null) {
            out = resp.getWriter();
            out.print("Problem creating folder");
            rd = req.getRequestDispatcher("downloadInfo");
            rd.include(req, resp);
            out.close();
            return;
        }
        output = Commands.executeCommand("cp -R " + projectName + "/ ../" + extraFilesPath, "app-projects/compiledProjects");
        if (output == null) {
            out = resp.getWriter();
            out.print("Problem copying project to another directory");
            rd = req.getRequestDispatcher("downloadInfo");
            rd.include(req, resp);
            out.close();
            return;
        }

        extraFilesPath = "app-projects/" + extraFilesPath;
        Boolean success = addUserInstructionFile(appName, appAuthor, extraFilesPath, userOS, appFinalDirectory);
        if (!success) {
            out = resp.getWriter();
            out.print("Problem adding user instruction file");
            rd = req.getRequestDispatcher("downloadInfo");
            rd.include(req, resp);
            out.close();
            return;
        }
        success = addStartupFile(userOS, appFinalDirectory, extraFilesPath, projectName);
        if (!success) {
            out = resp.getWriter();
            out.print("Problem adding startup file to install package");
            rd = req.getRequestDispatcher("downloadInfo");
            rd.include(req, resp);
            out.close();
            return;
        }
        success = addRunFile(userOS, extraFilesPath, appFinalDirectory, appJarFileName);
        if (!success) {
            out = resp.getWriter();
            out.print("Problem adding run file to install package");
            rd = req.getRequestDispatcher("downloadInfo");
            rd.include(req, resp);
            out.close();
            return;
        }
        success = zipProject(appName, username, appAuthor, projectName);
        if (!success) {
            out = resp.getWriter();
            out.print("Problem adding run file to install package");
            rd = req.getRequestDispatcher("downloadInfo");
            rd.include(req, resp);
            out.close();
            return;
        }
        String zipDir = "app-projects/projectZips";

        String fileName = appAuthor + "_" + appName + ".zip";

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
            DatabaseAccess.deleteUserAppFromDB(username, appName, appAuthor);
        }
        DatabaseAccess.addUserAppToDB(username, appName, appAuthor, userOS, appFinalDirectory, autoUpdateBool);

        MySessionUtils.updateSessionAppMaps(session);
    }

    private Boolean zipProject(String appName, String username, String appAuthor, String projectName) {
        String fileName = appAuthor + "_" + appName + ".zip";
        String extraFileDir = "app-projects/extraFileProjects";
        String output = Commands.executeCommand("jar -cMf " + fileName + " " + projectName + "/", extraFileDir);
        if (output == null) {
            return false;
        }
        output = Commands.executeCommand("mv -f " + fileName + " ../projectZips/", extraFileDir);
        if (output == null) {
            return false;
        }
        output = Commands.executeCommand("rm -rf " + appAuthor + "_" + projectName + "/", extraFileDir);
        if (output == null) {
            return false;
        }
        return true;
    }

    private Boolean addRunFile(String userOS, String extraFilesPath, String appFinalDirectory, String appJarFileName) {
        String pathname = extraFilesPath + "/run";
        String extension = (userOS.equals("windows")) ? ".bat" : ".sh";
        pathname += extension;
        String batchText = "@echo off\nstart java -jar " + appFinalDirectory + "\\target\\" + appJarFileName;
        String shellText = "java -jar " + appFinalDirectory + appJarFileName;
        File instructions = new File(pathname);
        try {
            instructions.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        FileWriter myWriter;
        try {
            myWriter = new FileWriter(instructions);
            if (userOS.equals("windows")) {
                myWriter.write(batchText);
            } else {
                myWriter.write(shellText);
            }
            myWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private Boolean addStartupFile(String userOS, String appFinalDirectory, String extraFilesPath, String projectName) {
        String pathname = extraFilesPath + "/startup";
        String extension = (userOS.equals("windows")) ? ".bat" : ".sh";
        pathname += extension;
        appFinalDirectory += projectName;
        String batchText = "@echo off\ntitle startup batch script\nif not exist \"" + appFinalDirectory + "\" mkdir \"" + appFinalDirectory + "\"\nmove \"" + projectName + "/\" \"" + appFinalDirectory + "\"";
        String shellText = "mkdir " + appFinalDirectory + "\nmv * .[^.]* " + appFinalDirectory;
        File instructions = new File(pathname);
        try {
            instructions.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        FileWriter myWriter;
        try {
            myWriter = new FileWriter(instructions);
            if (userOS.equals("windows")) {
                myWriter.write(batchText);
            } else {
                myWriter.write(shellText);
            }
            myWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private Boolean addUserInstructionFile(String appName, String appAuthor, String extraFilesPath, String userOS, String appFinalDirectory) {
        String pathname = extraFilesPath + "User_Instructions.txt";
        String extension = (userOS.equals("windows")) ? ".bat" : ".sh";
        File instructions = new File(pathname);
        try {
            instructions.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        FileWriter myWriter;
        try {
            myWriter = new FileWriter(instructions);
            myWriter.write(appName + "\nBy: " + appAuthor + "\n\nUser Instructions\n\n" + "Extract the downloaded "
                + appAuthor + "_" + appName + ".zip file anywhere\n" + "Open the startup" + extension
                + " file, which will move the application folder to your designated application directory:\n"
                + appFinalDirectory + "\n" + "To start the application, open the run" + extension + " file.\n"
                + "If you'd like, you can create a shortcut of the run" + extension
                + "file, and place the shortcut wherever you'd like.");
            myWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }
}