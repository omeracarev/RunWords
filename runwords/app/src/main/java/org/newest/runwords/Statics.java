package org.newest.runwords;

public class Statics {

    private int id;
    private String corcount;
    private String wrocount;
    private String runcount;
    private String timecount;

    public Statics(int id, String corcount, String wrocount, String runcount, String timecount) {
        this.id = id;
        this.corcount = corcount;
        this.wrocount = wrocount;
        this.runcount = runcount;
        this.timecount = timecount;
    }

    public Statics() {

    }

    @Override
    public String toString() {
        return id+">"+corcount+">"+wrocount+">"+runcount+">"+timecount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCorcount() {
        return corcount;
    }

    public void setCorcount(String corcount) {
        this.corcount = corcount;
    }

    public String getWrocount() {
        return wrocount;
    }

    public void setWrocount(String wrocount) {
        this.wrocount = wrocount;
    }

    public String getRuncount() {
        return runcount;
    }

    public void setRuncount(String runcount) {
        this.runcount = runcount;
    }

    public String getTimecount() {
        return timecount;
    }

    public void setTimecount(String timecount) {
        this.timecount = timecount;
    }
}
