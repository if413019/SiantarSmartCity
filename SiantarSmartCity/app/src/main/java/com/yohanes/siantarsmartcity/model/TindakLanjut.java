package com.yohanes.siantarsmartcity.model;

/**
 * Created by Yohanes_marthin on 17/05/2016.
 */
public class TindakLanjut {
    private String status_pengaduan;
    private String komentar;
    private String id_skpd;
    private int id_pengaduan;
    private String waktu_create_tindaklanjut;

    public TindakLanjut(){

    }

    public void setStatus(String status){
        this.status_pengaduan = status;
    }

    public void setKomentar(String komentar){
        this.komentar = komentar;
    }

    public void setSkpd(String skpd){
        this.id_skpd = skpd;
    }

    public void setId_pengaduan(int pengaduan){
        this.id_pengaduan = pengaduan;
    }

    public void setWaktu(String waktu){
        this.waktu_create_tindaklanjut = waktu;
    }

    public String getStatus()
    {
        return this.status_pengaduan;
    }

    public String getKomentar()
    {
        return this.komentar;
    }
    public String getId_skpd()
    {
        return this.id_skpd;
    }
    public int getId_pengaduan()
    {
        return this.getId_pengaduan();
    }
    public String getWaktu()
    {
        return this.waktu_create_tindaklanjut;
    }
}
