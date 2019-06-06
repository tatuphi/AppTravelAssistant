package com.example.mapdemo;

public class Review{

    private String commentName;
    private String iconName;
    private String comment;
    private Float rate;

    public Review(String commentName, String iconName, String comment, Float rate) {
        this.commentName= commentName;
        this.iconName= iconName;
        this.comment= comment;
        this.rate = rate;
    }

    public String getComment() { return comment; }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentName() {
        return commentName;
    }

    public void setCommentName(String commentName) {
        this.commentName = commentName;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }
}