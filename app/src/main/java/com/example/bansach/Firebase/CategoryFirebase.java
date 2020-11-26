package com.example.bansach.Firebase;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.bansach.Model.CategoryModel;
import com.example.bansach.Category.ListCategoryActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class CategoryFirebase {
    Context context;

    DatabaseReference reference;
    String key = "";

    public CategoryFirebase() {
    }

    public CategoryFirebase(Context context) {
        this.context = context;
        reference = FirebaseDatabase.getInstance().getReference("TheLoai");
    }

    public ArrayList<CategoryModel> getAll() {
        final ArrayList<CategoryModel> listCategory = new ArrayList<>();
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
                    ListCategoryActivity.adapter.notifyDataSetChanged();
                }
//                list.clear();
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    TheLoai s = data.getValue(TheLoai.class);
//                    list.add(s);
//                    ListTheLoaiActivity.adapter.notifyDataSetChanged();
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return listCategory;
    }

    public boolean insert(CategoryModel categoryModel) {
        reference.push().setValue(categoryModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
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


    public boolean update(final CategoryModel categoryModel) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.child("maTheLoai").getValue(String.class).equalsIgnoreCase(categoryModel.getMaTheLoai())) {
                        key = data.getKey();
                        reference.child(key).setValue(categoryModel);
                        Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return true;
    }


    public void delete(final String maTheLoai) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.child("maTheLoai").getValue(String.class).equalsIgnoreCase(maTheLoai)) {
                        key = data.getKey();
                        reference.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
