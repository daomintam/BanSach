package com.example.bansach.Model;

public class BillDetailModel {
    private String maHDCT;
    private BillModel billModel;
    private BookModel bookModel;
    private int soLuongMua;

    public BillDetailModel() {
    }

    public BillDetailModel(String maHDCT, BillModel billModel, BookModel bookModel, int soLuongMua) {
        this.maHDCT = maHDCT;
        this.billModel = billModel;
        this.bookModel = bookModel;
        this.soLuongMua = soLuongMua;
    }

    public String getMaHDCT() {
        return maHDCT;
    }

    public void setMaHDCT(String maHDCT) {
        this.maHDCT = maHDCT;
    }

    public BillModel getBillModel() {
        return billModel;
    }

    public void setgetBillModel(BillModel billModel) {
        this.billModel = billModel;
    }

    public BookModel getBook() {
        return bookModel;
    }

    public void setSach(BookModel bookModel) {
        this.bookModel = bookModel;
    }

    public int getSoLuongMua() {
        return soLuongMua;
    }

    public void setSoLuongMua(int soLuongMua) {
        this.soLuongMua = soLuongMua;
    }

    @Override
    public String toString() {
        return "HoaDonChiTiet{" +
                "maHDCT=" + maHDCT +
                ", hoaDon=" + billModel.toString() +
                ", sach=" + bookModel.toString() +
                ", soLuongMua=" + soLuongMua +
                '}';
    }
}
