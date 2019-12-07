package com.expossoftware.tbid_dev.model;

import java.io.Serializable;

public class ItemProgramKelas implements Serializable {
    String namaKelas, hargaKelas;

    public ItemProgramKelas(String tNamaKelas, String tHargaKelas){
        this.namaKelas = tNamaKelas;
        this.hargaKelas = tHargaKelas;
    }

    public String getNamaKelas(){ return namaKelas; }
    public String getHargaKelas(){ return hargaKelas; }

}