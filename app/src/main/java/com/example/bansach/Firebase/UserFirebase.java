package com.example.bansach.Firebase;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.bansach.MainActivity;
import com.example.bansach.Model.UserModel;
import com.example.bansach.User.ListUserActivity;
import com.example.bansach.User.MyUserActivity;
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

public class UserFirebase {
    Context context;
    DatabaseReference reference;
    String key = "";

    public UserFirebase() {
    }

    public UserFirebase(Context context) {
        this.context = context;
        reference = FirebaseDatabase.getInstance().getReference("NguoiDung");
    }

    public ArrayList<UserModel> getAll_ND() {
        final ArrayList<UserModel> userModelArrayList = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userModelArrayList.clear();
                    Iterable<DataSnapshot> dataSnapshotIterable = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = dataSnapshotIterable.iterator();
                    while (iterator.hasNext()) {
                        DataSnapshot next = (DataSnapshot) iterator.next();
                        UserModel userModel = next.getValue(UserModel.class);
                        userModelArrayList.add(userModel);
                    }
                    ListUserActivity.adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Lấy người dùng thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
        return userModelArrayList;
    }

    public boolean insert(UserModel userModel) {
        reference.push().setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    ListUserActivity.adapter.notifyDataSetChanged();
                    Toast.makeText(context, "Thành công", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return true;
    }

    public void update(final UserModel userModel) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.child("userName").getValue(String.class).equalsIgnoreCase(userModel.getUserName())) {
                        key = data.getKey();
                        reference.child(key).setValue(userModel);
                        ListUserActivity.adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void changePass(final String userName, final String password) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.child("userName").getValue(String.class).equalsIgnoreCase(userName)) {
                        key = data.getKey();
//                        reference.child(key).child("userName").setValue(userName);
                        reference.child(key).child("password").setValue(password);
                        ListUserActivity.adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void delete(final UserModel userModel) {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.child("userName").getValue(String.class).equalsIgnoreCase(userModel.getUserName())) {
                        key = data.getKey();
                        reference.child(key).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public ArrayList<UserModel> getAllNone() {
        final ArrayList<UserModel> userModelArrayList = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userModelArrayList.clear();
                    Iterable<DataSnapshot> dataSnapshotIterable = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = dataSnapshotIterable.iterator();
                    while (iterator.hasNext()) {
                        DataSnapshot next = (DataSnapshot) iterator.next();
                        UserModel userModel = next.getValue(UserModel.class);
                        userModelArrayList.add(userModel);
                    }
                }
                MainActivity.setTenTk();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return userModelArrayList;
    }

    public ArrayList<UserModel> getADMIN() {
        final ArrayList<UserModel> userModelArrayList = new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userModelArrayList.clear();
                    Iterable<DataSnapshot> dataSnapshotIterable = dataSnapshot.getChildren();
                    Iterator<DataSnapshot> iterator = dataSnapshotIterable.iterator();
                    while (iterator.hasNext()) {
                        DataSnapshot next = (DataSnapshot) iterator.next();
                        UserModel userModel = next.getValue(UserModel.class);
                        userModelArrayList.add(userModel);
                    }
                    MyUserActivity.setAdmin();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return userModelArrayList;
    }
}
