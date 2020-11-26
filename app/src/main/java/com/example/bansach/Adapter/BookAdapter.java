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

import com.example.bansach.R;
import com.example.bansach.Model.BookModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BookAdapter extends BaseAdapter implements Filterable {
    List<BookModel> arrBook;
    List<BookModel> arrSortSach;
    private Filter sachFilter;
    public Activity context;
    public LayoutInflater inflater;

    Locale localeVN = new Locale("vi", "VN");
    NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);

    public BookAdapter(Activity context, List<BookModel> arrBook) {
        super();
        this.context = context;
        this.arrBook = arrBook;
        this.arrSortSach = arrBook;
        this.inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrBook.size();
    }

    @Override
    public Object getItem(int position) {
        return arrBook.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public static class ViewHolder {
        ImageView img;
        TextView txtBookName;
        TextView txtBookPrice;
        TextView txtSoLuong;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_book, null);
            holder.img = (ImageView) convertView.findViewById(R.id.ivIcon);
            holder.txtBookName = (TextView)
                    convertView.findViewById(R.id.tvBookName);
            holder.txtBookPrice = (TextView)
                    convertView.findViewById(R.id.tvBookPrice);
            holder.txtSoLuong = (TextView) convertView.findViewById(R.id.tvSoLuong);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        BookModel bookModel = (BookModel) arrBook.get(position);
        holder.txtBookName.setText("" + bookModel.getTenSach());
        holder.txtSoLuong.setText("Số lượng: " + bookModel.getSoLuong());
        holder.txtBookPrice.setText("Giá: " + currencyVN.format(bookModel.getGiaBia()));
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void changeDataset(List<BookModel> items) {
        this.arrBook = items;
        notifyDataSetChanged();
    }

    public void resetData() {
        arrBook = arrSortSach;
    }

    public Filter getFilter() {
        if (sachFilter == null)
            sachFilter = new CustomFilter();
        return sachFilter;
    }

    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
// We implement here the filter logic
            if (constraint == null || constraint.length() == 0) {
                results.values = arrSortSach;
                results.count = arrSortSach.size();
            } else {
                List<BookModel> listBook = new ArrayList<BookModel>();
                for (BookModel p : arrBook) {
                    if
                    (p.getMaSach().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                        listBook.add(p);
                }
                results.values = listBook;
                results.count = listBook.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                arrBook = (List<BookModel>) results.values;
                notifyDataSetChanged();
            }
        }
    }
}
