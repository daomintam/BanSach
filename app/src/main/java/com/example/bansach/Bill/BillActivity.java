package com.example.bansach.Bill;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bansach.R;
import com.example.bansach.Firebase.BillFirebase;
import com.example.bansach.Model.BillModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BillActivity extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    EditText edNgayMua, edMaHoaDon;
    //    HoaDonDAO hoaDonDAO;
    BillFirebase billFireBase;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa_don);
        setTitle("THÊM HOÁ ĐƠN");
        setContentView(R.layout.activity_hoa_don);
        edNgayMua = (EditText) findViewById(R.id.edNgayMua);
        edMaHoaDon = (EditText) findViewById(R.id.edMaHoaDon);
    }


    public void datePicker(View view) {
        final Calendar calendar = Calendar.getInstance();
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int m = calendar.get(Calendar.MONTH);
        int y = calendar.get(Calendar.YEAR);
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String ngay = "";
                if (String.valueOf(month).length() == 1) {
                    ngay = dayOfMonth + "/" + "0" + (month + 1) + "/" + year;
                } else {
                    ngay = dayOfMonth + "/" + (month + 1) + "/" + year;
                }
                edNgayMua.setText(ngay);
            }
        }, y, m, d);
        datePickerDialog.show();
    }

    public void ADDHoaDon(View view) {
        billFireBase = new BillFirebase(BillActivity.this);
//        hoaDonDAO = new HoaDonDAO(HoaDonActivity.this);
        try {
            if (validation() < 0) {
                Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                BillModel billModel = new
                        BillModel(edMaHoaDon.getText().toString(), edNgayMua.getText().toString());
                if (billFireBase.insert(billModel)) {
                    Toast.makeText(getApplicationContext(), "Thêm thành công",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new
                            Intent(BillActivity.this, BillDetailActivity.class);
                    Bundle b = new Bundle();
                    b.putString("MAHOADON", edMaHoaDon.getText().toString());
                    b.putString("Ngay", edNgayMua.getText().toString());
                    intent.putExtras(b);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Thêm thất bại",
                            Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception ex) {
            Log.e("Error", ex.toString());
        }
    }

    public int validation() {
        if
        (edMaHoaDon.getText().toString().isEmpty() || edNgayMua.getText().toString().isEmpty()
        ) {
            return -1;
        }
        return 1;
    }
}
