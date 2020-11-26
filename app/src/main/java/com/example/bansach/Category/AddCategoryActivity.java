package com.example.bansach.Category;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bansach.R;
import com.example.bansach.Firebase.CategoryFirebase;
import com.example.bansach.Model.CategoryModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;


public class AddCategoryActivity extends AppCompatActivity {
    Button them, ds;
    CategoryFirebase categoryFirebase;
    EditText ma, ten, vitri, mota;
    ArrayList<CategoryModel> listCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.them_the_loai);
        setTitle("THÊM THỂ LOẠI SÁCH");
        them = findViewById(R.id.btnThemTL);
        ds = findViewById(R.id.btnDSTL);
        ma = findViewById(R.id.edtMaTL);
        ten = findViewById(R.id.edtTenTL);
        vitri = findViewById(R.id.edtVitri);
        mota = findViewById(R.id.edtMoTa);
        getAll();
    }

    public void huyTL(View view) {
        finish();
    }

    public void dsTL(View view) {
        finish();
    }

    public void themTL(View view) {
        categoryFirebase = new CategoryFirebase(this);
        String maTL = ma.getText().toString();
        String tenTL = ten.getText().toString();
        String vtTL = vitri.getText().toString();
        String motaTL = mota.getText().toString();

        if (maTL.isEmpty() || tenTL.isEmpty() || vtTL.isEmpty() || motaTL.isEmpty()) {
        } else {
            if (xetTrung(maTL)) {
                Toast.makeText(AddCategoryActivity.this, "Mã không được trùng!", Toast.LENGTH_SHORT).show();
            } else {
                if (categoryFirebase.insert(new CategoryModel(maTL, tenTL, motaTL, Integer.parseInt(vtTL)))) {
                    finish();
                }
            }

        }
    }

    //Lấy toàn bộ list
    public ArrayList<CategoryModel> getAll() {
        listCategory = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("TheLoai").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listCategory.clear();
                    Iterable<DataSnapshot> dataSnapshotIterable = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = dataSnapshotIterable.iterator();
                    while (iterator.hasNext()) {
                        DataSnapshot next = (DataSnapshot) iterator.next();
                        CategoryModel categoryModel = next.getValue(CategoryModel.class);
                        listCategory.add(categoryModel);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(context, "Thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
        return listCategory;
    }

    //Xét trùng mã
    public boolean xetTrung(String maTheLoai) {
        Boolean xet = false;
        for (int i = 0; i < listCategory.size(); i++) {
            String ma = listCategory.get(i).getMaTheLoai();
            if (ma.equalsIgnoreCase(maTheLoai)) {
                xet = true;
                break;
            }
        }
        return xet;
    }
}
