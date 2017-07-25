package org.zeroturnaround.javarebel.maven.model;

public class Module {

    private String name;

    private boolean subs;

    private int depth;

    public Module(){}

    public Module(String name, boolean subs) {
        this.name = name;
        this.subs = subs;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getSubs() {
        return subs;
    }

    public void setSubs(boolean subs) {
        this.subs = subs;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
