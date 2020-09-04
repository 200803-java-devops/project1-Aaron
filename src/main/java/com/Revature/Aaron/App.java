package com.Revature.Aaron;

import java.io.File;

import com.Revature.Aaron.Servlets.DeveloperServlet;
import com.Revature.Aaron.Servlets.DownloadInfo;
import com.Revature.Aaron.Servlets.LoginServlet;
import com.Revature.Aaron.Servlets.ManagerServlet;
import com.Revature.Aaron.Servlets.ApplicationDownload;
import com.Revature.Aaron.Servlets.ApplicationEdit;
import com.Revature.Aaron.Servlets.UserServlet;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class App {
    public static void main(String[] args) {
        Tomcat server = new Tomcat();
        server.setBaseDir(new File("target/tomcat/").getAbsolutePath());
        server.setPort(8080);
        server.getConnector();
        server.addWebapp("/aadut", new File("src/main/resources/").getAbsolutePath());
        server.addServlet("/aadut", "Login validation", new LoginServlet()).addMapping("/login");
        server.addServlet("/aadut", "Manager entry point", new ManagerServlet()).addMapping("/manager");
        server.addServlet("/aadut", "Developer entry point", new DeveloperServlet()).addMapping("/developer");
        server.addServlet("/aadut", "User entry point", new UserServlet()).addMapping("/user");
        server.addServlet("/aadut", "Developer submitted application", new ApplicationEdit()).addMapping("/applicationEdit");
        server.addServlet("/aadut", "User application download", new ApplicationDownload()).addMapping("/download");
        server.addServlet("/aadut", "User application download configuration form", new DownloadInfo()).addMapping("/downloadInfo");
        try {
            server.start();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }
}
