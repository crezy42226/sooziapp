package com.crezy.fuinykook;

public class ModelAddPost {

    String id, title, dos, like,search;

    public ModelAddPost() {
    }

    public ModelAddPost(String id, String title, String dos) {
        this.id = id;
        this.title = title;
        this.dos = dos;
        this.like = like;
        this.search = search;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDos() {
        return dos;
    }

    public void setDos(String dos) {
        this.dos = dos;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}