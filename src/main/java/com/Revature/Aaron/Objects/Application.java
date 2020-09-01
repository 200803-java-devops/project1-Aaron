package com.Revature.Aaron.Objects;

import java.time.LocalDateTime;

public class Application {

    private String authorUsername;
    private String authorFirstName;
    private String authorLastName;
    private String name;
    private String description;
    private String githubURL;
    private String version;
    private LocalDateTime versionDate;

    public Application(String authorUsername, String authorFirstName, String authorLastName, String name, String description, String githubURL, String version, LocalDateTime versionDate) {
        this.setAuthorUsername(authorUsername);
        this.setAuthorFirstName(authorFirstName);
        this.setAuthorLastName(authorLastName);
        this.setName(name);
        this.setDescription(description);
        this.setGithubURL(githubURL);
        this.setVersion(version);
        this.setVersionDate(versionDate);
    }

    public LocalDateTime getVersionDate() {
		return versionDate;
	}

	public void setVersionDate(LocalDateTime versionDate) {
		this.versionDate = versionDate;
	}

	public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGithubURL() {
        return githubURL;
    }

    public void setGithubURL(String githubURL) {
        this.githubURL = githubURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

}