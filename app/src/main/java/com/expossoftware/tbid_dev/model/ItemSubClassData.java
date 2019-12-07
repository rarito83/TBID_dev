package com.expossoftware.tbid_dev.model;

import java.io.Serializable;

public class ItemSubClassData implements Serializable {
    String namaSubKelas;

    public ItemSubClassData(String tnamakelas){
        this.namaSubKelas = tnamakelas;
    }

    public String getNamaSubKelas() { return namaSubKelas; }

}
