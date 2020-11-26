package com.example.bansach.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bansach.R;
import com.example.bansach.Model.CategoryModel;

import java.util.ArrayList;

public class CategoryAdapter extends BaseAdapter {
    public static ArrayList<CategoryModel> arrCategory;
    public Context context;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> arrCategory) {
        super();
        this.context = context;
        this.arrCategory = arrCategory;
    }

    @Override
    public int getCount() {
        return arrCategory.size();
    }

    @Override
    public Object getItem(int position) {
        return arrCategory.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public static class ViewHolder {
        ImageView img;
        TextView txtMaTheLoai;
        TextView txtTenTheLoai;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_theloai, null);
            holder.img = (ImageView) convertView.findViewById(R.id.ivIcon);
            holder.txtMaTheLoai = (TextView)
                    convertView.findViewById(R.id.tvMaTheLoai);
            holder.txtTenTheLoai = (TextView)
                    convertView.findViewById(R.id.tvTenTheLoai);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        CategoryModel _entry = (CategoryModel) arrCategory.get(position);
        holder.txtMaTheLoai.setText(_entry.getMaTheLoai());
        holder.txtTenTheLoai.setText(_entry.getTenTheLoai());
        return convertView;
    }
}