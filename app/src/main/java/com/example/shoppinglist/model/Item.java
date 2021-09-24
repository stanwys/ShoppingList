package com.example.shoppinglist.model;

public class Item {

    private int id;
    private int projectId;
    private String name;
    private int completed;

    public Item(int projectId, String name){
        this.id = -1;
        this.projectId = projectId;
        this.name = name;
        this.completed = 0;
    }

    public Item(int id, int projectId, String name, int completed){
        this.id = id;
        this.projectId = projectId;
        this.name = name;
        this.completed = completed;
    }

    public void changeCompletedState(){
        this.completed = 1 - this.completed;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
