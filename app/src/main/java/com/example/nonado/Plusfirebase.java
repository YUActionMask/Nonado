package com.example.nonado;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;


@IgnoreExtraProperties
public class Plusfirebase {
    public String title , comment;

    public Plusfirebase(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public String getTitle(){
        return title;
    }
    public String getcomment(){
        return comment;
    }
    public Plusfirebase(String title, String comment) {
        this.title = title;
        this.comment = comment;
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