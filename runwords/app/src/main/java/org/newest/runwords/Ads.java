package org.newest.runwords;

import androidx.annotation.NonNull;

public class Ads {
    private int ad_id;
    private String ad_quota;
    private String ad_date;
    private String ad_type;

    public Ads() {

    }

    @NonNull
    @Override
    public String toString() {
        return ad_id+">"+ad_quota+">"+ad_date+">"+ad_type;
    }

    public Ads(int ad_id, String ad_quota, String ad_date, String ad_type) {
        this.ad_id = ad_id;
        this.ad_quota = ad_quota;
        this.ad_date = ad_date;
        this.ad_type = ad_type;
    }

    public int getAd_id() {
        return ad_id;
    }

    public void setAd_id(int ad_id) {
        this.ad_id = ad_id;
    }

    public String getAd_quota() {
        return ad_quota;
    }

    public void setAd_quota(String ad_quota) {
        this.ad_quota = ad_quota;
    }

    public String getAd_date() {
        return ad_date;
    }

    public void setAd_date(String ad_date) {
        this.ad_date = ad_date;
    }

    public String getAd_type() {
        return ad_type;
    }

    public void setAd_type(String ad_type) {
        this.ad_type = ad_type;
    }
}
