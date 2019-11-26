package com.kdh.chart.datatypes;

import java.io.Serializable;

public class ProjectLocation implements Serializable {
    private String location;
    private Project project;
    public ProjectLocation(String location, Project project) {
        this.location = location;
        this.project = project;
    }

    public Project getProject() {
        return project;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
