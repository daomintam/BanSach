package com.example.bansach.Book;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bansach.Category.AddCategoryActivity;
import com.example.bansach.MainActivity;
import com.example.bansach.R;
import com.example.bansach.Firebase.BookFirebase;
import com.example.bansach.Firebase.CategoryFirebase;
import com.example.bansach.Model.BookModel;
import com.example.bansach.Model.CategoryModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class EditBookActivity extends AppCompatActivity {
    ArrayAdapter<CategoryModel> categoryModelArrayAdapter;
    BookFirebase bookFirebase;
    CategoryFirebase categoryFirebase;
    Spinner spnTheLoai;
    TextView edMaSach;
    EditText edTenSach, edNXB, edTacGia, edGiaBia, edSoLuong;
    String maTheLoai = "";
    ImageButton addCategory;
    ArrayList<CategoryModel> listCategory;
    Button btnSua, btnHuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_sach);
        setTitle("SỬA SÁCH");
        spnTheLoai = (Spinner) findViewById(R.id.spnTheLoai);
        getTheLoai();
        edMaSach = findViewById(R.id.edMaSach);
        edTenSach = (EditText) findViewById(R.id.edTenSach);
        edNXB = (EditText) findViewById(R.id.edNXB);
        edTacGia = (EditText) findViewById(R.id.edTacGia);
        edGiaBia = (EditText) findViewById(R.id.edGiaBia);
        edSoLuong = (EditText) findViewById(R.id.edSoLuong);
        btnSua = findViewById(R.id.btnAddBook);
        btnHuy = findViewById(R.id.btnCancelBook);

        if (MainActivity.checkAdmin == false) {
            btnSua.setVisibility(View.GONE);
            btnHuy.setVisibility(View.GONE);
        }

        spnTheLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int
                    position, long id) {
                maTheLoai = listCategory.get(spnTheLoai.getSelectedItemPosition()).getMaTheLoai();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        findViewById(R.id.addCategory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditBookActivity.this, AddCategoryActivity.class);
                startActivity(i);
            }
        });


    }

    public void getTheLoai() {
        categoryFirebase = new CategoryFirebase(EditBookActivity.this);
        listCategory = getAllTL();

        categoryModelArrayAdapter = new ArrayAdapter<CategoryModel>(this,
                android.R.layout.simple_spinner_item, listCategory);
        categoryModelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTheLoai.setAdapter(categoryModelArrayAdapter);
    }

    public void editBook(View view) {
        bookFirebase = new BookFirebase(EditBookActivity.this);
        String ma, maTL, ten, tgia, nxb, giaBia, sl;
        ma = edMaSach.getText().toString();
        maTL = maTheLoai;
        ten = edTenSach.getText().toString();
        tgia = edTacGia.getText().toString();
        nxb = edNXB.getText().toString();
        giaBia = edGiaBia.getText().toString();
        sl = edSoLuong.getText().toString();
        try {
            if (ma.isEmpty() && ten.isEmpty() && tgia.isEmpty() && nxb.isEmpty() && giaBia.isEmpty() && sl.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Các trường không được để trống!", Toast.LENGTH_SHORT).show();
            } else {
                BookModel bookModel = new BookModel(ma, maTL, ten, tgia, nxb, Integer.parseInt(giaBia), Integer.parseInt(sl));
                if (bookFirebase.update(bookModel)) {
                    Toast.makeText(getApplicationContext(), "Sửa thành công", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        } catch (Exception ex) {
            Log.e("Error", ex.toString());
        }
    }

    public void showBook(View view) {
        finish();
    }

    public int checkPositionTheLoai(String strTheLoai) {
        for (int i = 0; i < listCategory.size(); i++) {
            if (strTheLoai.equals(listCategory.get(i).getMaTheLoai())) {
                return i;
            }
        }
        return 0;
    }

    public ArrayList<CategoryModel> getAllTL() {
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
                categoryModelArrayAdapter.notifyDataSetChanged();
                //load data into form
                Intent in = getIntent();
                Bundle b = in.getExtras();
                if (b != null) {
                    edMaSach.setText(b.getString("MASACH"));
                    String ma = b.getString("MATHELOAI");
                    spnTheLoai.setSelection(checkPositionTheLoai(ma));
                    edTenSach.setText(b.getString("TENSACH"));
                    edNXB.setText(b.getString("NXB"));
                    edTacGia.setText(b.getString("TACGIA"));
                    edGiaBia.setText(b.getString("GIABIA"));
                    edSoLuong.setText(b.getString("SOLUONG"));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return listCategory;
    }
}
