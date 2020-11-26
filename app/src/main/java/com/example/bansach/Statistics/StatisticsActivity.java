package com.example.bansach.Statistics;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bansach.R;
import com.example.bansach.Firebase.BillDetailFirebase;
import com.example.bansach.Model.BillDetailModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class StatisticsActivity extends AppCompatActivity {
    public static TextView tvNgay, tvThang, tvNam;
    BillDetailFirebase billDetailFirebase;
    public static ArrayList<BillDetailModel> listBillDetail = new ArrayList<>();
    public static String ngay, thang, nam;
    public static int tongNgay = 0, tongThang = 0, tongNam = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("DOANH THU");
        setContentView(R.layout.activity_statistics);
        tongNgay = 0;
        tongThang = 0;
        tongNam = 0;

        //Lấy toàn bộ list HDCT
        billDetailFirebase = new BillDetailFirebase(this);


        tvNgay = (TextView) findViewById(R.id.tvThongKeNgay);
        tvThang = (TextView) findViewById(R.id.tvThongKeThang);
        tvNam = (TextView) findViewById(R.id.tvThongKeNam);

        tvNgay.setText(tongNgay + " đ");
        tvThang.setText(tongThang + " đ");
        tvNam.setText(tongNam + " đ");
        getDateNow();
        billDetailFirebase.getAllHD();
    }

    public static void getDateNow() {
        //Lấy ngày hiện tại
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        ngay = currentDate.substring(0, 2);
        thang = currentDate.substring(3, 5);
        nam = currentDate.substring(6, currentDate.length());
    }

    //SHow doanh thu theo ngày, tháng, năm
    public static void showDthu() {
        for (int i = 0; i < listBillDetail.size(); i++) {
            String ngayTK = listBillDetail.get(i).getBillModel().getNgayMua().substring(0, 2);
            String thangTK = listBillDetail.get(i).getBillModel().getNgayMua().substring(3, 5);
            String namTK = listBillDetail.get(i).getBillModel().getNgayMua().substring(6, listBillDetail.get(i).getBillModel().getNgayMua().length());
            if (ngay.matches(ngayTK)) {
                tongNgay += listBillDetail.get(i).getSoLuongMua() * listBillDetail.get(i).getBook().getGiaBia();
            }
            if (thang.matches(thangTK)) {
                tongThang += listBillDetail.get(i).getSoLuongMua() * listBillDetail.get(i).getBook().getGiaBia();
            }
            if (nam.matches(namTK)) {
                tongNam += listBillDetail.get(i).getSoLuongMua() * listBillDetail.get(i).getBook().getGiaBia();
            }

        }
        tvNgay.setText(tongNgay + " đ");
        tvThang.setText(tongThang + " đ");
        tvNam.setText(tongNam + " đ");
    }

}
