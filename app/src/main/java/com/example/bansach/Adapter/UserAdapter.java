package com.example.bansach.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bansach.Firebase.UserFirebase;
import com.example.bansach.MainActivity;
import com.example.bansach.R;
import com.example.bansach.Model.UserModel;

import java.util.ArrayList;

import static com.example.bansach.MainActivity.checkAdmin;


public class UserAdapter extends BaseAdapter {
    public static ArrayList<UserModel> arrUser;
    public Activity context;

    public UserAdapter(Activity context, ArrayList<UserModel> arrUser) {
        super();
        this.context = context;
        this.arrUser = arrUser;
    }

    @Override
    public int getCount() {
        return arrUser.size();
    }

    @Override
    public Object getItem(int position) {
        return arrUser.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public static class ViewHolder {
        ImageView img;
        TextView txtName;
        TextView txtPhone;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.item_nguoi_dung, null);
            holder.img = (ImageView) convertView.findViewById(R.id.ivIcon);
            holder.txtName = (TextView) convertView.findViewById(R.id.tvName);
            holder.txtPhone = (TextView) convertView.findViewById(R.id.tvPhone);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        UserModel _entry = (UserModel) arrUser.get(position);
        if (checkAdmin == true) {
            if (position % 3 == 0 || position % 3 == 1) {
                holder.img.setImageResource(R.drawable.vip);
            }
        }
        if (checkAdmin == false) {
            if (position % 3 == 0) {
                holder.img.setImageResource(R.drawable.emone);
            } else if (position % 3 == 1) {
                holder.img.setImageResource(R.drawable.emtwo);
            } else {
                holder.img.setImageResource(R.drawable.emthree);
            }
        }

        holder.txtName.setText(_entry.getHoTen());
        holder.txtPhone.setText(_entry.getPhone());
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void changeDataset(ArrayList<UserModel> items) {
        this.arrUser = items;
        notifyDataSetChanged();
    }
}
