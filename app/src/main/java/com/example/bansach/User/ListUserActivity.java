package com.example.bansach.User;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.bansach.MainActivity;
import com.example.bansach.R;
import com.example.bansach.Adapter.UserAdapter;
import com.example.bansach.Firebase.UserFirebase;
import com.example.bansach.Account.ChangePasswordActivity;
import com.example.bansach.Account.LogInActivity;
import com.example.bansach.Account.SignInActivity;
import com.example.bansach.Model.UserModel;

import java.util.ArrayList;

public class ListUserActivity extends AppCompatActivity {
    ArrayList<UserModel> userModelArrayList;
    SwipeMenuListView lvNguoiDung;
    public static UserAdapter adapter;
    UserFirebase userFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_nguoi_dung);
        setTitle("NGƯỜI DÙNG");
        lvNguoiDung = findViewById(R.id.lvNguoiDung);


        //Lấy dữ liệu người dùng từ FireBase
        userFirebase = new UserFirebase(ListUserActivity.this);
        userModelArrayList = userFirebase.getAll_ND();
        adapter = new UserAdapter(ListUserActivity.this, userModelArrayList);
        lvNguoiDung.setAdapter(adapter);

        //Kéo để hiện item xóa
        swipe();

        lvNguoiDung.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListUserActivity.this, EditUserActivity.class);
                Bundle b = new Bundle();
                b.putString("USERNAME", userModelArrayList.get(position).getUserName());
                b.putString("PASSWORD", userModelArrayList.get(position).getPassword());
                b.putString("PHONE", userModelArrayList.get(position).getPhone());
                b.putString("FULLNAME", userModelArrayList.get(position).getHoTen());
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        lvNguoiDung.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });
    }

    private void swipe() {
        //Thanh Swipe để xóa
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        lvNguoiDung.setMenuCreator(creator);
        lvNguoiDung.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                if (MainActivity.checkAdmin == true) {
                    final UserModel userModel = userModelArrayList.get(position);
                    if (index == 0) {
                        final AlertDialog.Builder builder2 = new AlertDialog.Builder(ListUserActivity.this);
                        builder2.setTitle("Cảnh báo");
                        builder2.setMessage("Bạn có chắc chắn xóa?");
                        builder2.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                userFirebase.delete(userModel);
                                userModelArrayList.clear();
                                userModelArrayList.addAll(userFirebase.getAll_ND());
                            }
                        });
                        builder2.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        final AlertDialog dialog = builder2.create();
                        dialog.show();
                    }
                    return false;
                }
                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user, menu);
        if (MainActivity.checkAdmin == false) {
            menu.findItem(R.id.add).setVisible(false);
        } else {
            menu.findItem(R.id.add).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;
        switch (item.getItemId()) {
            case R.id.add:
                intent = new
                        Intent(ListUserActivity.this, SignInActivity.class);
                startActivity(intent);
                return (true);
            case R.id.changePass:
                intent = new
                        Intent(ListUserActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
                return (true);
            case R.id.logOut:
                SharedPreferences pref =
                        getSharedPreferences("USER_FILE", MODE_PRIVATE);
                SharedPreferences.Editor edit = pref.edit();
                //xoa tinh trang luu tru truoc do
                edit.clear();
                edit.commit();
                intent = new Intent(ListUserActivity.this, LogInActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
