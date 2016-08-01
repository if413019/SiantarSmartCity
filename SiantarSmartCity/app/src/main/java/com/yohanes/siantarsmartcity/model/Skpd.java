package com.yohanes.siantarsmartcity.model;

/**
 * Created by Yohanes_marthin on 13/06/2016.
 */
public class Skpd {
    private int id;
    private String nama;
    private String deskripsi;
    private String image;
    private String alamat;
    private String telepon;

    public Skpd(int _id, String _nama, String _deskripsi, String _image, String _alamat, String
            _telepon)
    {
        this.id = _id;
        this.nama = _nama;
        this.deskripsi = _deskripsi;
        this.image = _image;
        this.alamat = _alamat;
        this.telepon = _telepon;
    }

    public void setImage(String image) { this.image = image; }

    public int getId(){return this.id;}

    public String getNama(){return this.nama;}

    public String getDeskripsi()
    {
        return this.deskripsi;
    }

    public String getImage() { return this.image;}

    public String getAlamat()
    {
        return this.alamat;
    }

    public String getTelepon() { return this.telepon;}

}
