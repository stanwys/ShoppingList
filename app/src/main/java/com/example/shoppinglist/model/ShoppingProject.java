package com.example.shoppinglist.model;

import java.util.Calendar;
import java.util.Date;

public class ShoppingProject implements Comparable<ShoppingProject> {

    private int id;
    private Date date;
    private String name;
    private int archived;

    public ShoppingProject(String name){
        this.id = -1;
        this.name = name;
        this.date = Calendar.getInstance().getTime();
        this.archived = 0;
    }

    public ShoppingProject(int id, String name, long date, int archived){
        this.id = id;
        this.name = name;
        this.date = new Date(date);
        this.archived = archived;
    }

    @Override
    public int compareTo(ShoppingProject sp){
        return this.date.compareTo(sp.getDate());
    }

    public int getArchived() {
        return archived;
    }

    public void setArchived(int archived) {
        this.archived = archived;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
