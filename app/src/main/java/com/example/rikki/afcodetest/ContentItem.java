package com.example.rikki.afcodetest;

/**
 * Created by Rikki on 2016/12/27.
 */

public class ContentItem {

    private String title;
    private String target;

    public ContentItem() { }

    public ContentItem(String title, String target){
        this.title = title;
        this.target = target;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setTarget(String target){
        this.target = target;
    }

    public String getTitle(){
        return title;
    }

    public String getTarget(){
        return target;
    }
}
