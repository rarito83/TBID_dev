package com.expossoftware.tbid_dev.model;

import java.io.Serializable;

public class ItemPresensi implements Serializable {
    String presenceSubClass,presenceDay, presenceDate, presenceTime;

    public ItemPresensi(String tPresenceSubClass, String tPresenceDay, String tPresenceDate, String tPresenceTime){
        this.presenceSubClass = tPresenceSubClass;
        this.presenceDay = tPresenceDay;
        this.presenceDate = tPresenceDate;
        this.presenceTime = tPresenceTime;
    }

    public String getPresenceSubClass() { return presenceSubClass; }
    public String getPresenceDay() { return presenceDay; }
    public String getPresenceTime() { return presenceTime; }
    public String getPresenceDate() { return presenceDate; }
}
