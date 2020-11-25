package com.example.bansach.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bansach.R;
import com.example.bansach.Model.BillDetailModel;

import java.util.List;

public class CartAdapter extends BaseAdapter {
    List<BillDetailModel> arrBillDetail;
    public Activity context;
    public LayoutInflater inflater;

    public CartAdapter(Activity context, List<BillDetailModel> arrBillDetail) {
        super();
        this.context = context;
        this.arrBillDetail = arrBillDetail;
        this.inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrBillDetail.size();
    }

    @Override
    public Object getItem(int position) {
        return arrBillDetail.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public static class ViewHolder {
        TextView txtMaSach;
        TextView txtSoLuong;
        TextView txtGiaBia;
        TextView txtThanhTien;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_cart, null);
            holder.txtMaSach = (TextView) convertView.findViewById(R.id.tvMaSach);
            holder.txtSoLuong = (TextView) convertView.findViewById(R.id.tvSoLuong);
            holder.txtGiaBia = (TextView) convertView.findViewById(R.id.tvGiaBia);
            holder.txtThanhTien = (TextView)
                    convertView.findViewById(R.id.tvThanhTien);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        BillDetailModel billDetailModel = (BillDetailModel) arrBillDetail.get(position);
        holder.txtMaSach.setText("Mã sách: " + billDetailModel.getBook().getMaSach());
        holder.txtSoLuong.setText("Số lượng: " + billDetailModel.getSoLuongMua());
        holder.txtGiaBia.setText("Giá bìa: " + billDetailModel.getBook().getGiaBia() + " VND");
        holder.txtThanhTien.setText("Thành tiền: " + billDetailModel.getSoLuongMua() * billDetailModel.getBook().getGiaBia() + " VND");
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void changeDataset(List<BillDetailModel> items) {
        this.arrBillDetail = items;
        notifyDataSetChanged();
    }
}
