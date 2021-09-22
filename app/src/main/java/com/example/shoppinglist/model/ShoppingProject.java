package com.example.shoppinglist.model;

public class ShoppingProject implements Comparable<ShoppingProject> {

    private int id;
    private int date;
    private String name;

    public ShoppingProject(String name){
        this.id = -1;
        this.date = -1;
        this.name = name;
    }

    @Override
    public int compareTo(ShoppingProject sp){
        return Integer.compare(this.date, sp.getDate());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
