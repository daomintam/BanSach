package com.example.bansach.Bill;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.bansach.MainActivity;
import com.example.bansach.R;
import com.example.bansach.Adapter.BillAdapter;
import com.example.bansach.Firebase.BillDetailFirebase;
import com.example.bansach.Firebase.BillFirebase;
import com.example.bansach.Firebase.UserFirebase;
import com.example.bansach.Model.BillModel;
import com.example.bansach.Model.BillDetailModel;
import com.example.bansach.Model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class ListBillActivity extends AppCompatActivity {
    public List<BillModel> listBill = new ArrayList<>();
    SwipeMenuListView lvHoaDon;
    public static BillAdapter adapter = null;
    BillFirebase billFireBase;
    BillDetailFirebase billDetailFireBase;
    UserFirebase userFireBase;
    ArrayList<UserModel> userModelArrayList;
    ArrayList<BillDetailModel> listHDCT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_hoa_don);
        setTitle("HOÁ ĐƠN");
        lvHoaDon = findViewById(R.id.lvHoaDon);
        billFireBase = new BillFirebase(ListBillActivity.this);
        billDetailFireBase = new BillDetailFirebase(this);
        listHDCT = billDetailFireBase.getAll();
        Log.i("hdct5", "" + listHDCT.size());
        userFireBase = new UserFirebase(this);
        userModelArrayList = userFireBase.getAllNone();
        listBill = billFireBase.getAll();
        adapter = new BillAdapter(this, listBill);
        lvHoaDon.setAdapter(adapter);

        //Khi click vào từng hóa đơn sẽ show ra hóa đơn chi tiết
        lvHoaDon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String maHD = listBill.get(position).getMaHoaDon();
                ArrayList<BillDetailModel> showBillDetail = new ArrayList<>();
                //Lọc list theo đúng hóa đơn
                for (int i = 0; i < listHDCT.size(); i++) {
                    String ma = listHDCT.get(i).getBillModel().getMaHoaDon();
                    if (maHD.matches(ma)) {
                        showBillDetail.add(listHDCT.get(i));
                    }
                }
                //Hóa đơn trống sẽ bị xóa khi click
//                if(showBillDetail.size()<=0){
//                    Toast.makeText(ListBillActivity.this, "Hóa đơn chưa thanh toán!", Toast.LENGTH_SHORT).show();
//                    billFireBase.delete(maHD);
//                }
                //else {
                final Dialog dialog = new Dialog(ListBillActivity.this);
                dialog.setContentView(R.layout.show_hd_chi_tiet);
                dialog.setCancelable(true);
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (dialog != null && dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }

                TextView ngay, ma, khachHang, sdt, tongTien;
                ListView listView;
                ngay = dialog.findViewById(R.id.showNgayHDCT);
                ma = dialog.findViewById(R.id.showMaHDCT);
                khachHang = dialog.findViewById(R.id.showTenHDCT);
                sdt = dialog.findViewById(R.id.showSdtHDCT);
                tongTien = dialog.findViewById(R.id.tongTienHDCT);
                listView = dialog.findViewById(R.id.lvShowHDCT);

                ngay.setText(showBillDetail.get(0).getBillModel().getNgayMua());
                ma.setText(showBillDetail.get(0).getBillModel().getMaHoaDon());
                //Show thông tin người dùng mua
                String maND = MainActivity.tenTk;
                for (int i = 0; i < userModelArrayList.size(); i++) {
                    if (userModelArrayList.get(i).getUserName().matches(maND)) {
                        khachHang.setText(userModelArrayList.get(i).getHoTen());
                        sdt.setText(userModelArrayList.get(i).getPhone());
                        break;
                    }
                }
                //Đổ adapter vào listView
                One_sach_HDCT adapter = new One_sach_HDCT(ListBillActivity.this, showBillDetail);
                listView.setAdapter(adapter);

                //Tính tổng tiền theo số lượng sách
                int tong = 0;
                for (int i = 0; i < showBillDetail.size(); i++) {
                    tong += (showBillDetail.get(i).getBook().getGiaBia()) * (showBillDetail.get(i).getSoLuongMua());
                }
                tongTien.setText(tong + "");
                dialog.show();
                //}
            }
        });

        // TextFilter
        lvHoaDon.setTextFilterEnabled(true);
        EditText edSeach = (EditText) findViewById(R.id.edSearch);
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

        lvHoaDon.setMenuCreator(creator);
        lvHoaDon.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                final BillModel billModel = listBill.get(position);
                if (index == 0) {
                    final AlertDialog.Builder builder2 = new AlertDialog.Builder(ListBillActivity.this);
                    builder2.setTitle("Cảnh báo");
                    builder2.setMessage("Xóa hóa đơn cũng sẽ luôn hóa đơn chi tiết. Bạn chắc chắn muốn xóa?");
                    builder2.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Xóa hóa đơn sẽ xóa luôn hóa đơn chi tiết
                            billFireBase.delete(billModel.getMaHoaDon());
                            billDetailFireBase.delete(billModel.getMaHoaDon());
                            listBill.clear();
                            listBill.addAll(billFireBase.getAll());
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_category, menu);
        MenuItem menuitem = menu.findItem(R.id.add);
        menuitem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent = new
                        Intent(ListBillActivity.this, BillActivity.class);
                startActivity(intent);
                return (true);
        }
        return super.onOptionsItemSelected(item);
    }
}
