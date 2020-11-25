package com.example.bansach.BestSeller;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bansach.R;
import com.example.bansach.Adapter.BookAdapter;
import com.example.bansach.Firebase.BillDetailFirebase;
import com.example.bansach.Firebase.BookFirebase;
import com.example.bansach.Model.BillDetailModel;
import com.example.bansach.Model.BookModel;

import java.util.ArrayList;
import java.util.List;

public class BestSellerActivity extends AppCompatActivity {
    public static List<BookModel> listBook = new ArrayList<>();
    ListView lvBook;
    BookAdapter adapter = null;
    EditText edThang;
    BillDetailFirebase billDetailFirebase;
    BookFirebase bookFirebase;
    ArrayList<BillDetailModel> listBillDetail;
    ArrayList<BillDetailModel> listTheoThang = new ArrayList<>();
    ArrayList<BookModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("TOP 10 SÁCH BÁN CHẠY");
        setContentView(R.layout.activity_luot_sach_ban_chay);
        lvBook = (ListView) findViewById(R.id.lvBookTop);
        edThang = (EditText) findViewById(R.id.edThang);
        billDetailFirebase = new BillDetailFirebase(this);
        bookFirebase = new BookFirebase(this);

        listBillDetail = billDetailFirebase.getAll();
        listBook = bookFirebase.getAllNone();
    }

    public void VIEW_SACH_TOP_10(View view) {
        try {
            String thang = edThang.getText().toString();
            if (Integer.parseInt(thang) > 13 ||
                    Integer.parseInt(thang) < 0) {
                Toast.makeText(getApplicationContext(), "Không đúng định dạng tháng (1-12)", Toast.LENGTH_SHORT).show();
            } else {
                //Lọc list theo tháng
                getListByMonth(thang);
                if (listTheoThang.size() != 0) {
                    //Lọc top 10 rồi show lên
                    top10();
//            Toast.makeText(this, listTheoThang.size()+"", Toast.LENGTH_SHORT).show();
                    //Lấy toàn bộ sách theo mã
                    convertMaSach();
                } else {
                    Toast.makeText(getApplicationContext(), "Không có thông tin!", Toast.LENGTH_SHORT).show();

                }
            }
        } catch (Exception ex) {

        }
        adapter = new BookAdapter(this, list);
        lvBook.setAdapter(adapter);

    }

    //22/6/2020
    //Lọc list theo tháng
    public void getListByMonth(String month) {
        for (int i = 0; i < listBillDetail.size(); i++) {
            String date = listBillDetail.get(i).getBillModel().getNgayMua();
            String thang = date.substring(date.length() - 6, date.length() - 4);
            if (thang.substring(1, 2).matches("/")) {
                thang = thang.substring(0, 1);
            }
            if (thang.matches(month)) {
                listTheoThang.add(listBillDetail.get(i));
            }
        }
    }


    //Lọc sách bán hạy{ nhất
    public void top10() {
        if (listTheoThang.size() > 10) {
            for (int i = 0; i < listTheoThang.size(); i++) {
                int sl1 = listTheoThang.get(i).getSoLuongMua();
                for (int j = i + 1; j < listTheoThang.size() - 1; j++) {
                    int sl2 = listTheoThang.get(j).getSoLuongMua();
                    if (sl1 < sl2) {
                        BillDetailModel l = listTheoThang.get(i);
                        listTheoThang.set(i, listTheoThang.get(j));
                        listTheoThang.set(j, l);
                    }
                }
            }
        }
    }

    //Lấy full sách từ mã sách
    public void convertMaSach() {
        for (int i = 0; i < listBook.size(); i++) {
            String ma = listTheoThang.get(i).getBook().getMaSach();
            if (listBook.get(i).getMaSach().matches(ma)) {
                list.add(listBook.get(i));
            }
        }
    }
}
