package com.expossoftware.tbid_dev.model;

import java.io.Serializable;

public class ItemPengajar implements Serializable {
    String nama, jabatan;
    int img;


    /*public ItemPengajar(){

    }*/

    public ItemPengajar(String tnama, String tjabatan, int tImg){
        this.nama = tnama;
        this.jabatan = tjabatan;
        this.img = tImg;
    }

    public String getNama() { return nama; }
    public String getJabatan() { return jabatan; }
    public int getImage() { return img; }
}