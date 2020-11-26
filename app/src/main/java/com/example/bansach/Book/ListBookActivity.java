package com.example.bansach.Book;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.bansach.MainActivity;
import com.example.bansach.R;
import com.example.bansach.Adapter.BookAdapter;
import com.example.bansach.Firebase.BookFirebase;
import com.example.bansach.Model.BookModel;

import java.util.ArrayList;

public class ListBookActivity extends AppCompatActivity {
    public static ArrayList<BookModel> list;
    SwipeMenuListView lvBook;
    public static BookAdapter adapter;
    //    SachDAO sachDAO;
    BookFirebase bookFirebase;
    public static Button btnThemSach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("QUẢN LÝ SÁCH");
        setContentView(R.layout.activity_list_book);
        getSupportActionBar().hide();
        View b = findViewById(R.id.btnThemSach);
        if (MainActivity.checkAdmin == true) {
            b.setVisibility(View.VISIBLE);
            findViewById(R.id.btnThemSach).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MainActivity.checkAdmin == true) {
                        Intent in = new Intent(ListBookActivity.this, AddBookActivity.class);
                        startActivity(in);
                    } else

                        finish();
                }
            });
        } else b.setVisibility(View.GONE);


        lvBook = findViewById(R.id.lvBook);
        bookFirebase = new BookFirebase(this);
        //đổ sách từ FireBase vào List
        list = bookFirebase.getAll();
        adapter = new BookAdapter(this, list);
        lvBook.setAdapter(adapter);
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

        lvBook.setMenuCreator(creator);
        lvBook.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                if (MainActivity.checkAdmin == true) {
                    final BookModel bookModel = list.get(position);
                    if (index == 0) {
                        final AlertDialog.Builder builder2 = new AlertDialog.Builder(ListBookActivity.this);
                        builder2.setTitle("Cảnh báo");
                        builder2.setMessage("Bạn có chắc chắn xóa?");
                        builder2.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//
                                bookFirebase.delete(bookModel.getMaSach());
                                list.clear();
                                list.addAll(bookFirebase.getAll());
                                adapter.notifyDataSetChanged();

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


        lvBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                BookModel bookModel = (BookModel) parent.getItemAtPosition(position);
                Intent intent = new
                        Intent(ListBookActivity.this, EditBookActivity.class);
                Bundle b = new Bundle();
                b.putString("MASACH", bookModel.getMaSach());
                b.putString("MATHELOAI", bookModel.getMaTheLoai());
                b.putString("TENSACH", bookModel.getTenSach());
                b.putString("TACGIA", bookModel.getTacGia());
                b.putString("NXB", bookModel.getNXB());
                b.putString("GIABIA", String.valueOf(bookModel.getGiaBia()));
                b.putString("SOLUONG", String.valueOf(bookModel.getSoLuong()));
                intent.putExtras(b);
                startActivity(intent);
            }
        });
// TextFilter
        lvBook.setTextFilterEnabled(true);
        EditText edSeach = (EditText) findViewById(R.id.edSearchBook);
        edSeach.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int
                    count) {
                System.out.println("Text [" + s + "] - Start [" + start + "] - Before [" + before + "] - Count [" + count + "]");
                if (count < before) {
                    adapter.resetData();
                }
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book, menu);
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
                        Intent(ListBookActivity.this, AddBookActivity.class);
                startActivity(intent);
                return (true);
        }
        return super.onOptionsItemSelected(item);
    }


}
