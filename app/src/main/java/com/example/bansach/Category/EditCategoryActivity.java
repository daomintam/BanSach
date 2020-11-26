package com.example.bansach.Category;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bansach.MainActivity;
import com.example.bansach.R;
import com.example.bansach.Firebase.CategoryFirebase;
import com.example.bansach.Model.CategoryModel;

public class EditCategoryActivity extends AppCompatActivity {
    CategoryFirebase categoryFirebase;
    TextView ma;
    EditText ten, vitri, mota;
    Button sua, huy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_the_loai);
        setTitle("SỬA THỂ LOẠI SÁCH");
        ma = findViewById(R.id.edtMaTL);
        ten = findViewById(R.id.edtTenTL);
        vitri = findViewById(R.id.edtVitri);
        mota = findViewById(R.id.edtMoTa);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        ma.setText(b.getString("MATHELOAI"));
        ten.setText(b.getString("TENTHELOAI"));
        mota.setText(b.getString("MOTA"));
        vitri.setText(b.getString("VITRI"));
        sua = findViewById(R.id.btnSuaTL);
        huy = findViewById(R.id.btnHuyTL);

        if (MainActivity.checkAdmin == false) {
            sua.setVisibility(View.GONE);
            huy.setVisibility(View.GONE);
        }
    }

    public void huyTL(View view) {
        finish();
    }

    public void dsTL(View view) {
        finish();
    }

    public void suaTL(View view) {
        categoryFirebase = new CategoryFirebase(this);
        String maTL = ma.getText().toString();
        String tenTL = ten.getText().toString();
        String vtTL = vitri.getText().toString();
        String motaTL = mota.getText().toString();

        if (maTL.isEmpty() || tenTL.isEmpty() || vtTL.isEmpty() || motaTL.isEmpty()) {
        } else {
            if (categoryFirebase.update(new CategoryModel(maTL, tenTL, motaTL, Integer.parseInt(vtTL)))) {
                finish();
            }
        }
    }
}
