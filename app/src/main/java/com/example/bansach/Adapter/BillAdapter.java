package com.example.bansach.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bansach.R;
import com.example.bansach.Model.BillModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BillAdapter extends BaseAdapter implements Filterable {
    List<BillModel> arrBill;
    List<BillModel> arrSortHoaDon;
    private Filter hoaDonFilter;
    public Activity context;
    public LayoutInflater inflater;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private int layout;

    public BillAdapter(Activity context, List<BillModel> arrayHoaDon) {
        super();
        this.context = context;
        this.arrBill = arrayHoaDon;
        this.arrSortHoaDon = arrayHoaDon;
        this.inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return arrBill.size();
    }

    @Override
    public Object getItem(int position) {
        return arrBill.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public static class ViewHolder {
        ImageView img;
        TextView txtMaHoaDon;
        TextView txtNgayMua;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_hoadon, null);
            holder.img = (ImageView) convertView.findViewById(R.id.ivIcon);
            holder.txtMaHoaDon = (TextView)
                    convertView.findViewById(R.id.tvMaHoaDon);
            holder.txtNgayMua = (TextView) convertView.findViewById(R.id.tvNgayMua);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        BillModel billModel = (BillModel) arrBill.get(position);
        holder.txtMaHoaDon.setText(billModel.getMaHoaDon());
        holder.txtNgayMua.setText(billModel.getNgayMua());
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void changeDataset(List<BillModel> items) {
        this.arrBill = items;
        notifyDataSetChanged();
    }

    public void resetData() {
        arrBill = arrSortHoaDon;
    }

    public Filter getFilter() {
        if (hoaDonFilter == null)
            hoaDonFilter = new CustomFilter();
        return hoaDonFilter;
    }

    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = arrSortHoaDon;
                results.count = arrSortHoaDon.size();
            } else {
                List<BillModel> listBill = new ArrayList<BillModel>();
                for (BillModel p : arrBill) {
                    if
                    (p.getMaHoaDon().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                        listBill.add(p);
                }
                results.values = listBill;
                results.count = listBill.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
// Now we have to inform the adapter about the new list filtered
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                arrBill = (List<BillModel>) results.values;
                notifyDataSetChanged();
            }
        }
    }
}
