package com.snw.schema.exporter.runner;

public class ProjectModel {

    private final Class mainClass;

    private final Class[] allClasses;

    public ProjectModel(Class mainClass, Class[] allClasses) {
        this.mainClass = mainClass;
        this.allClasses = allClasses;
    }

    public Class getMainClass() {
        return mainClass;
    }

    public Class[] getAllClasses() {
        return allClasses;
    }
}
