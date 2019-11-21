package com.kdh.chart.datatypes;

import java.io.Serializable;

/**
 * Project luu tru thong tin cac bieu do
 */
public class Project implements Serializable {
    private String projectName;
    private String modifiedTime;

    public Project(String projectName, String modifiedTime) {
        this.projectName = projectName;
        this.modifiedTime = modifiedTime;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getModifiedTime() {
        return modifiedTime;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setModifiedTime(String modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
}
