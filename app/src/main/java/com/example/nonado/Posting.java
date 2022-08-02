package com.example.nonado;

public class Posting {
    private String postName;
    private String writer;
    public Posting(String postName){
        this.postName = postName;
    }
    public Posting(String postName, String writer){
        this.postName = postName;
        this.writer = writer;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }
}
