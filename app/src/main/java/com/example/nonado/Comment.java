package com.example.nonado;

public class Comment {
    private String id;
    private String comment;
    private String date;

    public Comment(String id, String comment, String date){
        this.id = id;
        this.comment = comment;
        this.date = date;
    }

    public String getId(){
        return id;
    }

    public String getComment(){
        return comment;
    }

    public String getDate(){
        return date;
    }
}
