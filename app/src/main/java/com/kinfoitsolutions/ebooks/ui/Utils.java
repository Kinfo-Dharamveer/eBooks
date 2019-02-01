package com.kinfoitsolutions.ebooks.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import com.google.android.material.snackbar.Snackbar;
import com.kinfoitsolutions.ebooks.R;

public class Utils {


    public static ProgressDialog showProgressDialog(Context context, String message) {
        ProgressDialog m_Dialog = new ProgressDialog(context);
        m_Dialog.setMessage(message);
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(false);
        m_Dialog.show();
        return m_Dialog;

    }


    public static void showSnackBar(Context context, String message, View view) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();

    }


    public static void showNoInternetSnackbar(String message,View containerLayout,String netStatus) {

        Snackbar mSnackBar = Snackbar.make(containerLayout,message , Snackbar.LENGTH_LONG);

        if (netStatus.equals("offline")){
            mSnackBar.getView().setBackgroundColor(Color.RED);
        }
        else {
            mSnackBar.getView().setBackgroundColor(Color.GREEN);

        }

        mSnackBar.show();

    }


}
