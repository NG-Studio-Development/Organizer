package com.ngstudio.organaizer.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Calendar;
import java.util.List;

@Table(name = "Task")
public class Task extends Model {

    public static final int PRIORITY_LOW = 0;
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_URGENTLY = 2;
    public static final String TASK_ID = "task_id";

    @Column(name="Name")
    public String naming;

    @Column(name="Description")
    public String description;

    @Column(name="Priority")
    public int priority;

    @Column(name="Date")
    public long dateUnix;

    public static List<Task> getAll() {
        return new Select()
                .from(Task.class)
                .execute();
    }

    public boolean isDeadLine() { return Calendar.getInstance().getTimeInMillis() >= dateUnix; }

    /*public static Task createTaskById(long id) {
        return new Select().from(Task.class).where("Id = ?", id).executeSingle();
    }*/
}
