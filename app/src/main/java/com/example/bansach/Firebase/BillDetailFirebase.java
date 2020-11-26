package com.example.bansach.Firebase;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.bansach.Model.BillDetailModel;
import com.example.bansach.Statistics.StatisticsActivity;
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

public class BillDetailFirebase {
    Context context;
    DatabaseReference reference;
    String key = "";
    ArrayList<BillDetailModel> listBillDetail = new ArrayList<>();


    public BillDetailFirebase() {
    }

    public BillDetailFirebase(Context context) {
        this.context = context;
        reference = FirebaseDatabase.getInstance().getReference("HoaDonChiTiet");
    }

    public ArrayList<BillDetailModel> getAll() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listBillDetail.clear();
                    Iterable<DataSnapshot> dataSnapshotIterable = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = dataSnapshotIterable.iterator();
                    while (iterator.hasNext()) {
                        DataSnapshot next = (DataSnapshot) iterator.next();
                        BillDetailModel billDetailModel = next.getValue(BillDetailModel.class);
                        listBillDetail.add(billDetailModel);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        Log.i("hdct3", "" + listBillDetail.size());
        return listBillDetail;
    }

    public boolean insert(BillDetailModel billDetailModel) {
        reference.push().setValue(billDetailModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    Toast.makeText(context, "Thanh toán thành công", Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
        return true;
    }


    public boolean update(final BillDetailModel billDetailModel) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.child("maHoaDon").getValue(String.class).equalsIgnoreCase(billDetailModel.getMaHDCT())) {
                        key = data.getKey();
                        reference.child(key).setValue(billDetailModel);
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


    public void delete(final String maHoaDon) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.child("hoaDon").child("maHoaDon").getValue(String.class).equalsIgnoreCase(maHoaDon)) {
                        key = data.getKey();
                        reference.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
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

    public void getAllHD() {
        StatisticsActivity.listBillDetail.clear();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    StatisticsActivity.listBillDetail.clear();
                    Iterable<DataSnapshot> dataSnapshotIterable = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = dataSnapshotIterable.iterator();
                    while (iterator.hasNext()) {
                        DataSnapshot next = (DataSnapshot) iterator.next();
                        BillDetailModel billDetailModel = next.getValue(BillDetailModel.class);
                        listBillDetail.add(billDetailModel);
                    }
                    StatisticsActivity.listBillDetail.clear();
                    StatisticsActivity.listBillDetail.addAll(listBillDetail);
                    StatisticsActivity.showDthu();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        Log.i("hdct3", "" + listBillDetail.size());
    }

}
