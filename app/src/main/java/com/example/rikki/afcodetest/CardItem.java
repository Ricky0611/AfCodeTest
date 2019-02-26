package com.example.rikki.afcodetest;

import java.util.ArrayList;

/**
 * Created by Rikki on 2016/12/27.
 */

public class CardItem {

    private String title;
    private String imageUrl;
    private ArrayList<ContentItem> list;
    private String promoMessage;
    private String topDesc;
    private String bottomDesc;
    private String link;

    public CardItem(){
        list = new ArrayList<>();
    }

    public void setTitle(String string){
        title = string;
    }
    public void setImageUrl(String string){
        imageUrl = string;
    }
    public void setPromo(String string){
        promoMessage = string;
    }
    public void setTopDesc(String string){
        topDesc = string;
    }
    public void setBottomDesc(String string){
        bottomDesc = string;
    }
    public void setLink(String string){
        link = string;
    }
    public void setList(ArrayList<ContentItem> list){
        this.list = list;
    }
    public void setContent(int position, String title, String target){
        ContentItem item = new ContentItem(title, target);
        list.add(position, item);
    }

    public String getTitle(){
        return title;
    }
    public String getImageUrl(){
        return imageUrl;
    }
    public String getPromo(){
        return promoMessage;
    }
    public String getTopDesc(){
        return topDesc;
    }
    public String getBottomDesc(){
        return bottomDesc;
    }
    public String getLink() {
        return link;
    }
    public ArrayList<ContentItem> getList(){
        return list;
    }
}
