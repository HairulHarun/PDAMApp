package com.gorontalo.chair.pdam_app.adapter;

public class URLAdapter {
//    private String URL = "http://192.168.43.163/pdam-app/webservices/";
//    private String URL_PHOTO = "http://192.168.43.163/pdam-app/admin-control/assets/images/photo/";

    private String URL = "https://pdamgorontalo.com/webservices/";
    private String URL_PHOTO = "https://pdamgorontalo.com/admin-control/assets/images/photo/";

    public String getPdam(){
        return URL = URL+"ws-get-pdam.php";
    }

    public String getTagihan(){
        return URL = URL+"ws-get-tagihan.php";
    }

    public String getPengaduan(){
        return URL = URL+"ws-get-pengaduan.php";
    }

    public String simpanPengaduan(){
        return URL = URL+"ws-simpan-pengaduan.php";
    }

    public String getRiwayatTagihan(){
        return URL = URL+"ws-get-riwayat-tagihan.php";
    }

    public String getNotifikasi(){
        return URL = URL+"ws-get-all-notifikasi.php";
    }

    public String getLogin(){
        return URL = URL+"ws-login-pelanggan.php";
    }

    public String getSingleNotifikasi(){
        return URL = URL+"ws-get-single-notifikasi.php";
    }

    public String getPhotoPdam(){
        return URL = URL_PHOTO+"pdam/";
    }

    public String getPhotoPelanggan(){
        return URL = URL_PHOTO+"pelanggan/";
    }
}
