package org.zeroturnaround.javarebel.maven.model;

public class Module {

    private String name;

    private int depth;

    public Module(){}

    public Module(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
