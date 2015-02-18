package com.ngstudio.organaizer.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "Enumeration")
public class Enumeration extends Model {

    @Column(name="Name")
    public String name;

    @Column(name="Parent")
    public Enumeration parent;

    @Column(name="IsMainRoot")
    private int isMainRoot;

    @Column(name="IsAchieved")
    public int isAchieved;

    public Enumeration() {
        super();
        isMainRoot = 0;
    }

    public boolean isAchieved () {
        return isAchieved !=0;
    }
    public void setAchievedState (boolean state) { isAchieved = state ? 1:0; }

    public static Enumeration createMainRootParent() {
        Enumeration e = new Enumeration();
        e.isMainRoot = 1;
        return e;
    }

    public static List<Enumeration> getAll() {
        return new Select()
                .from(Enumeration.class)
                .execute();
    }

    public static List<Enumeration> getAll(Enumeration parent) {
        return new Select()
                .from(Enumeration.class)
                .where("Parent = ?", parent.getId())
                .orderBy("Name ASC")
                .execute();
    }

    public static List<Enumeration> getAllMainRoot() {
        return new Select()
                .from(Enumeration.class)
                .where("IsMainRoot = ?", 1)
                .execute();
    }

    public static void debugAddListMainRoot() {

        for(int i=0; i<4; i++) {
            Enumeration enumeration = Enumeration.createMainRootParent();
            enumeration.name = "nm "+i;
            enumeration.save();
        }

    }

}
