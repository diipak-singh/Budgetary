package com.tecnosols.budgetary;

public class HelpModel {
    String heading, content;
    int image;

    public HelpModel(String heading, String content, int image) {
        this.heading = heading;
        this.content = content;
        this.image = image;
    }

    public HelpModel() {
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
