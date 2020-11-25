package com.example.bansach.Book;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bansach.R;
import com.example.bansach.Firebase.BookFirebase;
import com.example.bansach.Firebase.CategoryFirebase;
import com.example.bansach.Model.BookModel;
import com.example.bansach.Model.CategoryModel;
import com.example.bansach.Category.AddCategoryActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class AddBookActivity extends AppCompatActivity {
    ArrayAdapter<CategoryModel> categoryModelArrayAdapter;
    BookFirebase bookFirebase;
    String current = "";
    CategoryFirebase categoryFirebase;
    Spinner spnTheLoai;
    EditText edMaSach, edTenSach, edNXB, edTacGia, edGiaBia, edSoLuong;
    String maTheLoai = "";
    ArrayList<CategoryModel> listCategory = new ArrayList<>();
    ArrayList<BookModel> listSach = new ArrayList<>();
    ImageView addCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_sach);
        setTitle("THÊM SÁCH");
        spnTheLoai = (Spinner) findViewById(R.id.spnTheLoai);

        getAllTL();
        getAllSach();
        showTheLoai();
        edMaSach = (EditText) findViewById(R.id.edMaSach);
        edTenSach = (EditText) findViewById(R.id.edTenSach);
        edNXB = (EditText) findViewById(R.id.edNXB);
        edTacGia = (EditText) findViewById(R.id.edTacGia);
        edGiaBia = (EditText) findViewById(R.id.edGiaBia);
        edSoLuong = (EditText) findViewById(R.id.edSoLuong);
        addCategory = findViewById(R.id.addCategory);

        //Format tiền


        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddBookActivity.this, AddCategoryActivity.class);
                startActivity(i);
            }
        });

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
        //load data into form
        Intent in = getIntent();
        Bundle b = in.getExtras();
        if (b != null) {
            edMaSach.setText(b.getString("MASACH"));
            String maTheLoai = b.getString("MATHELOAI");
            edTenSach.setText(b.getString("TENSACH"));
            edNXB.setText(b.getString("NXB"));
            edTacGia.setText(b.getString("TACGIA"));
            edGiaBia.setText(b.getString("GIABIA"));
            edSoLuong.setText(b.getString("SOLUONG"));
            spnTheLoai.setSelection(checkPositionTheLoai(maTheLoai));
        }
    }

    public void addBook(View view) {
        bookFirebase = new BookFirebase(AddBookActivity.this);
        String ma, maTL, ten, tgia, nxb, giaBia, sl;
        ma = edMaSach.getText().toString().trim();
        maTL = maTheLoai;
        ten = edTenSach.getText().toString().trim();
        tgia = edTacGia.getText().toString().trim();
        nxb = edNXB.getText().toString().trim();
        giaBia = edGiaBia.getText().toString().trim();
        sl = edSoLuong.getText().toString().trim();
        try {
            if (ma.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Các trường không được để trống!", Toast.LENGTH_SHORT).show();
            } else {
                if (listCategory.size() == 0) {
                    Toast.makeText(getApplicationContext(), "Vui lòng thêm thể loại trước!", Toast.LENGTH_SHORT).show();
                } else {
                    if (ten.isEmpty() && tgia.isEmpty() && nxb.isEmpty() && giaBia.isEmpty() && sl.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Các trường không được để trống!", Toast.LENGTH_SHORT).show();
                    } else {
                        BookModel bookModel = new BookModel(ma, maTL, ten, tgia, nxb, Integer.parseInt(giaBia), Integer.parseInt(sl));
                        //Xét sách không trùng mã
                        xetTrung(edMaSach.getText().toString());
                        if (checkTrung) {
                            Toast.makeText(getApplicationContext(), "Mã sách đã tồn tại!", Toast.LENGTH_SHORT).show();
                        } else {
                            if (bookFirebase.insert(bookModel)) {
                                finish();
                            }
                        }
                    }
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
                    categoryModelArrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return listCategory;
    }

    //Get toàn bộ sách
    public ArrayList<BookModel> getAllSach() {
        listSach = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Sach").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listSach.clear();
                    Iterable<DataSnapshot> dataSnapshotIterable = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = dataSnapshotIterable.iterator();
                    while (iterator.hasNext()) {
                        DataSnapshot next = (DataSnapshot) iterator.next();
                        BookModel bookModel = next.getValue(BookModel.class);
                        listSach.add(bookModel);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return listSach;
    }

    public void showTheLoai() {
        categoryFirebase = new CategoryFirebase(AddBookActivity.this);
        listCategory.clear();
        listCategory.addAll(getAllTL());
        categoryModelArrayAdapter = new ArrayAdapter<CategoryModel>(this,
                android.R.layout.simple_spinner_item, listCategory);
        categoryModelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTheLoai.setAdapter(categoryModelArrayAdapter);
    }

    Boolean checkTrung = false;

    public void xetTrung(String themMaSach) {
        for (int i = 0; i < listSach.size(); i++) {
            String ma = listSach.get(i).getMaSach();
            if (ma.equalsIgnoreCase(themMaSach)) {
                checkTrung = true;
                break;
            }
        }
    }
}
