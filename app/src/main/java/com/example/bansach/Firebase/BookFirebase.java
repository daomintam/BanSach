package com.example.bansach.Firebase;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.bansach.Bill.BillDetailActivity;
import com.example.bansach.Model.BookModel;
import com.example.bansach.Book.ListBookActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class BookFirebase {
    Context context;
    DatabaseReference reference;
    String key = "";

    public BookFirebase() {
    }

    public BookFirebase(Context context) {
        this.context = context;
        reference = FirebaseDatabase.getInstance().getReference("Sach");
    }

    //Get toàn bộ sách
    public ArrayList<BookModel> getAll() {
        final ArrayList<BookModel> list = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Sach").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    list.clear();
                    Iterable<DataSnapshot> dataSnapshotIterable = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = dataSnapshotIterable.iterator();
                    while (iterator.hasNext()) {
                        DataSnapshot next = (DataSnapshot) iterator.next();
                        BookModel bookModel = next.getValue(BookModel.class);
                        list.add(bookModel);
                    }
                }
                ListBookActivity.adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return list;
    }


    public boolean insert(BookModel bookModel) {
        reference.push().setValue(bookModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
//                    ListTheLoaiActivity.adapter.notifyDataSetChanged();
                    Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
        return true;
    }


    public boolean update(final BookModel bookModel) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.child("maSach").getValue(String.class).equalsIgnoreCase(bookModel.getMaSach())) {
                        key = data.getKey();
                        reference.child(key).setValue(bookModel);
                        Toast.makeText(context, "Sửa thành công!", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return true;
    }


    public void delete(final String maSach) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.child("maSach").getValue(String.class).equalsIgnoreCase(maSach)) {
                        key = data.getKey();
                        reference.child(key).removeValue();
                        Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    //Xóa theo mã thể loại
    public void deletebyMaTL(final String maTL) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.child("maTheLoai").getValue(String.class).equalsIgnoreCase(maTL)) {
                        key = data.getKey();
                        reference.child(key).removeValue();
                        Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        ListBookActivity.adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //Get toàn bộ sách
    public ArrayList<BookModel> getAllSach() {
        final ArrayList<BookModel> listSach = new ArrayList<>();
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
                        Log.i("loi", listSach.get(0).getMaSach());
                    }
                }
                BillDetailActivity.dataAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return listSach;
    }


    //Get toàn bộ sách
    public ArrayList<BookModel> getAllNone() {
        final ArrayList<BookModel> listSach = new ArrayList<>();
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
                        Log.i("loi", listSach.get(0).getMaSach());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return listSach;
    }
}
