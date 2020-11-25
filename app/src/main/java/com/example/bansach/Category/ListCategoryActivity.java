package com.example.bansach.Category;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.bansach.Adapter.CategoryAdapter;
import com.example.bansach.Firebase.BookFirebase;
import com.example.bansach.Firebase.CategoryFirebase;
import com.example.bansach.Model.CategoryModel;

import java.util.ArrayList;

public class ListCategoryActivity extends AppCompatActivity {
    public static ArrayList<CategoryModel> listCategory;
    public static SwipeMenuListView lvTheLoai;
    public static CategoryAdapter adapter;
    CategoryFirebase categoryFirebase;
    BookFirebase bookFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_category);
        setTitle("THỂ LOẠI");
        setContentView(R.layout.activity_list_category);
        lvTheLoai = findViewById(R.id.lvTheLoai);
        categoryFirebase = new CategoryFirebase(ListCategoryActivity.this);
        listCategory = categoryFirebase.getAll();
        adapter = new CategoryAdapter(ListCategoryActivity.this, listCategory);
        lvTheLoai.setAdapter(adapter);
//        registerForContextMenu(lvTheLoai);
        bookFirebase = new BookFirebase(this);
        findViewById(R.id.btnThemTheLoai).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.checkAdmin == true) {
                    Intent i = new Intent(ListCategoryActivity.this, AddCategoryActivity.class);
                    startActivity(i);
                } else
                    finish();
            }
        });


        //Thanh Swipe để xóa
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        lvTheLoai.setMenuCreator(creator);

        lvTheLoai.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                final CategoryModel categoryModel = listCategory.get(position);
                if (index == 0) {
                    final AlertDialog.Builder builder2 = new AlertDialog.Builder(ListCategoryActivity.this);
                    builder2.setTitle("Cảnh báo");
                    builder2.setMessage("Xóa thể loại sẽ xóa luôn các sách thuộc thể loại đó. Bạn chắc chắn muốn xóa?");
                    builder2.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            categoryFirebase.delete(categoryModel.getMaTheLoai());
                            //Xóa luôn bên sách theo mã thể loại
                            bookFirebase.deletebyMaTL(categoryModel.getMaTheLoai());
                            listCategory.clear();
                            listCategory.addAll(categoryFirebase.getAll());

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
        });

        lvTheLoai.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new
                        Intent(ListCategoryActivity.this, EditCategoryActivity.class);
                Bundle b = new Bundle();
                b.putString("MATHELOAI", listCategory.get(position).getMaTheLoai());
                b.putString("TENTHELOAI", listCategory.get(position).getTenTheLoai());
                b.putString("VITRI", String.valueOf(listCategory.get(position).getViTri()));
                b.putString("MOTA", listCategory.get(position).getMoTa());
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_category, menu);
        if (MainActivity.checkAdmin == false) {
            menu.findItem(R.id.add).setVisible(false);
        }
        MenuItem menuitem = menu.findItem(R.id.add);
        menuitem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new
                        Intent(ListCategoryActivity.this, AddCategoryActivity.class);
                startActivity(intent);
                return (true);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent1 = new
                        Intent(ListCategoryActivity.this, AddCategoryActivity.class);
                startActivity(intent1);
                return (true);
        }
        return super.onContextItemSelected(item);
    }

}
