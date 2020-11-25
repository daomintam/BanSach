package com.example.bansach.Bill;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.bansach.R;
import com.example.bansach.Adapter.CartAdapter;
import com.example.bansach.Firebase.BillDetailFirebase;
import com.example.bansach.Firebase.BookFirebase;
import com.example.bansach.Model.BillModel;
import com.example.bansach.Model.BillDetailModel;
import com.example.bansach.Model.BookModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class BillDetailActivity extends AppCompatActivity {
    EditText edMaHoaDon, edSoLuong;
    TextView tvThanhTien;
    Spinner spinnerMaSach;
    String maSach = "";
    String ngayHd = "";
    Button btnTru, btnCong;
    Button btnAddBill, btnPay;
    public static ArrayAdapter<BookModel> dataAdapter;
    //    HoaDonChiTietDAO hoaDonChiTietDAO;
//    SachDAO sachDAO;
    BillDetailFirebase billDetailFirebase;
    BookFirebase bookFirebase;
    ArrayList<BillDetailModel> listBillDetail = new ArrayList<>();
    ArrayList<BookModel> listSach;
    SwipeMenuListView lvCart;

    public static CartAdapter adapter = null;
    double thanhTien = 0;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("CHI TIẾT HOÁ ĐƠN");
        setContentView(R.layout.activity_hoa_don_chi_tiet);
        init();
        setSpinner();


        adapter = new CartAdapter(this, listBillDetail);
        lvCart.setAdapter(adapter);

        //Lấy mã hóa đơn từ Thêm hóa đơn
        Intent in = getIntent();
        Bundle b = in.getExtras();
        if (b != null) {
            edMaHoaDon.setText(b.getString("MAHOADON"));
            ngayHd = b.getString("Ngay");
        }

        //Chọn mã sách trong spiner
        spinnerMaSach.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int
                    position, long id) {
                maSach = listSach.get(spinnerMaSach.getSelectedItemPosition()).getMaSach();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //change so luong
        edSoLuong.setText("0");
        findViewById(R.id.btnTru).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int s1 = (Integer.parseInt(edSoLuong.getText().toString()));
                if (s1 <= 0) {
                    return;
                } else if (s1 > 0) {
                    //btnTru.setEnabled(true);
                    int soluong = (Integer.parseInt(edSoLuong.getText() + ""));
                    int sl = soluong - 1;
                    edSoLuong.setText(sl + "");
                }
            }
        });
        findViewById(R.id.btnCong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int soluong = (Integer.parseInt(edSoLuong.getText() + ""));
                int sl = soluong + 1;
                edSoLuong.setText(sl + "");
            }
        });


//        BookModel bookModel=new BookModel();
//        int gia=bookModel.getGiaBia();
//        bookModel.getGiaBia();
//        int sl = (Integer.parseInt(edSoLuong.getText()+""));
//        tvThanhTien.setText(sl*gia);
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

        lvCart.setMenuCreator(creator);

        lvCart.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                final BillDetailModel hd = listBillDetail.get(position);
                if (index == 0) {
                    final AlertDialog.Builder builder2 = new AlertDialog.Builder(BillDetailActivity.this);
                    builder2.setTitle("Cảnh báo");
                    builder2.setMessage("Bạn chắc chắn muốn xóa?");
                    builder2.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            billDetailFirebase.delete(hd.getMaHDCT());
//                            list.clear();
//                            list.addAll(billDetailFirebase.getAll());
                            listBillDetail.remove(position);
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
        });
    }

    private void init() {
        btnAddBill = findViewById(R.id.btnAddBill);
        edMaHoaDon = (EditText) findViewById(R.id.edMaHoaDon);
        edMaHoaDon.setFocusable(false);
        edSoLuong = (EditText) findViewById(R.id.edSoLuongMua);
        spinnerMaSach = findViewById(R.id.spMaSach);
        lvCart = findViewById(R.id.lvCart);
        tvThanhTien = (TextView) findViewById(R.id.tvThanhTien);
        //btnPay=findViewById(R.id.btnPay);
    }


    public void AddBill(View view) {
        billDetailFirebase = new BillDetailFirebase(BillDetailActivity.this);
        bookFirebase = new BookFirebase(BillDetailActivity.this);
        try {
            int sl = 0;
            //Sach
            BookModel bookModel = null;
            int posision = 0;
            //Get list by mã sách
            for (int i = 0; i < listSach.size(); i++) {
                if (listSach.get(i).getMaSach().matches(maSach)) {
                    bookModel = listSach.get(i);
                    break;
                }
            }
            int check = checkListMaSach(listBillDetail, maSach);
            BillModel billModel = new BillModel(edMaHoaDon.getText().toString(), ngayHd);

            //Hóa đơn chi tiết //edMaHoaDon.getText().toString()
            BillDetailModel billDetailModel = new BillDetailModel("1", billModel, bookModel, Integer.parseInt(edSoLuong.getText().toString()));

            if (check >= 0) {
                int soluong = listBillDetail.get(check).getSoLuongMua();
                billDetailModel.setSoLuongMua(soluong +
                        Integer.parseInt(edSoLuong.getText().toString()));
                listBillDetail.set(check, billDetailModel);
                adapter.notifyDataSetChanged();
            } else {
                listBillDetail.add(billDetailModel);
                adapter.notifyDataSetChanged();
            }
        } catch (Exception ex) {
            Log.e("Error", ex.toString());
        }
    }

    public int checkListMaSach(ArrayList<BillDetailModel> listBillDetail, String maSach) {
        int pos = -1;
        for (int i = 0; i < listBillDetail.size(); i++) {
            BillDetailModel billDetailModel = listBillDetail.get(i);
            if (billDetailModel.getBook().getMaSach().equalsIgnoreCase(maSach)) {
                pos = i;
                break;
            }
        }
        return pos;
    }

    public void thanhToanHoaDon(View view) {
        billDetailFirebase = new BillDetailFirebase(BillDetailActivity.this);
        thanhTien = 0;
        try {
            for (BillDetailModel billDetailModel : listBillDetail) {
                billDetailFirebase.insert(billDetailModel);
                thanhTien = thanhTien + billDetailModel.getSoLuongMua() *
                        billDetailModel.getBook().getGiaBia();
            }
            tvThanhTien.setText("Tổng tiền: " + thanhTien + " đ");
            Intent i = new Intent(this, ListBillActivity.class);
            startActivity(i);
        } catch (Exception ex) {
            Log.e("Error", ex.toString());
        }
    }

    public int checkMaSach(String strTheLoai) {
        for (int i = 0; i < listSach.size(); i++) {
            if (strTheLoai.equals(listSach.get(i).getMaSach())) {
                return i;
            }
        }
        return 0;
    }

    private void setSpinner() {
        // spinner
        //Khai báo hàm
        bookFirebase = new BookFirebase(BillDetailActivity.this);
        listSach = bookFirebase.getAllSach();
        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listSach);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaSach.setAdapter(dataAdapter);
    }
}
