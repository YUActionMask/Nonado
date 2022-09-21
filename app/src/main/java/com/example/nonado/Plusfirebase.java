package com.example.nonado;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;


@IgnoreExtraProperties
public class Plusfirebase {
    public String title , comment, name, location, date;

    public Plusfirebase(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public String getTitle(){
        return title;
    }
    public String getcomment(){
        return comment;
    }
    public String getname(){
        return name;
    }
    public Plusfirebase(String title, String comment, String name, String location) {
        this.title = title;
        this.comment = comment;
        this.name = name;
        this.location = location;
    }
    public Plusfirebase(String name, String comment, String date) {
        this.name = name;
        this.comment = comment;
        this.date = date;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("comment", comment);
        return result;
    }
}
//