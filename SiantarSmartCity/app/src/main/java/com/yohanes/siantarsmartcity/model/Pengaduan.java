package com.yohanes.siantarsmartcity.model;
import java.util.Date;

/**
 * Created by Yohanes_marthin on 26/03/2016.
 */
public class Pengaduan {
    private int id;
    private String judul;
    private String deskripsi;
    private String alamat;
    private String tanggal;
    private String namauser;
    private String status;
    private String image;

    public Pengaduan(int _id, String _judul, String _deskripsi, String _alamat, String _tanggal,
                     String nama_user, String _status)
    {
        this.id = _id;
        this.judul = _judul;
        this.deskripsi = _deskripsi;
        this.alamat = _alamat;
        this.tanggal = _tanggal;
        this.namauser = nama_user;
        this.status = _status;
    }

    public void setJudul(String judul)
    {
        this.judul = judul;
    }

    public void setDeskripsi(String deskripsi)
    {
        this.deskripsi = deskripsi;
    }

    public void setAlamat(String alamat)
    {
        this.alamat = alamat;
    }

    public void setTanggal(String tanggal)
    {
        this.tanggal = tanggal;
    }
    public void setImage(String image) { this.image = image; }

    public int getId(){return this.id;}

    public String getNamaUser(){return this.namauser;}

    public String getJudul()
    {
        return this.judul;
    }

    public String getDeskripsi()
    {
        return this.deskripsi;
    }

    public String getAlamat()
    {
        return this.alamat;
    }

    public String getTanggal()
    {
       return this.tanggal;
    }

    public String getStatus()
    {
        return this.status;
    }

    public String getImage() { return this.image;}
}