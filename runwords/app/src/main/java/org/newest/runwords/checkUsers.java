package org.newest.runwords;

import androidx.annotation.NonNull;

public class checkUsers {
    private  int  userid;
    private String username;

    public checkUsers(int userid, String username) {
        this.userid = userid;
        this.username = username;
    }

    public checkUsers() {

    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NonNull
    @Override
    public String toString() {
        return username;
    }
}
