package com.example.tp1;

public class Note {

    private String titre;
    private String description;
    private  int imgUrl;
    private String creationTime;

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String  creationTime) {
        this.creationTime = creationTime;
    }



    public Note() {

    }

    public int getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(int imgUrl) {
        this.imgUrl = imgUrl;
    }



    public Note(String titre, String description, int img, String creationTime) {
        this.titre = titre;
        this.description = description;
        this.imgUrl= img;
        this.creationTime = creationTime;
    }



    @Override
    public String toString(){

        return description;

    }
    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

