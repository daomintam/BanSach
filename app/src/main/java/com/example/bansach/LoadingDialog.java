package com.example.bansach;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.view.Window;

import com.example.bansach.R;

public class LoadingDialog {
    Activity activity;
    Dialog dialog;

    public LoadingDialog(Activity activity) {
        this.activity = activity;

    }

    public void startLoad(int x) {
        dialog = new Dialog(activity);
        if (x == 1) {
            dialog.setContentView(R.layout.loading);
        }
        if (x == 2) {
            dialog.setContentView(R.layout.loading1);
        }

        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.show();
    }

    public void dismisLoad() {
        dialog.dismiss();
    }
}
