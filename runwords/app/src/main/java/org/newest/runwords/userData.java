package org.newest.runwords;

public class userData {
    private String name;

    private String buttext;

    public userData() {
    }

    public userData(String name, String buttext) {
        this.name = name;
        this.buttext = buttext;
    }

    public String getButtext() {
        return buttext;
    }

    public void setButtext(String buttext) {
        this.buttext = buttext;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
