package com.example.bansach.Bill;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bansach.R;
import com.example.bansach.Model.BillDetailModel;

import java.util.ArrayList;

public class One_sach_HDCT extends BaseAdapter {
    Context context;
    ArrayList<BillDetailModel> list;

    public One_sach_HDCT(Context context, ArrayList<BillDetailModel> list) {
        this.context = context;
        this.list = list;
    }

    class ViewHolder {
        TextView ten, sl, gia;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        BillDetailModel billDetailModel = list.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.one_sach_hdct, null);
            holder.ten = convertView.findViewById(R.id.lvTenSachHDCT);
            holder.sl = convertView.findViewById(R.id.lvSLSachHDCT);
            holder.gia = convertView.findViewById(R.id.lvGiaSachHDCT);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();

        holder.ten.setText(billDetailModel.getBook().getTenSach());
        holder.gia.setText(billDetailModel.getBook().getGiaBia() + "");
        holder.sl.setText(billDetailModel.getSoLuongMua() + "");
        return convertView;
    }
}
