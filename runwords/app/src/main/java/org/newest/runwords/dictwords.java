package org.newest.runwords;

public class dictwords {
    private String engwords;
    private String trwords;
    private int rawid;

    public dictwords(String engwords, String trwords, int rawid) {
        this.engwords = engwords;
        this.trwords = trwords;
        this.rawid = rawid;
    }

    public String getEngwords() {
        return engwords;
    }

    public void setEngwords(String engwords) {
        this.engwords = engwords;
    }

    public String getTrwords() {
        return trwords;
    }

    public void setTrwords(String trwords) {
        this.trwords = trwords;
    }

    public int getRawid() {
        return rawid;
    }

    public void setRawid(int rawid) {
        this.rawid = rawid;
    }
}
