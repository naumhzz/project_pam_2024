package com.nauproject.myproject;

public class item_reminder {
    int id;
    public String jam;
    public String status;
    String image;

    public item_reminder(int id, String jam, String status, String image) {
        this.id = id;
        this.jam = jam;
        this.status = status;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getJam() {
        return jam;
    }

    public String getStatus() {
        return status;
    }

    public String getImage() {
        return image;
    }
}