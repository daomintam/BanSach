package com.example.bansach.Model;

public class BillModel {
    private static String maHoaDon;
    private String ngayMua;

    public BillModel() {
    }

    public BillModel(String maHoaDon, String ngayMua) {
        this.maHoaDon = maHoaDon;
        this.ngayMua = ngayMua;
    }

    public static String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public String getNgayMua() {
        return ngayMua;
    }

    public void setNgayMua(String ngayMua) {
        this.ngayMua = ngayMua;
    }
}

