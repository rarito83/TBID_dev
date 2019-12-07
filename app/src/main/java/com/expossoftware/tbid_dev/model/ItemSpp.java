package com.expossoftware.tbid_dev.model;

import java.io.Serializable;

public class ItemSpp implements Serializable {
    String sppStrDate, sppStrMonth, sppStrClass, sppStrSubClass, sppStrNominal, sppStrRefID, sppStrType, sppStrAliasName;

    public ItemSpp(String tStrDate, String tStrMonth, String tStrClass, String tStrSubClass, String tStrNominal,
                   String tStrRefID, String tStrType, String tStrAliasName) {
        this.sppStrDate = tStrDate;
        this.sppStrMonth = tStrMonth;
        this.sppStrClass = tStrClass;
        this.sppStrSubClass = tStrSubClass;
        this.sppStrNominal = tStrNominal;
        this.sppStrRefID = tStrRefID;
        this.sppStrType = tStrType;
        this.sppStrAliasName = tStrAliasName;

    }

    public String getSppStrDate() {
        return sppStrDate;
    }

    public String getSppStrMonth() {
        return sppStrMonth;
    }

    public String getSppStrClass() {
        return sppStrClass;
    }

    public String getSppStrSubClass() {
        return sppStrSubClass;
    }

    public String getSppStrNominal() {
        return sppStrNominal;
    }

    public String getSppStrRefID() {
        return sppStrRefID;
    }

    public String getSppStrType() {
        return sppStrType;
    }

    public String getSppStrAliasName() {
        return sppStrAliasName;
    }

}